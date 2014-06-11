#!/bin/bash

. $testHome/cmd/setConfig.sh

export jar_home="$testHome/lib/"
echo "java -cp $jar_home/GridQueryServlet.jar:$jar_home/selenium/selenium-server-standalone-$seleniumVersion.jar org.openqa.grid.selenium.GridLauncher -role hub -servlets se.aftonbladet.abtest.selenium.grid.GridQueryServlet -debug" 

java -cp $jar_home/GridQueryServlet.jar:$jar_home/selenium/selenium-server-standalone-$seleniumVersion.jar org.openqa.grid.selenium.GridLauncher -role hub -servlets se.aftonbladet.abtest.selenium.grid.GridQueryServlet -debug 
