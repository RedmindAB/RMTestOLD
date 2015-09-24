#!/bin/bash -x

. $testHome/cmd/setConfig.sh

export APK_PATH=$1
if [ -z "$APK_PATH" ]; then
	echo "usage: Full or relative path to the apk to installed should be supplied as an argument"
	exit 1
fi
echo "Apk to install: $APK_PATH"
echo "$ANDROID_HOME/platform-tools/adb library used: $ADB_PATH"
export APK_PACKAGE=`$AAPTCmd dump badging "$APK_PATH" | grep package: | cut -d " " -f 2 | cut -d "'" -f 2`
export modelName=""

$ANDROID_HOME/platform-tools/adb devices | grep "	device" | cut -d "	" -f1 | while read currDevId
do
	modelName=`$ANDROID_HOME/platform-tools/adb -s $currDevId shell getprop ro.product.model | tr -d "\r"`

	echo "Trying to (re)install package: $APK_PACKAGE at $modelName with id: $currDevId"
	echo "its ok with a failed uninstall due to no prevoius installation"
	echo "Uninstalling package....."
	$ANDROID_HOME/platform-tools/adb -s $currDevId uninstall $APK_PACKAGE 2>&1
	echo "Installing package....."
	$ANDROID_HOME/platform-tools/adb -s $currDevId install "$APK_PATH"
done









