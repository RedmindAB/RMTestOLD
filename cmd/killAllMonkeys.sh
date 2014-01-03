#!/bin/bash 

export modelName=""

adb devices | grep "	device" | cut -d "	" -f1 | while read currDevId
do
	modelName=`$ADB_PATH/adb -s $currDevId shell getprop ro.product.model | tr -d "\r"`

	echo "killing monkeys at $modelName with id: $currDevId"
	$ADB_PATH/adb -s $currDevId shell kill $($ADB_PATH/adb -s $currDevId shell ps | grep monkey | awk '{ print $2 }')
done









