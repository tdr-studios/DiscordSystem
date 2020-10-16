package de.tdrstudios.discordsystem.api.modules;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
@AllArgsConstructor
@Getter
public class ModuleMeta {
    private final String name;
    private final String version;
    private final String[] authors;
    private final String description;
    private final String[] hardDependencies;
    private final String[] softDependencies;
    private final String mainClass;
    private final File file;
    private final ClassLoader classLoader;
}
