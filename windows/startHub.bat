@ECHO off 

java -cp ..\lib\RMTest-SNAPSHOT.jar;../lib/selenium-server-standalone-2.46.0.jar org.openqa.grid.selenium.GridLauncher -role hub -servlets se.aftonbladet.abtest.selenium.grid.GridQueryServlet -debug 
