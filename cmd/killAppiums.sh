#!/bin/bash

#Cleans up portforwards
ps -e | grep "ium/bin/appium.js" | grep -v grep | sed -e 's/^ *//g' | tr -s ' ' | cut -d " " -f1 | while read thePid
	do
	echo "killing $thePid"
	kill $thePid
done
