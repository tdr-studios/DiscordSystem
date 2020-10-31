package de.tdrstudios.discordsystem.api;

import de.tdrstudios.discordsystem.api.services.CreateService;
import de.tdrstudios.discordsystem.api.services.Service;
import de.tdrstudios.discordsystem.version.module.DownloadableModule;

import java.util.UUID;


public interface ModuleDownloadService extends Service {
    void loadRepository(String url);

    void downloadModule(DownloadableModule module);

    DownloadableModule searchModule(String name, String... authors);

    void reloadRepositories();
}
