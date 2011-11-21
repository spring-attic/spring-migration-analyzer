#!/bin/bash

if [ -z "$JAVA_HOME" ]
then
    echo The JAVA_HOME environment variable is not defined
    exit 1
fi

SCRIPT="$0"

# SCRIPT may be an arbitrarily deep series of symlinks. Loop until we have the concrete path.
while [ -h "$SCRIPT" ] ; do
  ls=`ls -ld "$SCRIPT"`
  # Drop everything prior to ->
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    SCRIPT="$link"
  else
    SCRIPT=`dirname "$SCRIPT"`/"$link"
  fi
done

DIST_DIR=`dirname "$SCRIPT"`/../dist
LIB_DIR=`dirname "$SCRIPT"`/../lib
CONFIG_DIR=`dirname "$SCRIPT"`/../config

CLASSPATH=""

for file in $DIST_DIR/*
do
	if [[ $file == *.jar ]]
	then
	    if [ $CLASSPATH ]
		then	
	        CLASSPATH=$CLASSPATH:$file
	    else
	        CLASSPATH=$file
	    fi
	fi
done

for file in $LIB_DIR/*
do
	if [[ $file == *.jar ]]
	then
	    if [ $CLASSPATH ]
		then	
	        CLASSPATH=$CLASSPATH:$file
	    else
	        CLASSPATH=$file
	    fi
	fi
done

CLASSPATH=$CLASSPATH:$CONFIG_DIR

$JAVA_HOME/bin/java $JAVA_OPTS -classpath $CLASSPATH org.springframework.migrationanalyzer.commandline.MigrationAnalysis $*