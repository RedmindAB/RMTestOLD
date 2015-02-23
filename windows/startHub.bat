@ECHO off 

java -cp ..\lib\GridQueryServlet.jar;selenium-server-standalone-2.43.1.jar org.openqa.grid.selenium.GridLauncher -role hub -servlets se.aftonbladet.abtest.selenium.grid.GridQueryServlet -debug 
