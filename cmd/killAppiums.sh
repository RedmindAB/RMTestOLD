#!/bin/bash

#Cleans up portforwards
ps -e | grep "/usr/local/bin/appium" | grep -v grep | sed -e 's/^ *//g' | tr -s ' ' | cut -d " " -f1 | while read thePid
	do
	echo "killing $thePid"
	kill $thePid
done
ps -ef | grep ios_webkit_debug_proxy | awk '{ print $2 }' | while read thePid
do
	echo "killing $thePid"
	kill $thePid
	done