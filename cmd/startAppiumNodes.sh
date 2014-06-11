#!/bin/bash -x

. $testHome/cmd/setConfig.sh

export NodeConfig=$1
if [ -z "$NodeConfig" ]; then
    echo "usage: Full or relative path to a nodeconfig json file"
    exit 1
fi

appium -p 7777 &

sleep 3

java -jar $testHome/lib/selenium/selenium-server-standalone-$SeleniumVersion.jar -role node -nodeConfig $NodeConfig


