package de.tdrstudios.discordsystem.version.module;

import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public class DownloadableModuleVersion implements Comparable<DownloadableModuleVersion>{
    private DefaultArtifactVersion minCoreVersion, maxCoreVersion, version;
    private long created;
    private String url;

    public DownloadableModuleVersion(DefaultArtifactVersion minCoreVersion, DefaultArtifactVersion maxCoreVersion, DefaultArtifactVersion version, long created, String url) {
        this.minCoreVersion = minCoreVersion;
        this.maxCoreVersion = maxCoreVersion;
        this.version = version;
        this.created = created;
        this.url = url;
    }

    public DownloadableModuleVersion() {
    }

    public boolean isSupported(DefaultArtifactVersion coreVersion) {
        if (minCoreVersion == null && maxCoreVersion == null) {
            return true;
        }else if (maxCoreVersion == null) {
            return !(coreVersion.compareTo(minCoreVersion) < 0);
        }else if (minCoreVersion == null) {
            return !(coreVersion.compareTo(maxCoreVersion) > 0);
        }
        return !(coreVersion.compareTo(minCoreVersion) < 0 || coreVersion.compareTo(maxCoreVersion) > 0);
    }

    @Override
    public int compareTo(DownloadableModuleVersion o) {
        return o.getVersion().compareTo(version);
    }

    @Override
    public String toString() {
        return version.toString();
    }

    public DefaultArtifactVersion getMinCoreVersion() {
        return this.minCoreVersion;
    }

    public DefaultArtifactVersion getMaxCoreVersion() {
        return this.maxCoreVersion;
    }

    public DefaultArtifactVersion getVersion() {
        return this.version;
    }

    public long getCreated() {
        return this.created;
    }

    public String getUrl() {
        return this.url;
    }

    public void setMinCoreVersion(DefaultArtifactVersion minCoreVersion) {
        this.minCoreVersion = minCoreVersion;
    }

    public void setMaxCoreVersion(DefaultArtifactVersion maxCoreVersion) {
        this.maxCoreVersion = maxCoreVersion;
    }

    public void setVersion(DefaultArtifactVersion version) {
        this.version = version;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
