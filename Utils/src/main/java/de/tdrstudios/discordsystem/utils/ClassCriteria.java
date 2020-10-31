package de.tdrstudios.discordsystem.utils;

import java.lang.annotation.Annotation;

/**
 * @author PXAV
 */
public interface ClassCriteria {
    boolean matches(Class<?> c);
    static ClassCriteria annotatedWith(Class<? extends Annotation> annotation) {
        return c -> c.isAnnotationPresent(annotation);
    }
    static ClassCriteria subclassOf(Class<?> parentClass) {
        return c -> !parentClass.equals(c) && parentClass.isAssignableFrom(c);
    }
    static ClassCriteria locatedIn(String packageName) {
        return c -> c.getPackage().getName().equals(packageName);
    }
}
