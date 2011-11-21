# Spring Migration Analyzer

Spring Migration Anaylzer is a command-line tool for analyzing JEE applications. It produces a report describing the application and how to migrate it to Spring.

# Building

Spring Migration Anaylzer is built using Maven. To build:

	mvn clean package

# Usage

Having built the Migration Anaylzer, perform the following for usage information:

	cd packaging/target
	unzip spring-migration-analyzer-1.0.0.BUILD-SNAPSHOT.zip
	cd spring-migration-analyzer-1.0.0.BUILD-SNAPSHOT/bin/
	./migration-analysis.sh

# Example usage

	./migration-analysis.sh -i ~/dev/resources/migration-apps/my-app.ear -o my-app -t html

Currently `html` is the only supported report type, i.e. `-t` must be specified with a value of `html`.
