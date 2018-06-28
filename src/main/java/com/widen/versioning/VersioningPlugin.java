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
        project.afterEvaluate(p -> applyVersion(project, settings));
    }

    private void applyVersion(final Project project, final Settings settings) {
        String version = null;
        try {
            version = VersionGenerator.generateFromGit(settings, project.getRootProject().getProjectDir());
        }
        catch (Exception e) {
            project.getLogger().warn("Error trying to determine project version", e);
        }

        if (version != null) {
            project.setVersion(version);
        }
        else if ("unspecified".equals(project.getVersion())) {
            project.getLogger().debug("No version specified, using initial version");
            project.setVersion(settings.initialVersion);
        }
    }
}
