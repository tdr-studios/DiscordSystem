package de.tdrstudios.discordsystem.console;

import lombok.Getter;

import java.io.PrintStream;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public class LogSystem {
    @Getter
    private Logger logger;

    public LogSystem(Logger logger) {
        this.logger = logger;
        initWriter();
    }

    private void initWriter() {
        error = create(Level.SEVERE);
        warning = create(Level.WARNING);
        out = create(Level.INFO);
        debug = create(Level.CONFIG);
    }

    public void error(String msg) {
        logger.severe(msg);
    }

    public void warning(String msg) {
        logger.warning(msg);
    }

    public void write(String msg) {
        logger.info(msg);
    }

    public void debug(String msg) {
        logger.config(msg);
    }


    public void lineSeperator() {
        this.write(" ");

    }
    public void lineSeperator(int count) throws InterruptedException {
        while(count > 0) {
            this.lineSeperator();
            count = count - 1;
            TimeUnit.MILLISECONDS.sleep(2);
        }

    }

    @Getter
    private PrintStream debug;

    @Getter
    private PrintStream out;

    @Getter
    private PrintStream warning;

    @Getter
    private PrintStream error;

    private PrintStream create(Level level) {
        return new PrintStream(new ActionOutputStream(new Consumer<String>() {
            @Override
            public void accept(String s) {
                logger.log(level, s);
            }
        }), true);
    }
}
