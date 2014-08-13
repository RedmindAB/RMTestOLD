#!/bin/bash -x

. $testHome/cmd/setConfig.sh

export jar_home="$testHome/lib/"

java -cp $RmJar:$jar_home/selenium/selenium-server-standalone-$seleniumVersion.jar se.redmind.rmtest.selenium.grid.RegisterNode $@
