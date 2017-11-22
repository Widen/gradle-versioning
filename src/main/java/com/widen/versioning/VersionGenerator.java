package com.widen.versioning;

import org.apache.commons.io.IOUtils;

import java.nio.charset.Charset;

public class VersionGenerator {
    public static String generateFromGit() {
        try {
            Process process = Runtime.getRuntime().exec("git describe --tags");
            String output = IOUtils.toString(process.getInputStream(), Charset.forName("UTF-8"));

            process.waitFor();
            if (process.exitValue() != 0 || output.isEmpty()) {
                throw new RuntimeException("Could not determine version from Git");
            }

            return generateFromString(output.trim());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateFromString(String describe) {
        if (describe == null) {
            return "";
        }
        return describe.replaceFirst("-", "+");
    }
}
