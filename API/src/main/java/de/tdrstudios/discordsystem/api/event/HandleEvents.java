package de.tdrstudios.discordsystem.api.event;

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
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HandleEvents {
}
