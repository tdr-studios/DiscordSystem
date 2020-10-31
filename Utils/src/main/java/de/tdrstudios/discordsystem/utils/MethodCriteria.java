package de.tdrstudios.discordsystem.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author PXAV
 */
public interface MethodCriteria {
    boolean matches(Method m);
    static MethodCriteria annotatedWith(Class<? extends Annotation> annotation) {
        return m -> m.isAnnotationPresent(annotation);
    }

    static MethodCriteria isPublic() {
        return m -> Modifier.isPublic(m.getModifiers());
    }

    static MethodCriteria isPrivate() {
        return m -> Modifier.isPrivate(m.getModifiers());
    }

    static MethodCriteria isStatic() {
        return m -> Modifier.isStatic(m.getModifiers());
    }

    static MethodCriteria isAbstract() {
        return m -> Modifier.isAbstract(m.getModifiers());
    }

    static MethodCriteria isProtected() {
        return m -> Modifier.isProtected(m.getModifiers());
    }

    static MethodCriteria isSynchronized() {
        return m -> Modifier.isSynchronized(m.getModifiers());
    }

    static MethodCriteria isVolatile() {
        return m -> Modifier.isVolatile(m.getModifiers());
    }

    static MethodCriteria parameterCount(int parameterCount) {
        return m -> m.getParameterCount() == parameterCount;
    }

    static MethodCriteria parameterType(int index, Class<?> clazz) {
        return m -> {
            Class<?>[] types = m.getParameterTypes();
            if (types.length == 0) return false;
            if (!(types.length >= index)) return false;
            return types[index].getName().equals(clazz.getName());
        };
    }

    static MethodCriteria parameterType(Class<?> clazz) {
        return m -> {
            Class<?>[] types = m.getParameterTypes();
            if (types.length == 0) return false;
            return types[0].getName().equals(clazz.getName());
        };
    }
}
