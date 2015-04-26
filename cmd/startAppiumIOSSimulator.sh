#!/bin/bash -x

. $testHome/cmd/setConfig.sh
logName=`getLogPrefix`
export PHONE_NAME=$1
export IOSVERSION=$2
export USAGE_MESSAGE="Usage: command.sh <Iphone model> <IOS version>"

listSims()
{
        xcrun instruments -s devices | grep Simulator | while read line
        do
                simName=`echo $line | cut -d "(" -f 1 | xargs echo`
                simIosVersion=`echo $line | cut -d "(" -f 2 | cut -d ")" -f 1 | cut -d " " -f1`
                if [ "$1 $2" = "$simName $simIosVersion" ]
                then
                        echo "Found simulator"
                        exit 22
                else
                        echo "not matching simulator:\"$simName\" $simIosVersion"
                fi
        done
}

simExists()
{
	listSims "$1" "$2"
	if [ $? -eq 22 ]
	then
      		echo "Found: $PHONE_NAME $IOSVERSION"
       		return 0
	else
       		echo "did not find simulator"
		return 1
	fi
}

if [ -z "$PHONE_NAME" ]; then
        echo $USAGE_MESSAGE
	listSims | grep "not matching" | cut -d ":" -f2
	exit 1
fi
if [ -z "$IOSVERSION" ]; then
        echo $USAGE_MESSAGE
        exit 1
fi



simExists "$PHONE_NAME" "$IOSVERSION"
if [ $? -ne 0 ]
then
	echo "Cannot find simulator with preferences: $PHONE_NAME $IOSVERSION"
	exit 1
fi

export jar_home="$testHome/lib/selenium/"

export basePort=4723
export modelName=""
export androidVersion=""
export isInstalled=""
export idevicePath="$testHome/lib/libimobiledevice-macosx"


#kill all simulators
ps -ef |grep 'iPhone Simulator.app' | grep -v grep | sed -e 's/^ *//g' | tr -s ' ' | cut -d " " -f2 | while read thePid
	do
	echo "killing $thePid"
	kill $thePid
done

description="$PHONE_NAME  $IOSVERSION `hostname`"
cp -f $testHome/etc/iPhoneSimulator_TEMPLATE.json	$testHome/etc/Simulator_Temp.json

sed -i '' "s/DESCR_STRING/$description/g" $testHome/etc/Simulator_Temp.json
sed -i '' "s/DEVICE_NAME/$PHONE_NAME/g" $testHome/etc/Simulator_Temp.json
sed -i '' "s/IOS_VERSION/$IOSVERSION/g" $testHome/etc/Simulator_Temp.json
sed -i '' "s/APPIUM_PORT/$basePort/g" $testHome/etc/Simulator_Temp.json
sed -i '' "s/APPIUM_HOST/$RMTestLocalNodeIp/g" $testHome/etc/Simulator_Temp.json
sed -i '' "s/HUB_PORT/4444/g" $testHome/etc/Simulator_Temp.json
sed -i '' "s/HUB_HOST/$RMTestHubIp/g" $testHome/etc/Simulator_Temp.json
cat $testHome/etc/Simulator_Temp.json

logfile="$logName.log"
echo "Appium version:"
$testHome/appium/bin/appium.js -v
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

