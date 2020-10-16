package de.tdrstudios.discordsystem.version.module;

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
public class DownloadableModuleVersion implements Comparable<DownloadableModuleVersion>{
    private DefaultArtifactVersion minCoreVersion, maxCoreVersion, version;
    private long created;
    private String url;

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
}
