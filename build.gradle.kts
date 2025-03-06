plugins {
  `kotlin-dsl`
  `java-gradle-plugin`
  groovy
  id("com.gradle.plugin-publish") version "1.3.1"
  id("com.widen.versioning") version "0.4.0"
}

group = "com.widen.oss"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(11)
  }
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

repositories {
  mavenCentral()
}

dependencies {
  testImplementation("org.spockframework:spock-core:2.3-groovy-3.0")
}

tasks.test {
  useJUnitPlatform()
  testLogging {
    events("passed", "skipped", "failed")
    exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    showExceptions = true
    showCauses = true
    showStackTraces = true
  }
}

gradlePlugin {
  plugins {
    website.set("https://github.com/Widen/gradle-versioning")
    vcsUrl.set("https://github.com/Widen/gradle-versioning")
    plugins {
      create("versioningPlugin") {
        id = "com.widen.versioning"
        implementationClass = "com.widen.versioning.VersioningPlugin"
        displayName = "Widen Gradle versioning plugin"
        description = "A Gradle plugin for applying project version from Git tags"
        tags = listOf("versioning", "git")
      }
    }
  }
}
