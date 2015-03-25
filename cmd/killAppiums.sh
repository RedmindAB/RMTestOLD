#!/bin/bash
ps ax | grep "node" | grep "ium/bin/appium.js" | cut -d " " -f1 | while read thePid
	do
	echo "killing $thePid"
	kill $thePid
done
ps ax | grep "node" | grep "appium" | cut -d " " -f1 | while read thePid
	do
	echo "killing $thePid"
	kill $thePid
done
