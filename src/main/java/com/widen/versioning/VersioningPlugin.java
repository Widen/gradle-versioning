package com.widen.versioning;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;

public class VersioningPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.setVersion(VersionGenerator.generateFromGit());

        Task task = project.getTasks().create("printVersion");
        task.setGroup("Versioning");
        task.setDescription("Prints the project\'s configured version");
        task.doLast(t -> System.out.println(project.getVersion()));
    }
}
