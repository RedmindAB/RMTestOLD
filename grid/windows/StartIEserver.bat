@ECHO off
java -Dwebdriver.iexplorer.driver=C:\Users\petter\RMTest\RMTest\grid\windows\IEDriverServer.exe -jar selenium-server-standalone-2.47.1.jar -role webdriver -port 5558 -browser browserName="internet explorer"
