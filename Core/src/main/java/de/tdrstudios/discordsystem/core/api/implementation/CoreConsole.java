package de.tdrstudios.discordsystem.core.api.implementation;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import de.tdrstudios.discordsystem.api.services.CreateService;
import de.tdrstudios.discordsystem.console.ActionOutputStream;
import de.tdrstudios.discordsystem.console.Console;
import de.tdrstudios.discordsystem.console.ConsoleColor;
import org.fusesource.jansi.AnsiConsole;
import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
@Singleton
@CreateService
public class CoreConsole implements Console {
    private Terminal terminal;
    private LineReaderImpl lineReader;
    private String prompt;
    private boolean loopRunning;
    private boolean reading;
    private Future<?> loopFuture = null;

    @Inject
    private ExecutorService service;
    @Inject
    private Injector injector;

    public CoreConsole() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        try {
            AnsiConsole.systemInstall();
            terminal = TerminalBuilder.builder().dumb(true).system(true).build();
            lineReader = new LineReaderImpl(terminal);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger = Logger.getAnonymousLogger();
        logger.setUseParentHandlers(false);
        ConsoleHandler handler = new ConsoleHandler();
        Method setOutputStream = StreamHandler.class.getDeclaredMethod("setOutputStream", OutputStream.class);
        setOutputStream.setAccessible(true);
        setOutputStream.invoke(handler, new ActionOutputStream(this::write));
        handler.setFormatter(new LogFormatter(true));
        Logger.getLogger("org.jline").setUseParentHandlers(false);
        logger.addHandler(handler);
    }

    public boolean isLoopRunning() {
        return this.loopRunning;
    }

    public boolean isReading() {
        return this.reading;
    }

    public Consumer<String> getMessageConsumer() {
        return this.messageConsumer;
    }

    public void setMessageConsumer(Consumer<String> messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    public class ConsoleHandler extends java.util.logging.ConsoleHandler {
        @Override
        public void publish(LogRecord record) {
            String[] split = record.getMessage().split(System.lineSeparator());
            if (split.length != 0) {
                for (String s : split) {
                    super.publish(clone(record, s));
                }
            }else super.publish(record);
        }
        public LogRecord clone(LogRecord record, String msg) {
            LogRecord r = new LogRecord(record.getLevel(), msg);
            r.setLoggerName(record.getLoggerName());
            r.setMillis(record.getMillis());
            r.setParameters(record.getParameters());
            r.setResourceBundle(record.getResourceBundle());
            r.setResourceBundleName(record.getResourceBundleName());
            r.setSequenceNumber(record.getSequenceNumber());
            r.setSourceClassName(record.getSourceClassName());
            r.setSourceMethodName(record.getSourceMethodName());
            r.setThreadID(record.getThreadID());
            r.setThrown(record.getThrown());
            return r;
        }
    }

    private Logger logger;

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public void startLoop() {
        if (loopFuture != null) return;
        loopRunning = true;
        loopFuture = service.submit(new Runnable() {
            @Override
            public void run() {
                while (loopRunning) {
                    while (reading) {
                        String s = readLine();
                        if (messageConsumer != null) messageConsumer.accept(s);
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private Consumer<String> messageConsumer;

    @Override
    public void stopLoop() {
        stopReading();
        loopFuture.cancel(true);
    }

    @Override
    public void startReading() {
        reading = true;
    }

    @Override
    public void stopReading() {
        reading = false;
    }

    @Override
    public void setPrompt(String prompt) {
        prompt = ConsoleColor.toColouredString('&', prompt);
        this.prompt = prompt;
        lineReader.setPrompt(ConsoleColor.toColouredString('&', prompt));
    }

    @Override
    public String readLine() {
        return lineReader.readLine(ConsoleColor.toColouredString('&', prompt));
    }

    @Override
    public void write(String message) {
        message = ConsoleColor.toColouredString('&', message);
        lineReader.printAbove(message);
    }
}
