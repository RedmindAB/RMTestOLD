@ECHO off
java -jar ../lib/selenium-server-standalone-2.47.1.jar -role node -nodeConfig Win.json -Dwebdriver.chrome.driver=C:\Users\xtompe\.RmTest\lib\chromedriver\chromedriver.exe -Dwebdriver.ie.driver=C:\Users\xtompe\.RmTest\lib\IEDriver\IEDriverServer.exe
pause
