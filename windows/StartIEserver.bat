@ECHO off
java -Dwebdriver.iexplorer.driver=C:\ws\IEDriverServer.exe -jar selenium-server-standalone-2.35.0.jar -role webdriver -port 5558 -browser browserName=iexplorer