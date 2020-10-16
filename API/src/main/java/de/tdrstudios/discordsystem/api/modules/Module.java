package de.tdrstudios.discordsystem.api.modules;

import com.google.inject.Injector;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public class Module {
    private Injector injector;
    private ModuleMeta meta;

    public Module(){}

    public void init(Injector injector, ModuleMeta meta) {
        this.injector = injector;
        this.meta = meta;
    }

    public <T> T getInstance(Class<T> clazz) {
        return injector.getInstance(clazz);
    }

    public Injector getInjector() {
        return injector;
    }

    public File getFile() {
        return meta.getFile();
    }

    public ModuleMeta getMeta() {
        return meta;
    }

    public String getName() {
        return meta.getName();
    }

    public String getDescription() {
        return meta.getDescription();
    }

    public String getVersion() {
        return meta.getVersion();
    }

    public String[] getAuthors() {
        return meta.getAuthors();
    }

    public String[] getHardDependencies() {
        return meta.getHardDependencies();
    }

    public String[] getSoftDependencies() {
        return meta.getSoftDependencies();
    }

    public String[] getDependencies() {
        List<String> strings = new ArrayList<>();
        strings.addAll(Arrays.asList(getHardDependencies()));
        strings.addAll(Arrays.asList(getSoftDependencies()));
        return strings.toArray(new String[strings.size()]);
    }

    public void onLoad() {}

    public void onEnable() {}

    public void onDisable() {}
}
