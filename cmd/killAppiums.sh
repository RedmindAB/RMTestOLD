#!/bin/bash

#Cleans up portforwards
ps -e | grep "bin/appium" | grep -v grep | sed -e 's/^ *//g' | tr -s ' ' | cut -d " " -f1 | while read thePid
	do
	echo "killing $thePid"
	kill $thePid
done
echo "Kill ios_webkit"
ps -ef | grep ios_webkit_debug_proxy | awk '{ print $2 }' | while read thePid
do
	echo "killing $thePid"
	kill $thePid
	done