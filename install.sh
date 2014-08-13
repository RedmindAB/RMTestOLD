#!/bin/bash 

scriptDir="$( cd "$( dirname "$0" )" && pwd )"

echo "TESTHOME=$scriptDir" > $HOME/.RmTest
echo ".RmTest configured"

if [[ ! -f $scriptDir/etc/LocalConfig.json ]]
then
	echo "Could not find LocalConfig.json, creating from template"
	cp $scriptDir/etc/LocalConfigTemplate.json $scriptDir/etc/LocalConfig.json
fi
echo "Verified LocalConfig.json"

mvnHome=`which mvn`
if [[ -x "$mvnHome" ]]
then
	echo "Maven seems OK"
else
	echo "Maven doesnt exist or is not executable"
	exit 1
fi

cd $scriptDir

echo "Running maven compile, correct any errors and rerun this script"

mvn package 

if [[ $? -ne 0 ]]
then
	echo "Found maven Error, please fix and rerun installation"
	exit 1
else
	echo "mvn build completed sucessfully"
fi



echo ""
echo ""
echo "### Installation finished successfully ###"
echo ""
echo ""

echo "Proceed with the following manual configurations"

cat $scriptDir/Installation.txt


 



