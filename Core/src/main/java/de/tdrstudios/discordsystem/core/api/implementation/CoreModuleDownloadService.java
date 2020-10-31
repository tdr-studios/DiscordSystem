package de.tdrstudios.discordsystem.core.api.implementation;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.tdrstudios.discordsystem.api.Discord;
import de.tdrstudios.discordsystem.api.ModuleDownloadService;
import de.tdrstudios.discordsystem.api.modules.Module;
import de.tdrstudios.discordsystem.api.modules.ModuleAction;
import de.tdrstudios.discordsystem.api.modules.ModuleService;
import de.tdrstudios.discordsystem.api.services.CreateService;
import de.tdrstudios.discordsystem.version.WebHelper;
import de.tdrstudios.discordsystem.version.module.DownloadableModule;
import de.tdrstudios.discordsystem.version.module.DownloadableModuleVersion;
import de.tdrstudios.discordsystem.version.repository.ModuleRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Singleton
@CreateService
public class CoreModuleDownloadService implements ModuleDownloadService {
    @Inject
    private ModuleService moduleService;
    private List<ModuleRepository> repositories;


    @Override
    public void loadRepository(String url) {
        ModuleRepository repository = null;
        try {
            repository = ModuleRepository.findRepository(new WebHelper(url));
            repository.setCoreVersion(Discord.getVersion());
            for (DownloadableModule module : repository.getModules()) {
                Module m = moduleService.getModule(module.getName());
                if (m == null) continue;
                if (Arrays.equals(m.getAuthors(), module.getAuthors())) {
                    module.setInstalledVersion(new DefaultArtifactVersion(m.getVersion()));
                }
            }
            repositories.add(repository);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void downloadModule(DownloadableModule module) {
        if (module == null) return;
        if (!module.isUptoDate()) {
            DownloadableModuleVersion latestSupported = module.getLatestSupported();
            String fileName = FilenameUtils.getName(module.getLatestSupported().getUrl());
            if (!fileName.endsWith(".jar")) {
                System.err.println("[Modules] Failed to download! The file is not a jar file");
                return;
            }
            File file = new File("modules", fileName);
            if (file.exists()) {
                System.err.println("[Modules] Failed to download! A file with the name "+fileName+" already exists");
                return;
            }
            try {
                FileUtils.copyURLToFile(new URL(latestSupported.getUrl()), file);
                ModuleService moduleService = Discord.getInstance(ModuleService.class);
                Module m = moduleService.loadModule(file);
                moduleService.callAction(m, ModuleAction.EVENT_READY);
                moduleService.callAction(m, ModuleAction.COMMAND_READY);
                moduleService.callAction(m, ModuleAction.DISCORD_READY);
                moduleService.callAction(m, ModuleAction.ENABLE);
                moduleService.callAction(m, ModuleAction.STARTED);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public DownloadableModule searchModule(String name, String... authors) {
        name = name.toLowerCase();
        for (int i = 0; i < authors.length; i++) {
            authors[i] = authors[i].toLowerCase();
        }
        final int authorCount = authors.length;
        final boolean noAuthors = authorCount == 0;
        List<DownloadableModule> modules = new ArrayList<>();
        for (ModuleRepository repository : repositories) {
            for (DownloadableModule module : repository.getModules()) {
                if (module.getName().toLowerCase().equals(name)) {
                    int matches = 0;
                    for (String author : module.getAuthors()) {
                        for (String a : authors) {
                            if (a.equals(author.toLowerCase())) matches++;
                        }
                    }
                    if (noAuthors) modules.add(module);
                    int moduleauthorCount = module.getAuthors().length;
                    if (getPercentage(moduleauthorCount, authorCount) >= 50) modules.add(module);
                }
            }
        }
        if (noAuthors) {
            if (modules.size() == 0) return null;
            return modules.get(modules.size()-1);
        }
        List<DownloadableModule> highestPercentage = new ArrayList<>();
        double highest = 0.0;
        for (DownloadableModule module : modules) {
            double percentage = getPercentage(module.getAuthors().length, authorCount);
            if (!(percentage <= highest)) {
                highest = percentage;
                highestPercentage.add(module);
            }
        }
        if (highestPercentage.size() == 0) return null;
        return highestPercentage.get(highestPercentage.size()-1);
    }

    private double getPercentage(int base, int value) {
        return ((double)value/base)*100.0;
    }



    @Override
    public void reloadRepositories() {

    }

    @Override
    public void initialize() throws Exception {
        repositories = new ArrayList<>();
        loadRepository("https://api.tdrstudios.de");
    }
}
