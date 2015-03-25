#!/bin/bash

ps ax | grep java | grep "\-role\ hub" | cut -d " " -f1 | while read thePid
	do
	echo "killing $thePid"
	kill $thePid
done
