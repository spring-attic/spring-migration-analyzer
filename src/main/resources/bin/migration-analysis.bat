@ECHO OFF
IF "%OS%" == "Windows_NT" SETLOCAL

IF NOT "%JAVA_HOME%" == "" GOTO JavaHomeSet
ECHO The JAVA_HOME environment variable is not defined
GOTO:EOF

:JavaHomeSet
SET SCRIPT_DIR=%~dp0%

SET CLASSPATH=

PUSHD "%SCRIPT_DIR%"..\dist
FOR %%G IN (*.*) DO CALL:APPEND_TO_CLASSPATH dist %%G
POPD
PUSHD "%SCRIPT_DIR%"..\lib
FOR %%G IN (*.*) DO CALL:APPEND_TO_CLASSPATH lib %%G
POPD
SET CLASSPATH=%CLASSPATH%;%SCRIPT_DIR%..\config
GOTO Continue

: APPEND_TO_CLASSPATH
SET FILENAME=%~2
SET SUFFIX=%FILENAME:~-4%

IF %SUFFIX% EQU .jar (
    IF "%CLASSPATH%." == "." (
        SET CLASSPATH=%SCRIPT_DIR%..\%~1\%FILENAME%
    ) else (
        SET CLASSPATH=%CLASSPATH%;%SCRIPT_DIR%..\%~1\%FILENAME%
    )
)
GOTO:EOF

:Continue
"%JAVA_HOME%"\bin\java %JAVA_OPTS% -classpath "%CLASSPATH%" org.springframework.migrationanalyzer.commandline.MigrationAnalysis %*