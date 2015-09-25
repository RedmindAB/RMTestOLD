#!/bin/bash -x

. $testHome/cmd/setConfig.sh
logName=`getLogPrefix`
$testHome/cmd/killHub.sh


java -cp $RmJar:$SeleniumJar org.openqa.grid.selenium.GridLauncher -role hub -servlets se.redmind.rmtest.selenium.grid.servlets.GridQueryServlet &> $logName.log &
exit 0;
