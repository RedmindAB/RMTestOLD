#!/bin/bash 

export modelName=""
export androidVersion=""

echo "Connected devices:"
adb devices | grep "	device" | cut -d "	" -f1 | while read currDevId
do
	modelName=`adb -s $currDevId shell getprop ro.product.model | tr -d "\r"`
	androidVersion=`adb -s $currDevId shell getprop ro.build.version.release | tr -d "\r"`
	echo "----------------------------------------------------"
	echo "Model: $modelName ---- AndroidVersion: $androidVersion ---- ID: $currDevId"

done









