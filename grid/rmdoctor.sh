#!/bin/bash

green='\033[0;32m'
NC='\033[0m'

echo "##### Starting checks #####"

########## CHECKING MAVEN #########
mvnRes=""
echo "Checking Maven"
mvn=`which mvn`
if [[ -x $mvn ]]
then
	mvnRes="OK!"
else
	mvnRes="No maven seems to be installed"
fi

########## CHECKING JAVA ##########

javaRes="";

echo "Checking java"

if type -p java; then
    _java=java
elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then     
    _java="$JAVA_HOME/bin/java"
else
    javaRes="No java version seems to be installed"
fi

if [[ "$_java" ]]; then
    version=$("$_java" -version 2>&1 | awk -F '"' '/version/ {print $2}')
    if [[ "$version" > "1.7" ]]; then
	javaRes="OK!"
    else
	javaRes="The java version is to low, you have $version installed and it needs to be 1.7 or higher"
    fi
fi

########## CHECKING TESTHOME ########
echo "Checking \$testHome"
testHomeRes="";
if [[ ! -z $testHome ]]
then
	testHomeRes="OK!"
else
	testHomeRes="the testHome system variable is not set"
fi

####### Print Results ########
echo "##### Results #####"
echo "Maven:		$mvnRes"
echo "Java:		$javaRes"
echo "\$testHome	$testHomeRes"
