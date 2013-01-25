# Spring Migration Analyzer

Spring Migration Anaylzer is a command-line tool for analyzing Java EE applications. It produces a report describing the application and how to migrate it to Spring.

# Downloads
- [Releases](http://www.springsource.org/download/community?project=Spring%20Migration%20Analyzer)
- [Snapshots](http://repo.springsource.org/simple/libs-snapshot-local/org/springframework/migrationanalyzer/spring-migration-analyzer/1.0.0.BUILD-SNAPSHOT/)

# Usage

To begin using the Migration Analyzer, perform the following for usage information:

	unzip spring-migration-analyzer-<version>-dist.zip
	cd spring-migration-analyzer-<version>/bin/
	./migration-analysis.sh

# Example usage

	./migration-analysis.sh ~/dev/resources/migration-apps/my-app.ear

This will analyze `my-app.ear`, producing a report in the current working directory. The report's directory will be named `migration-analysis.my-app.ear`.

# Building from source

Spring Migration Anaylzer is built using Gradle. To build:

	./gradlew clean build dist

Once the build's completed, a .zip package will be available in `build/distributions`.

# Infrastructure

- Issues - [https://jira.springsource.org/browse/SMA](https://jira.springsource.org/browse/SMA)
- CI - [https://build.springsource.org/browse/SMA](https://build.springsource.org/browse/SMA)
- Quality - [https://sonar.springsource.org/dashboard/index/2770](https://sonar.springsource.org/dashboard/index/2770)