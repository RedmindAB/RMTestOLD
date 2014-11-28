#!/bin/bash -x

DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
cd $DIR

if [ ! -f ./push_keys.sh ]
then
	echo "cant find push_keys.sh "
	exit 1
else
	./propagate_file.sh $DIR/push_keys.sh
fi

if [ ! -f ../etc/hostList ]
then
	echo "cant find hostList file"
	exit 1
else
	./propagate_file.sh $DIR/../etc/hostList
fi

if [ ! -d ~/.ssh/ ] 
then 
	mkdir ~/.ssh 
fi
if [ ! -f ~/.ssh/id_dsa.pub ] ; then
	echo "The local file ~/.ssh/id_dsa.pub is missing. Let's create it."
	ssh-keygen -t dsa 
fi

cat ../etc/hostList | while read hostIp
do
	./push_keys.sh $hostIp
done
