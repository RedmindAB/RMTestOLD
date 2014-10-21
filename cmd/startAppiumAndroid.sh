#!/bin/bash 

. $testHome/cmd/setConfig.sh

./killAppiums.sh
export APK_PATH=$1
if [ -z "$APK_PATH" ]; then
	echo "No APK supplied as argument, starting Chrome"
fi

export jar_home="$testHome/lib/selenium/"
export androidNodeFile="/tmp/androidNodes.cfg"

export basePort=8080
export bootstrapPort=10080
export chromeDriverPort=11080
export selendroidPort=12080
export modelName=""
export androidVersion=""
export isInstalled=""
rm -f $androidNodeFile
export APK_PACKAGE=`$ADB_PATH/../build-tools/android-$AndroidBuildToolVersion/aapt dump badging $APK_PATH | grep package: | cut -d " " -f 2 | cut -d "'" -f 2`


adb devices | grep "	device" | cut -d "	" -f1 | while read currDevId
do
	modelName=`adb -s $currDevId shell getprop ro.product.model | tr -d "\r"`	
	modelBrand=`adb -s $currDevId shell getprop ro.product.brand | tr -d "\r"`	
	androidVersion=`adb -s $currDevId shell getprop ro.build.version.release | tr -d "\r"`
	description="$modelBrand $modelName  $androidVersion"	
	echo "####### $modelName ########"
	basePort=$[$basePort+1]
	bootstrapPort=$[$bootstrapPort+1]
	chromeDriverPort=$[$chromeDriverPort+1]
	selendroidPort=$[$selendroidPort+1]
	cp -f $testHome/etc/Appium_TEMPLATE.json	$testHome/etc/Appium_TEMP.json
	
	sed -i '' "s/PLATFORM/Android/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/OS_NAME/ANDROID/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/DEVICE_ID/$currDevId/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/DESCR_STRING/$description/g" $testHome/etc/Appium_TEMP.json
	if [ -z $APK_PATH ]
		then
		sed -i '' "/APP_PATH/d" $testHome/etc/Appium_TEMP.json
		sed -i '' '/app-package/d' $testHome/etc/Appium_TEMP.json	
		sed -i '' '/app-activity/d' $testHome/etc/Appium_TEMP.json
		sed -i '' "s:BROWSER_NAME:Chrome:g" $testHome/etc/Appium_TEMP.json
	else	
		sed -i '' "s:APP_PKG:$APK_PACKAGE:g" $testHome/etc/Appium_TEMP.json
		sed -i '' '/BROWSER_NAME/d' $testHome/etc/Appium_TEMP.json
	fi
	sed -i '' "s/DEVICE_NAME/$modelName/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/DEVICE_VERSION/$androidVersion/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/MAX_SESSIONS/1/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/APPIUM_PORT/$basePort/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/APPIUM_HOST/$RMTestLocalNodeIp/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/HUB_PORT/4444/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/HUB_HOST/$RMTestHubIp/g" $testHome/etc/Appium_TEMP.json
	cat $testHome/etc/Appium_TEMP.json
	logfile="$testHome/log/appium_android_$currDevId.log"	
	$testHome/appium/bin/appium.js -U $currDevId -a $RMTestLocalNodeIp -p $basePort -bp $bootstrapPort --chromedriver-port $chromeDriverPort --nodeconfig $testHome/etc/Appium_TEMP.json &> $logfile &
	appiumStarted=true	
	while $appiumStarted
		do
		connectedCount=`grep -c "Appium successfully registered with the grid on $RMTestHubIp:4444" $logfile`
		if [ $connectedCount -gt 0  ]
		then
			echo "Connected to HUB"
			appiumStarted=false
		fi
		echo "Not yet connected to HUB"
		sleep 1
	done
done

