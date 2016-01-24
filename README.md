RMTest
======

####Prerequisites

Verify that you have the following software installed: Java, to check installed version: java -version

Maven, to check installed version: mvn -version

Install Android SDK: http://developer.android.com/sdk/index.html


####Initial installation

Go to folder where you want the installation and clone repo:

git clone https://github.com/RedmindAB/RMTest.git

Go to the newly created "RMTest" folder and execute the installation script.

cd RMTest

./install.sh The test will fail if the Hub-server and the Node-server aren't running. (See below). Ignore the test results.

Eclipse Setup and import project

Import RMTest (File -> Import -> Maven -> Existing Maven project -> Next. Search for the RMTest directory and press finish).
