#!/bin/bash -x
. ../etc/RMTest.conf
./killAppiums.sh
export APK_PATH=$1
if [ -z "$APK_PATH" ]; then
	echo "usage: Full or relative path to the apk to installed should be supplied as an argument"
	exit 1
fi

export jar_home="$testHome/lib/selenium/"
export androidNodeFile="/tmp/androidNodes.cfg"

export basePort=8080
export modelName=""
export androidVersion=""
export isInstalled=""
rm -f $androidNodeFile

adb devices | grep "	device" | cut -d "	" -f1 | while read currDevId
do
	modelName=`adb -s $currDevId shell getprop ro.product.model | tr -d "\r"`	
	androidVersion=`adb -s $currDevId shell getprop ro.build.version.release | tr -d "\r"`	
	echo "####### $modelName ########"
	basePort=$[$basePort+1]
	cp -f $testHome/etc/Appium_TEMPLATE.json	$testHome/etc/Appium_TEMP.json
	
	sed -i '' "s/PLATFORM/MAC/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s:APP_PATH:$APK_PATH:g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/DEVICE_NAME/$modelName/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/DEVICE_VERSION/$androidVersion/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/MAX_SESSIONS/1/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/APPIUM_PORT/$basePort/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/APPIUM_HOST/$RMTestLocalNodeIp/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/HUB_PORT/4444/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/HUB_HOST/$RMTestHubIp/g" $testHome/etc/Appium_TEMP.json
	cat $testHome/etc/Appium_TEMP.json	
	appium -U $currDevId -a $RMTestLocalNodeIp -p $basePort --full-reset --nodeconfig $testHome/etc/Appium_TEMP.json &
#	isInstalled=`adb -s $currDevId shell pm list packages  org.openqa.selenium.android.app`

done

