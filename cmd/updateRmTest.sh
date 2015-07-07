#!/bin/bash

export rmTestZip=$1
export rmTestFileName=`basename $rmTestZip`
mv $rmTestZip $testHome/../$rmTestFileName

cd $testHome/cmd
./propagate_file.sh $testHome/../$rmTestFileName

./exec_on_all.sh "cd $testHome/..; unzip -uv $testHome/../$rmTestFileName"

./exec_on_all.sh "cd $testHome/; ./install.sh"
