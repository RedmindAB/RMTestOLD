@ECHO OFF
SET RootDir="C:\Repositories\RMTest\RMTest\dotNet\RMTest"
SET outputDir="%RootDir%\TestResults"
SET TestDir="%RootDir%\RMTest.Tests\bin\Debug"

cd \Program\nunit.org\bin\

nunit-console.exe %TestDir%\RMTest.Tests.dll --domain=Multiple --process=Multiple --workers=4 --work=%outputDir% --debug

pause