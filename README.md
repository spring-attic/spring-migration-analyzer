# Spring Migration Analyzer

Spring Migration Anaylzer is a command-line tool for analyzing JEE applications. It produces a report describing the application and how to migrate it to Spring.

# Downloads

- [Snapshots](https://s3browse.springsource.com/browse/maven.springframework.org/snapshot/org/springframework/migrationanalyzer/spring-migration-analyzer/1.0.0.BUILD-SNAPSHOT/)

# Building from source

Spring Migration Anaylzer is built using Maven. To build:

	mvn clean package

Once the build's completed, a .zip package will be available in `packaging/target`.

# Usage

Having built or downloaded the Migration Anaylzer, perform the following for usage information:

	unzip spring-migration-analyzer-1.0.0.BUILD-SNAPSHOT.zip
	cd spring-migration-analyzer-1.0.0.BUILD-SNAPSHOT/bin/
	./migration-analysis.sh

# Example usage

	./migration-analysis.sh -i ~/dev/resources/migration-apps/my-app.ear

This will analyze `my-app.ear`, producing a report in the current working directory. The report's directory will be named `my-app.ear`.

# Infrastructure

- Issues - [https://jira.springsource.org/browse/SMA](https://jira.springsource.org/browse/SMA)
- CI - [https://build.springsource.org/browse/SMA](https://build.springsource.org/browse/SMA)
- Quality - [https://sonar.springsource.org/dashboard/index/2770](https://sonar.springsource.org/dashboard/index/2770)