#!/bin/bash 

scriptDir="$( cd "$( dirname "$0" )" && pwd )"
cd $scriptDir

foundOldConfig=false
if [ -f $HOME/.RmTest ]; then
	oldRmHome=`grep TESTHOME ~/.RmTest | cut -d "=" -f2`
	if [ -f $oldRmHome/etc/LocalConfig.json ]; then
		foundOldConfig=true		
	fi    
fi

echo "TESTHOME=$scriptDir" > $HOME/.RmTest
echo ".RmTest configured"

if [[ ! -f $scriptDir/etc/LocalConfig.json ]]
then
	if [ $foundOldConfig ] && [ -f $oldRmHome/etc/LocalConfig.json ]; then
    		echo "Found old installation, copying config"
		cp $oldRmHome/etc/LocalConfig.json $scriptDir/etc/LocalConfig.json
	else
		echo "Could not find old config, creating from template"
		cp $scriptDir/etc/LocalConfigTemplate.json $scriptDir/etc/LocalConfig.json
	fi
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

echo $(tput setaf 1) "Proceed with the following manual configurations"

cat $scriptDir/Installation.txt

echo $(tput sgr0)


