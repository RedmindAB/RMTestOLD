#!/bin/bash -x
export jar_home="$testHome/lib/"

java -cp $jar_home/RegisterNode.jar:$jar_home/selenium/selenium-server-standalone-$SeleniumVersion.jar se.aftonbladet.abtest.selenium.grid.RegisterNode $@
