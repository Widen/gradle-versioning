package com.widen.versioning;

import org.apache.commons.io.IOUtils;

import java.nio.charset.Charset;
import java.util.ArrayList;

public class VersionGenerator {
    public static String generateFromGit(Settings settings) {
        ArrayList<String> args = new ArrayList<>();
        args.add("git");
        args.add("describe");
        args.add("--tags");

        if (settings.tagPrefix != null) {
            args.add("--match");
            args.add(settings.tagPrefix + '*');
        }

        if (settings.excludeTags != null) {
            args.add("--exclude");
            args.add(settings.excludeTags);
        }

        if (settings.dirtyMark) {
            args.add("--dirty");
        }

        try {
            Process process = Runtime.getRuntime().exec(args.toArray(new String[0]));
            String output = IOUtils.toString(process.getInputStream(), Charset.forName("UTF-8"));

            process.waitFor();
            if (process.exitValue() != 0 || output.isEmpty()) {
                return null;
            }

            return generateFromString(output.trim(), settings);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateFromString(String describe, Settings settings) {
        if (describe == null) {
            return null;
        }

        String version = describe.replaceFirst("-(\\d+-g.)", "+$1");

        if (settings.tagPrefix != null && version.startsWith(settings.tagPrefix)) {
            version = version.substring(settings.tagPrefix.length());
        }

        return version;
    }
}
