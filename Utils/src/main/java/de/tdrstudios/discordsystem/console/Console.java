package de.tdrstudios.discordsystem.console;

import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public interface Console {
    Logger getLogger();
    boolean isLoopRunning();
    boolean isReading();
    void startLoop();
    void setMessageConsumer(Consumer<String> consumer);
    Consumer<String> getMessageConsumer();
    void stopLoop();
    void startReading();
    void stopReading();
    void setPrompt(String prompt);
    String readLine();
    void write(String message);
}
