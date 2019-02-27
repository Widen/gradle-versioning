#!/bin/sh
set -eu

GRADLE_PUBLISH_KEY=$(aws ssm get-parameter --name '/buildkite/gradle-publish-key' --with-decryption | jq -r '.Parameter.Value')
GRADLE_PUBLISH_SECRET=$(aws ssm get-parameter --name '/buildkite/gradle-publish-secret' --with-decryption | jq -r '.Parameter.Value')

echo "gradle.publish.key=$GRADLE_PUBLISH_KEY" >> ~/.gradle/gradle.properties
echo "gradle.publish.secret=$GRADLE_PUBLISH_SECRET" >> ~/.gradle/gradle.properties

./gradlew publishPlugins