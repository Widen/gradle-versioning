package com.widen.versioning;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;

public class VersioningPlugin implements Plugin<Project> {
    @Override
    public void apply(final Project project) {
        // Make settings available to build script.
        final Settings settings = project.getExtensions().create("versioning", Settings.class);

        Task task = project.getTasks().create("printVersion");
        task.setGroup("Versioning");
        task.setDescription("Prints the project\'s configured version");
        task.doLast(t -> System.out.println(project.getVersion()));

        // Defer setting the version until after the build script is evaluated.
        project.afterEvaluate(p -> {
            String version = VersionGenerator.generateFromGit(settings, p.getRootProject().getProjectDir());
            if (version != null) {
                p.setVersion(version);
            }
            else if ("unspecified".equals(p.getVersion())) {
                p.setVersion(settings.initialVersion);
            }
        });
    }
}
