#!/bin/bash -x
#. ../etc/RMTest.conf
./killAppiums.sh
export APK_PATH=$1
if [ -z "$APK_PATH" ]; then
	echo "usage: Full or relative path to the apk to installed should be supplied as an argument"
	exit 1
fi

export jar_home="$testHome/lib/selenium/"

export basePort=8180
export modelName=""
export androidVersion=""
export isInstalled=""
rm -f $androidNodeFile
export idevicePath="$testHome/lib/libimobiledevice-macosx"

$idevicePath/idevice_id -l | while read currDevId
do
	modelName=`$idevicePath/ideviceinfo -u $currDevId | grep DeviceName`
	iosVersion=`$idevicePath/ideviceinfo -u $currDevId | grep ProductVersion`
	description="$modelName  $iosVersion"	
	echo "####### $modelName ########"
	basePort=$[$basePort+1]
	cp -f $testHome/etc/Appium_TEMPLATE.json	$testHome/etc/Appium_TEMP.json
	
	sed -i '' "s/PLATFORM/MAC/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/OS_NAME/IOS/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/DESCR_STRING/$description/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s:APP_PATH:$APK_PATH:g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/DEVICE_NAME/$modelName/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/DEVICE_VERSION/$iosVersion/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/MAX_SESSIONS/1/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/APPIUM_PORT/$basePort/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/APPIUM_HOST/$RMTestLocalNodeIp/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/HUB_PORT/4444/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/HUB_HOST/$RMTestHubIp/g" $testHome/etc/Appium_TEMP.json
	cat $testHome/etc/Appium_TEMP.json	
#	appium -U $currDevId -a $RMTestLocalNodeIp -p $basePort --session-override --nodeconfig $testHome/etc/Appium_TEMP.json &
$testHome/appium/bin/appium.js -U $currDevId -a $RMTestLocalNodeIp -p $basePort --nodeconfig ../etc/Appium_TEMP.json 
#	isInstalled=`adb -s $currDevId shell pm list packages  org.openqa.selenium.android.app`

done
