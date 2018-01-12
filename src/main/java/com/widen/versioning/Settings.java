package com.widen.versioning;

public class Settings {
    /**
     * An initial version number to use if no tags are available.
     */
    public String initialVersion = "0.0.0";

    /**
     * A prefix that tags must start with in order to be considered a version tag.
     */
    public String tagPrefix;

    /**
     * A regular expression for tags that should be ignored.
     */
    public String excludeTags;

    /**
     * Whether a `-dirty` suffix should be added to the version string if the Git working directory is dirty.
     */
    public boolean dirtyMark = true;
}
