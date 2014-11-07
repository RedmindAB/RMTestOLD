#!/bin/bash

export testHome=`grep TESTHOME ~/.RmTest | cut -d "=" -f2`

getConfigParam() {
	$testHome/cmd/json.sh -l < $testHome/etc/LocalConfig.json | grep "\"configuration\",\"$1\"" | cut -d"]" -f2 | tr -d '\t' | sed 's/"//g'
}

export androidHome=`getConfigParam androidHome`
export ANDROIDHOME=$androidHome
export ANDROID_HOME=$androidHome
export ADB_PATH="$ANDROIDHOME/platform-tools"
export PATH=$PATH:$ANDROID_HOME/platform-tools
export RmJar="$testHome/target/RMTest-SNAPSHOT.jar"
export RMTestHubIp=`getConfigParam hubIp`
export RMTestLocalNodeIp=`getConfigParam localIp`
#export AndroidBuildToolVersion=`getConfigParam AndroidBuildtoolsVersion`
export AAPTCmd=`find $androidHome -name aapt | head -1`
export seleniumVersion=`getConfigParam seleniumVersion`
export PRETTY_TIMESTAMP="date +%Y%m%d_%H%M%S"


alias ws="cd $testHome"
alias cmd="cd $testHome/cmd/"
alias adt="cd $ANDROID_HOME"

getLogPrefix() {
	echo "$testHome/log/$0-`$PRETTY_TIMESTAMP`"
}


PATH=$PATH:$testHome/lib



