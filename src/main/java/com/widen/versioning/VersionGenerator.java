package com.widen.versioning;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Optional;

public class VersionGenerator {
    public static Optional<String> generateFromGit(Settings settings, File projectDir) {
        return gitDescribe(settings, projectDir)
            .flatMap(describe -> generateFromString(describe, settings));
    }

    public static Optional<String> generateFromString(String describe, Settings settings) {
        if (describe == null) {
            return Optional.empty();
        }

        String version = describe.trim().replaceFirst("-(\\d+-g.)", "+$1");

        if (settings.tagPrefix != null && version.startsWith(settings.tagPrefix)) {
            version = version.substring(settings.tagPrefix.length());
        }

        return Optional.of(version);
    }

    public static Optional<String> gitDescribe(Settings settings, File projectDir) {
        ArrayList<String> args = new ArrayList<>();
        args.add("git");
        args.add("describe");
        args.add("--tags");
        args.add("--abbrev=7");

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

        if (settings.useCommitHashDefault) {
            args.add("--always");
        }

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(args)
                .redirectInput(ProcessBuilder.Redirect.INHERIT)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .directory(projectDir);
            processBuilder.environment().put("GIT_DIR", projectDir.getPath());

            Process process = processBuilder.start();
            String output = IOUtils.toString(process.getInputStream(), Charset.forName("UTF-8"));

            process.waitFor();
            if (process.exitValue() != 0) {
                throw new RuntimeException("Git returned status code " + process.exitValue() + ": " + output);
            }

            return Optional.of(output).filter(s -> !s.isEmpty());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
