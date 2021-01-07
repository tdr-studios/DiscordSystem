package de.tdrstudios.discordsystem.api.services;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ComparableService implements Comparable<ComparableService> {
    public Service getService() {
        return service;
    }

    public int getSorter() {
        return sorter;
    }

    private final Service service;
    private final int sorter;

    public ComparableService(Service service) {
        this.service = service;
        Class<? extends Service> aClass = service.getClass();
        CreateService annotation = aClass.getAnnotation(CreateService.class);
        sorter = annotation.value();
    }

    @Override
    public int compareTo(@NotNull ComparableService o) {
        return Integer.compare(o.getSorter(), sorter);
    }

    public static Collection<Service> sort(Collection<Service> services) {
        List<ComparableService> comparableServices = new ArrayList<>();
        for (Service service : services) {
            comparableServices.add(new ComparableService(service));
        }
        services = null;
        Collections.sort(comparableServices);
        List<Service> nServices = new ArrayList<>();
        for (ComparableService comparableService : comparableServices) {
            nServices.add(comparableService.getService());
        }
        return nServices;
    }
}
