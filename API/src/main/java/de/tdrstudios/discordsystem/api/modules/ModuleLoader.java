package de.tdrstudios.discordsystem.api.modules;

import com.google.inject.Singleton;

import java.io.File;
import java.util.Collection;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
@Singleton
public interface ModuleLoader {
    Module loadFile(File file);
    void enableModule(Module module);
    void disableModule(Module module);
    void unloadModule(File file);

    Collection<Module> getLoadOrder();
}
