@ECHO off 

java -cp ..\lib\RMTest-SNAPSHOT.jar;selenium-server-standalone-2.45.0.jar org.openqa.grid.selenium.GridLauncher -role hub -servlets se.aftonbladet.abtest.selenium.grid.GridQueryServlet -debug 
