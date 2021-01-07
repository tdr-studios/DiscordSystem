package de.tdrstudios.discordsystem.version.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import de.tdrstudios.discordsystem.utils.GsonUtils;
import de.tdrstudios.discordsystem.utils.JsonDocument;
import de.tdrstudios.discordsystem.version.WebHelper;
import de.tdrstudios.discordsystem.version.module.DownloadableModule;
import de.tdrstudios.discordsystem.version.module.ModuleType;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

import java.util.ArrayList;
import java.util.List;


public class ModuleRepository {
    private List<DownloadableModule> modules;

    private String name;

    private ModuleRepositoryType type;

    public static ModuleRepository findRepository(WebHelper webHelper) {
        JsonDocument config = webHelper.getFromFile("config.repo");
        boolean useDefault = config == null;
        String fileName = "repo.json";
        if (!useDefault) {
            if (config.has("dc2Repo")) {
                JsonDocument doc = config.getDocument("dc2Repo");
                if (doc.has("fileName")) {
                    fileName = doc.getString("fileName");
                }
            }
        }
        JsonDocument document = webHelper.getFromFile(fileName);
        if (document == null) throw new IllegalArgumentException("Repo not valid! "+webHelper.getBaseUrl());
        String repoName = document.getString("name");
        ModuleRepositoryType type = ModuleRepositoryType.OPTIONAL;
        if (document.has("type")) {
            type = ModuleRepositoryType.valueOf(document.getString("type").toUpperCase());
        }
        JsonArray array = document.getObject().getAsJsonArray("modules");
        List<DownloadableModule> modules = new ArrayList<>();
        for (JsonElement element : array) {
            if (element.isJsonObject()) {
                DownloadableModule module = GsonUtils.get().fromJson(element, DownloadableModule.class);
                if (module.getType() == null) module.setType(ModuleType.UNDEFINED);
                module.setCoreVersion(null);
                module.setInstalledVersion(null);
                modules.add(module);
            }
        }
        ModuleRepository repository = new ModuleRepository();
        repository.modules = modules;
        repository.name = repoName;
        repository.type = type;
        return repository;
    }

    public ModuleRepository setCoreVersion(String version) {
        DefaultArtifactVersion coreVersion = new DefaultArtifactVersion(version);
        for (DownloadableModule module : modules) {
            module.setCoreVersion(coreVersion);
        }
        return this;
    }

    public List<DownloadableModule> getModules() {
        return this.modules;
    }

    public String getName() {
        return this.name;
    }

    public ModuleRepositoryType getType() {
        return this.type;
    }
}
