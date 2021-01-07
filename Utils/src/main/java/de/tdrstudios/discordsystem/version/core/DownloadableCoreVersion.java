package de.tdrstudios.discordsystem.version.core;

import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
public class DownloadableCoreVersion implements Comparable<DownloadableCoreVersion> {
    private DefaultArtifactVersion version;
    private long created;
    private String url;

    public DownloadableCoreVersion(DefaultArtifactVersion version, long created, String url) {
        this.version = version;
        this.created = created;
        this.url = url;
    }

    public DownloadableCoreVersion() {
    }

    @Override
    public int compareTo(DownloadableCoreVersion o) {
        return o.getVersion().compareTo(version);
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
