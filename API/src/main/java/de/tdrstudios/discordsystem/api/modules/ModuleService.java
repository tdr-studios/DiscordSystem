package de.tdrstudios.discordsystem.api.modules;

import de.tdrstudios.discordsystem.api.services.CreateService;
import de.tdrstudios.discordsystem.api.services.Service;

import java.io.File;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
@CreateService
public interface ModuleService extends Service {
    void loadModules(File folder);

    void unloadModules(File folder);
    void unloadModule(Module module);
    void unloadAll();

    void disableAll();
    void enableAll();
    void enable(Module module);
    void disable(Module module);

    Module getModule(String name);
    Module getModule(File file);

    ClassLoader[] getClassLoaders();

    void startModules();

    void callAction(ModuleAction action);
    void callAction(Module module, ModuleAction action);
}
