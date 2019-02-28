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
        task.setDescription("Prints the project's configured version");
        task.doLast(t -> System.out.println(project.getVersion()));

        project.setVersion(new LazyVersion(project, settings));
    }

    private static class LazyVersion {
        private final Project project;
        private final Settings settings;
        private String version;

        private LazyVersion(Project project, Settings settings) {
            this.project = project;
            this.settings = settings;
        }

        private String generateVersion() {
            try {
                return VersionGenerator
                    .generateFromGit(settings, project.getRootProject().getProjectDir())
                    .orElse(settings.initialVersion);
            }
            catch (Exception e) {
                project.getLogger().warn("Error trying to determine project version", e);
                return settings.initialVersion;
            }
        }

        @Override
        public String toString() {
            if (version == null) {
                version = generateVersion();
            }
            return version;
        }
    }
}
