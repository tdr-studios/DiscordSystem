package de.tdrstudios.discordsystem.version.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

/**
 * @author DSeeLP
 * @since 0.1-ALPHA
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DownloadableCoreVersion implements Comparable<DownloadableCoreVersion> {
    private DefaultArtifactVersion version;
    private long created;
    private String url;

    @Override
    public int compareTo(DownloadableCoreVersion o) {
        return o.getVersion().compareTo(version);
    }
}
