#!/bin/bash -x

export IPA_PATH=$1
export PKG_NAME="NOTSET"
if [ -z "$IPA_PATH" ]; then
	echo "Usage:"
	echo "installIpa.sh <ipafile>"
	echo "ipafile: Full or relative path to the .ipa file"
	exit 1
fi

echo "Apk to install: $IPA_PATH"
export plistFile=`zipinfo -1 $IPA_PATH  | grep app/Info.plist`
PKG_NAME=`unzip -p $IPA_PATH  $plistFile |  plutil -p '-' | grep "CFBundleIdentifier" | cut -d " " -f5 | tr -d "\""`   
echo "Package name: $PKG_NAME"
export modelName=""

idevice_id -l | while read currDevId
do
	modelName=`ideviceinfo -u $currDevId | grep DeviceName`

	echo "Trying to (re)install package: $PKG_NAME $modelName with id: $currDevId"

	ideviceinstaller -U $currDevId -u $PKG_NAME

	ideviceinstaller -U $currDevId -i $IPA_PATH
done









