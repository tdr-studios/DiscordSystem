package de.tdrstudios.discordsystem.utils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author PXAV
 */
public class ReflectionUtils {
    public static Collection<Class<?>> filter(String[] packageNames, ClassCriteria... criteria) {
        // validate given parameters
        Preconditions.checkNotNull(packageNames);
        Preconditions.checkNotNull(criteria);

        Set<Class<?>> output = Sets.newHashSet();

        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo()
                .whitelistPackages(packageNames)
                .scan()) {

            ClassInfoList allClasses = scanResult.getAllClasses();

            // iterate all classes in the packages and check if they
            // math the criteria
            for (ClassInfo current : allClasses) {
                Class<?> c = current.loadClass();
                boolean allMatch = true;
                for (ClassCriteria criterion : criteria) {
                    if (!criterion.matches(c)) {
                        allMatch = false;
                        break;
                    }
                }
                if (allMatch) {
                    output.add(c);
                }
            }
        }

        return Collections.unmodifiableList(output.stream().filter(Objects::nonNull).collect(Collectors.toList()));
    }

    public static Collection<Method> filter(Class<?>[] classes, MethodCriteria... criteria) {
        // validate given parameters
        Preconditions.checkNotNull(classes);
        Preconditions.checkNotNull(criteria);

        Set<Method> output = Sets.newHashSet();

        for (Class<?> clazz : classes) {
            for (Method method : clazz.getMethods()) {
                boolean allMatch = true;
                for (MethodCriteria criterion : criteria) {
                    if (!criterion.matches(method)) allMatch = false;
                }
                if (allMatch) output.add(method);
            }
        }

        return Collections.unmodifiableList(output.stream().filter(Objects::nonNull).collect(Collectors.toList()));
    }
}
