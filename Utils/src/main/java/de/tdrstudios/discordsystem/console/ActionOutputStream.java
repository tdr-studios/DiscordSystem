package de.tdrstudios.discordsystem.console;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public class ActionOutputStream extends ByteArrayOutputStream {
    private final Consumer<String> consumer;

    public ActionOutputStream(Consumer<String> consumer) {
        this.consumer = consumer;
    }

    private final String separator = System.lineSeparator();

    @Override
    public void flush() throws IOException {
        synchronized (this) {
            super.flush();
            String record = this.toString();
            super.reset();
            if ((record.length() > 0) && (!record.equals(separator))) {
                consumer.accept(record);
            }

        }
    }
}
