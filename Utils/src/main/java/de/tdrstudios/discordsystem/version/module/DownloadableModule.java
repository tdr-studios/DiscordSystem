package de.tdrstudios.discordsystem.version.module;

import com.google.gson.annotations.Expose;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public class DownloadableModule {
    @Expose
    private DefaultArtifactVersion coreVersion;
    @Expose
    private DefaultArtifactVersion installedVersion;

    private final String name;
    private final String[] authors;
    private final String description;

    private ModuleType type;

    public DownloadableModule(DefaultArtifactVersion coreVersion, DefaultArtifactVersion installedVersion, String name, String[] authors, String description, ModuleType type) {
        this.coreVersion = coreVersion;
        this.installedVersion = installedVersion;
        this.name = name;
        this.authors = authors;
        this.description = description;
        this.type = type;
    }

    public List<DownloadableModuleVersion> getVersions() {
        Collections.sort(versions);
        return versions;
    }

    private final List<DownloadableModuleVersion> versions = new ArrayList<>();

    public DownloadableModuleVersion getLatest() {
        Collections.sort(versions);
        for (DownloadableModuleVersion version : versions) {
                return version;
        }
        return null;
    }

    public DownloadableModuleVersion getLatestSupported() {
        Collections.sort(versions);
        for (DownloadableModuleVersion version : versions) {
            if (version.isSupported(coreVersion)) {
                return version;
            }
        }
        return null;
    }

    public boolean isUptoDate() {
        if (installedVersion == null) return false;
        int i = getLatestSupported().compareTo(new DownloadableModuleVersion(null, null, installedVersion, 0, null));
        return i >= 0;
    }

    public void addVersion(DownloadableModuleVersion version) {
        versions.add(version);
    }


    public void addVersion(String minCoreVersion, String maxCoreVersion, String version, long created, String downloadUrl) {
        addVersion(new DownloadableModuleVersion(minCoreVersion == null ? null :new DefaultArtifactVersion(minCoreVersion), maxCoreVersion == null ? null : new DefaultArtifactVersion(maxCoreVersion), new DefaultArtifactVersion(version), created, downloadUrl));
    }


    public DefaultArtifactVersion getCoreVersion() {
        return this.coreVersion;
    }

    public DefaultArtifactVersion getInstalledVersion() {
        return this.installedVersion;
    }

    public String getName() {
        return this.name;
    }

    public String[] getAuthors() {
        return this.authors;
    }

    public String getDescription() {
        return this.description;
    }

    public ModuleType getType() {
        return this.type;
    }

    public void setCoreVersion(DefaultArtifactVersion coreVersion) {
        this.coreVersion = coreVersion;
    }

    public void setInstalledVersion(DefaultArtifactVersion installedVersion) {
        this.installedVersion = installedVersion;
    }

    public void setType(ModuleType type) {
        this.type = type;
    }
}
