#!/bin/bash 

currentUser=`whoami`

export DIR=$(dirname ${1})
export FILE=$(basename ${1})



cat $testHome/etc/hostList | while read hostIp
do
	echo "Copying file: $1 to host: $hostIp"
	ssh -nfq $currentUser@$hostIp "mkdir -p $DIR"
	scp -r $1 $currentUser@$hostIp:$1 
done
