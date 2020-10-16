package de.tdrstudios.discordsystem.api.commands;

import com.google.inject.Singleton;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
@Singleton
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CreateCommand {
    String name();
    ExecutorType executorType();
}
