@ECHO off 

java -cp ..\lib\RMTest-SNAPSHOT.jar;selenium-server-standalone-2.47.1.jar org.openqa.grid.selenium.GridLauncher -role hub -servlets se.redmind.rmtest.selenium.grid.servlets.GridQueryServlet -debug 
