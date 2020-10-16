package de.tdrstudios.discordsystem.utils;

import io.github.classgraph.ClassInfo;

import java.lang.annotation.Annotation;

/**
 * @author PXAV
 */
public interface Criteria {
    boolean matches(Class<?> c);
    static Criteria annotatedWith(Class<? extends Annotation> annotation) {
        return c -> c.isAnnotationPresent(annotation);
    }
    static Criteria subclassOf(Class<?> parentClass) {
        return c -> !parentClass.equals(c) && parentClass.isAssignableFrom(c);
    }
    static Criteria locatedIn(String packageName) {
        return c -> c.getPackage().getName().equals(packageName);
    }
}
