#!/bin/bash -x

. $testHome/cmd/setConfig.sh

export jar_home="$testHome/lib/"

java -cp $jar_home/RegisterNode.jar:$jar_home/selenium/selenium-server-standalone-$seleniumVersion.jar se.aftonbladet.abtest.selenium.grid.RegisterNode $@
