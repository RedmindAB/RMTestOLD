#!/bin/bash 

. $testHome/cmd/setConfig.sh

export modelName=""
export androidVersion=""
export modelBrand=""
echo "Connected devices:"
adb devices | grep "	device" | cut -d "	" -f1 | while read currDevId
do
	modelBrand=`adb -s $currDevId shell getprop ro.product.brand | tr -d "\r"`
	modelName=`adb -s $currDevId shell getprop ro.product.model | tr -d "\r"`
	androidVersion=`adb -s $currDevId shell getprop ro.build.version.release | tr -d "\r"`
	echo "----------------------------------------------------"
	echo "Model: $modelBrand $modelName ---- AndroidVersion: $androidVersion ---- ID: $currDevId"

done









