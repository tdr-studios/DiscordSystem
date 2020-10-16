package de.tdrstudios.discordsystem.api.modules;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CreateModule {
    String name();
    String version();
    String[] authors();
    String description() default "";
    String[] hardDependencies() default {};
    String[] softDependencies() default {};
}
