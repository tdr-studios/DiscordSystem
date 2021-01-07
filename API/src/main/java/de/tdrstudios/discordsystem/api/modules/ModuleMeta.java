package de.tdrstudios.discordsystem.api.modules;

import java.io.File;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public class ModuleMeta {
    private final String name;

    public String getVersion() {
        return version;
    }

    public String[] getAuthors() {
        return authors;
    }

    public String getDescription() {
        return description;
    }

    public String[] getHardDependencies() {
        return hardDependencies;
    }

    public String[] getSoftDependencies() {
        return softDependencies;
    }

    public String getMainClass() {
        return mainClass;
    }

    public File getFile() {
        return file;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    private final String version;
    private final String[] authors;
    private final String description;
    private final String[] hardDependencies;
    private final String[] softDependencies;
    private final String mainClass;
    private final File file;
    private final ClassLoader classLoader;

    public ModuleMeta(String name,
                      String version,
                      String[] authors,
                      String description,
                      String[] hardDependencies,
                      String[] softDependencies,
                      String mainClass,
                      File file,
                      ClassLoader classLoader) {
        this.name = name;
        this.version = version;
        this.authors = authors;
        this.description = description;
        this.hardDependencies = hardDependencies;
        this.softDependencies = softDependencies;
        this.mainClass = mainClass;
        this.file = file;
        this.classLoader = classLoader;
    }

    public String getName() {
        return name;
    }
}
