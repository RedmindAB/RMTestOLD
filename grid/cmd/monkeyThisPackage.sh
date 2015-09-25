#!/bin/bash 

. $testHome/cmd/setConfig.sh

export APK_PATH=$1
export INSTALLFIRST=$2
if [ -z "$APK_PATH" ]; then
	echo "usage: ${0##*/} <arg1> (<arg2>) "
	echo "arg1: Full or relative path to the apk to installed should be supplied as an argument"
	echo "arg2: If set the APK will not be reinstalled. The value can be any string, like NODONTINSTALLTHISAGAIN"
	exit 1
fi
monkeyIterations="10000"
#monkeyArgs="--pct-touch 70 --pct-majornav 30 --throttle 1 --monitor-native-crashes "
monkeyArgs="--pct-touch 30 --pct-majornav 70 --randomize-throttle --monitor-native-crashes "
#monkeyAllowedIntents=" -c android.intent.action.MAIN -c se.aftonbladet.sportbladet.fotboll/.NotificationDialog -c se.aftonbladet.sportbladet.fotboll/.FavoriteLeaguesActivity -c se.aftonbladet.sportbladet.fotboll/.ErrorScreen"
echo "Apk to install: $APK_PATH"
echo "adb library used: $ADB_PATH"
export APK_PACKAGE=`$ADB_PATH/../build-tools/android-$AndroidBuildToolVersion/aapt dump badging $APK_PATH | grep package: | cut -d " " -f 2 | cut -d "'" -f 2`
export modelName=""
if [ -z "$INSTALLFIRST" ]; then
	$testHome/cmd/installApk.sh $APK_PATH
fi
adb devices | grep "	device" | cut -d "	" -f1 | while read currDevId
do
	modelName=`$ADB_PATH/adb -s $currDevId shell getprop ro.product.model | tr -d "\r"`
#	$ADB_PATH/adb -s $currDevId shell am force-stop $APK_PACKAGE
	echo "running monkeytests on package: $APK_PACKAGE at $modelName with id: $currDevId"
	echo "Results are stored in: /tmp/mkey_$currDevId.log"
#	$ADB_PATH/adb -s $currDevId shell "input keyevent 26"
	$ADB_PATH/adb -s $currDevId shell "monkey -p $APK_PACKAGE $monkeyArgs $monkeyAllowedIntents $monkeyIterations" > /tmp/mkey_$currDevId.log &
done









