package de.tdrstudios.discordsystem.core.api.implementation;

import de.tdrstudios.discordsystem.console.ConsoleColor;

import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public class LogFormatter extends Formatter {

    private final boolean colored;

    public LogFormatter(boolean colored) {
        this.colored = colored;
    }

    private static final SimpleDateFormat format = new SimpleDateFormat("dd.MM HH:mm:ss");

    private static final ConsoleColor braceColor = ConsoleColor.DARK_GRAY;

    private static final ConsoleColor defaultColor = ConsoleColor.DEFAULT;

    private static final ConsoleColor errorColor = ConsoleColor.RED;
    private static final ConsoleColor errorTextColor = ConsoleColor.YELLOW;

    @Override
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder();
        if (colored) builder.append(braceColor);
        builder.append("[");
        if (colored) builder.append(ConsoleColor.WHITE);
        builder.append(format.format(record.getMillis()));
        if (colored) builder.append(braceColor);
        builder.append("]");
        if (colored) {
            if (record.getLevel().intValue() > 800) {
                builder.append(errorColor);
            }else builder.append(braceColor);
        }
        builder.append(" ");
        builder.append(record.getLevel().getName().toUpperCase());
        if (colored) builder.append(braceColor);
        builder.append(": ");
        if (colored) {
            if (record.getLevel().intValue() > 800) {
                builder.append(errorTextColor);
            }else builder.append(defaultColor);
        }
        builder.append(record.getMessage());
        builder.append(System.lineSeparator());
        return builder.toString();
    }
}
