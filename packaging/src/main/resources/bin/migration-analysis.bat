@ECHO OFF
IF "%OS%" == "Windows_NT" SETLOCAL

IF NOT "%JAVA_HOME%" == "" goto JavaHomeSet
echo The JAVA_HOME environment variable is not defined
GOTO:EOF

:JavaHomeSet
SET SCRIPT_DIR=%~dp0%

set CLASSPATH=

PUSHD "%SCRIPT_DIR%"..\dist
FOR %%G IN (*.*) DO CALL:APPEND_TO_CLASSPATH dist %%G
POPD
PUSHD "%SCRIPT_DIR%"..\lib
FOR %%G IN (*.*) DO CALL:APPEND_TO_CLASSPATH lib %%G
POPD
set CLASSPATH=%CLASSPATH%;%SCRIPT_DIR%..\config
GOTO Continue

: APPEND_TO_CLASSPATH
set filename=%~2
set suffix=%filename:~-4%
if %suffix% equ .jar set CLASSPATH=%CLASSPATH%;%SCRIPT_DIR%..\%~1\%filename%
GOTO:EOF

:Continue
"%JAVA_HOME%"\bin\java %JAVA_OPTS% -classpath "%CLASSPATH%" org.springframework.migrationanalyzer.commandline.MigrationAnalysis %*
