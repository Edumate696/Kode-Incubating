@echo off
@setlocal
set "KODE_HOME=%~dp0.."
"%JAVA_HOME%/bin/java" -Dkode.home="%KODE_HOME%" %KODE_DEBUG_FLAG% -cp "%KODE_HOME%/include/*;%KODE_HOME%/boot/" ${mainClass}