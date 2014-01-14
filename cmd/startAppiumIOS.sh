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
	description="$modelName  $androidVersion"	
	echo "####### $modelName ########"
	basePort=$[$basePort+1]
	cp -f $testHome/etc/Appium_TEMPLATE.json	$testHome/etc/Appium_TEMP.json
	
	sed -i '' "s/PLATFORM/MAC/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/DESCR_STRING/$description/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s:APP_PATH:$APK_PATH:g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/DEVICE_NAME/$modelName/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/DEVICE_VERSION/$androidVersion/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/MAX_SESSIONS/1/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/APPIUM_PORT/$basePort/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/APPIUM_HOST/$RMTestLocalNodeIp/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/HUB_PORT/4444/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/HUB_HOST/$RMTestHubIp/g" $testHome/etc/Appium_TEMP.json
	cat $testHome/etc/Appium_TEMP.json	
	appium -U $currDevId -a $RMTestLocalNodeIp -p $basePort --session-override --nodeconfig $testHome/etc/Appium_TEMP.json &
/Users/petost/workspaces/ADTWS/appium/bin/appium.js -U c173e0ecc22613a5c674e80626119f4f1ab2b5b1 -a localhost -p 8180 --nodeconfig ../etc/AppiumIos_TEMPLATE.json
#	isInstalled=`adb -s $currDevId shell pm list packages  org.openqa.selenium.android.app`

done

