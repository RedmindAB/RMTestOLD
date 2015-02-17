#!/bin/bash 

currentUser=`whoami`

cat $testHome/etc/hostList | grep -v "#" | while read hostIp
do
	echo "Running command $1 on host: $hostIp"
	ssh -nq $currentUser@$hostIp $1 
done
