#!/bin/bash -x

. $testHome/cmd/setConfig.sh

logName=`getLogPrefix`

export NodeConfig=$1
if [ -z "$NodeConfig" ]; then
    echo "usage: Full or relative path to a nodeconfig json file"
    exit 1
fi

java -Dwebdriver.chrome.driver=$testHome/node_modules/chromedriver/bin/chromedriver -jar $testHome/lib/selenium-server-standalone-$seleniumVersion.jar -role node -nodeConfig $NodeConfig &> $logName.log &


