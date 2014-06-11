#!/bin/bash

export testHome=`grep TESTHOME ~/.RmTest | cut -d "=" -f2`

getConfigParam() {
	$testHome/cmd/json.sh -l < $testHome/etc/LocalConfig.json | grep "\"configuration\",\"$1\"" | cut -d"]" -f2 | tr -d '\t' | sed 's/"//g'
}

export androidHome=`getConfigParam androidHome`
export ANDROIDHOME=$androidHome
export PATH=$PATH:$ANDROID_HOME/platform-tools/
export RMTestHubIp=`getConfigParam hubIp`
export RMTestLocalNodeIp=`getConfigParam localIp`
export AndroidBuildToolVersion=`getConfigParam AndroidBuildtoolsVersion`
export seleniumVersion=`getConfigParam seleniumVersion`





