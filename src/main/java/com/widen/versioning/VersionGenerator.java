package com.widen.versioning;

import java.io.File;
import java.nio.charset.StandardCharsets;
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
                .redirectErrorStream(true)
                .directory(projectDir);

            Process process = processBuilder.start();
            process.waitFor();

            String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            if (process.exitValue() != 0) {
                String message = output.replace("\n", " ");
                throw new GitException("Git returned status code " + process.exitValue() + ": " + message);
            }

            return Optional.of(output).filter(s -> !s.isEmpty());
        }
        catch (GitException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
