package de.tdrstudios.discordsystem.api;

import de.tdrstudios.discordsystem.api.services.Service;
import de.tdrstudios.discordsystem.version.module.DownloadableModule;


public interface ModuleDownloadService extends Service {
    void loadRepository(String url);

    void downloadModule(DownloadableModule module);

    DownloadableModule searchModule(String name, String... authors);

    void reloadRepositories();
}
