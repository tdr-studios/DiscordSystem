package de.tdrstudios.discordsystem.version.core;

import com.google.gson.annotations.Expose;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public class DownloadableCore {
    @Expose
    private final DefaultArtifactVersion currentVersion;
    private List<DownloadableCoreVersion> versions = new ArrayList<>();

    public DownloadableCore(DefaultArtifactVersion currentVersion) {
        this.currentVersion = currentVersion;
    }

    public DownloadableCore(DefaultArtifactVersion currentVersion, List<DownloadableCoreVersion> versions) {
        this.currentVersion = currentVersion;
        this.versions = versions;
    }

    public DownloadableCoreVersion getLatest() {
        Collections.sort(versions);
        for (DownloadableCoreVersion version : versions) {
                return version;
        }
        return null;
    }

    public boolean isUptoDate() {
        DownloadableCoreVersion version = new DownloadableCoreVersion(currentVersion, 0, "");
        int i = getLatest().compareTo(version);
        return i >= 0;
    }

    public void addVersion(DownloadableCoreVersion version) {
        versions.add(version);
    }


    public void addVersion(String minCoreVersion, String maxCoreVersion, String version, long created, String downloadUrl) {
        addVersion(new DownloadableCoreVersion(new DefaultArtifactVersion(version), created, downloadUrl));
    }


    public DefaultArtifactVersion getCurrentVersion() {
        return this.currentVersion;
    }

    public List<DownloadableCoreVersion> getVersions() {
        return this.versions;
    }
}
