#!/bin/bash -x

. $testHome/cmd/setConfig.sh
logName=`getLogPrefix`
. $testHome/cmd/killAppiums.sh
# TODO: Add posibility to tests app on simulator

export jar_home="$testHome/lib/selenium/"

export basePort=8180
export modelName=""
export androidVersion=""
export isInstalled=""
export idevicePath="$testHome/lib/libimobiledevice-macosx"

#export PKG_NAME=`unzip -p $IPA_PATH  $plistFile |  plutil -p '-' | grep "CFBundleIdentifier" | cut -d " " -f5 | tr -d "\""`

#kill all simulators
ps -ef |grep 'iPhone Simulator.app' | grep -v grep | sed -e 's/^ *//g' | tr -s ' ' | cut -d " " -f2 | while read thePid
	do
	echo "killing $thePid"
	kill $thePid
done

#	modelName=`$idevicePath/ideviceinfo -u $currDevId | grep DeviceName | sed "s/DeviceName: //g"`
#	iosVersion=`$idevicePath/ideviceinfo -u $currDevId | grep ProductVersion | sed "s/ProductVersion: //g"`
#	description="$modelName  $iosVersion"
#	echo "####### $modelName ########"
#	basePort=$[$basePort+1]
	cp -f $testHome/etc/iPhoneSimulator.json	$testHome/etc/Simulator_Temp.json
	
#	sed -i '' "s/PLATFORM/MAC/g" $testHome/etc/Appium_TEMP.json
#	sed -i '' "s/OS_NAME/IOS/g" $testHome/etc/Appium_TEMP.json
#	sed -i '' "s/DEVICE_ID/$currDevId/g" $testHome/etc/Appium_TEMP.json
#	sed -i '' "s/DESCR_STRING/$description/g" $testHome/etc/Appium_TEMP.json
#	sed -i '' "s:APP_PATH:$IPA_PATH:g" $testHome/etc/Appium_TEMP.json
#        if [ $IPA_PATH == "safari" ]
#                then
#                sed -i '' '/app-package/d' $testHome/etc/Appium_TEMP.json
#                sed -i '' '/app-activity/d' $testHome/etc/Appium_TEMP.json
#        else
#		sed -i '' "s:APP_PKG:$PKG_NAME:g" $testHome/etc/Appium_TEMP.json
#        fi
#	sed -i '' "s/DEVICE_NAME/iphone/g" $testHome/etc/Appium_TEMP.json
#	sed -i '' "s/DEVICE_VERSION/$iosVersion/g" $testHome/etc/Appium_TEMP.json
#	sed -i '' "s/MAX_SESSIONS/1/g" $testHome/etc/Appium_TEMP.json
#	sed -i '' "s/APPIUM_PORT/$basePort/g" $testHome/etc/Appium_TEMP.json
	sed -i '' "s/APPIUM_HOST/$RMTestLocalNodeIp/g" $testHome/etc/Simulator_Temp.json
	sed -i '' "s/HUB_PORT/4444/g" $testHome/etc/Simulator_Temp.json
	sed -i '' "s/HUB_HOST/$RMTestHubIp/g" $testHome/etc/Simulator_Temp.json
	cat $testHome/etc/Simulator_Temp.json
	
#	$testHome/appium/bin/appium.js -U $currDevId -a $RMTestLocalNodeIp -p $basePort --nodeconfig ../etc/Appium_TEMP.json &> $testHome/log/appium_$currDevId.log & 
#	sleep 5
	logfile="$testHome/log/logName.log"
        #$testHome/appium/bin/appium.js --nodeconfig $testHome/etc/Simulator_Temp.json --show-ios-log --safari --session-override &> $logfile &
        $testHome/appium/bin/appium.js --nodeconfig  $testHome/etc/Simulator_Temp.json --show-ios-log --safari --session-override &> $logfile &
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

