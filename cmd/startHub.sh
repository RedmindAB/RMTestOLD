#!/bin/bash -x

. $testHome/cmd/setConfig.sh

#export jar_home="$testHome/target/lib/selenium-server-2.40.0.jar:$testHome/target/RMTest-SNAPSHOT.jar"
#echo "java -cp $CLASSPATH org.openqa.grid.selenium.GridLauncher -role hub -servlets se.aftonbladet.abtest.selenium.grid.GridQueryServlet -debug" 

java -cp $RmJar org.openqa.grid.selenium.GridLauncher -role hub -servlets se.redmind.rmtest.selenium.grid.servlets.GridQueryServlet -debug 
