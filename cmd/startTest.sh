#/bin/bash -x

. $testHome/cmd/setConfig.sh

$ANDROID_HOME/platform-tools/adb devices | grep "\tdevice" | cut -d "	" -f1 | while read currDevId
do
        screenWidth=`$ANDROID_HOME/platform-tools/adb -s $currDevId shell dumpsys window | grep mUnrestrictedScreen | cut -d ")" -f2 | cut -d "x" -f1 | xargs echo`
        screenDensity=`$ANDROID_HOME/platform-tools/adb -s $currDevId shell getprop ro.sf.lcd_density | tr -d "\r"`
        screenSize=`echo "scale=1;$screenWidth/$screenDensity" | bc`
        echo "Screenwidth: $screenWidth"
        echo "ScreenDensity: $screenDensity"
        echo "ScreenSizeInches: $screenSize"
done
