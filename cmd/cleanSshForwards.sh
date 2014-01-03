#!/bin/bash

#Cleans up portforwards
ps -e | grep "ssh -nNT -L" | grep -v grep | sed -e 's/^ *//g' | tr -s ' ' | cut -d " " -f1 | while read thePid
	do
	echo "killing $thePid"
	kill $thePid
done
