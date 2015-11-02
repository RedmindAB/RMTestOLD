@configuration
Feature: RMTest Configuration file

  Scenario: read and validate an invalid config file
    When we read the following configuration file:
      """
      autoCloseDrivers: true
      jsonReportSavePath: /some/path/target/RMTReports
      rmReportIP: 127.0.0.1
      rmReportLivePort: 12345
      """
    And that we validate it
    Then we get a ValidationException

  Scenario: read and validate a valid local config file
    When we read the following configuration file:
      """
      runner:
        type: local
        usePhantomJS: true
        useChrome: true
        useFirefox: false
        android:
          home: /some/path
          toolsVersion: 4.4
      autoCloseDrivers: true
      jsonReportSavePath: /some/path/target/RMTReports
      rmReportIP: 127.0.0.1
      rmReportLivePort: 12345
      """
    And that we validate it
    Then we get no error

  Scenario: read and validate a valid grid config file
    When we read the following configuration file:
      """
      runner:
        type: grid
        localIp: 127.0.0.1
        hubIp: 127.0.0.1
        enableLiveStream: false
      autoCloseDrivers: true
      jsonReportSavePath: /some/path/target/RMTReports
      rmReportIP: 127.0.0.1
      rmReportLivePort: 12345
      """
    And that we validate it
    Then we get no error

  Scenario: read and validate a valid legacy config file
    When we read the following configuration file:
      """
      {
        "configuration":
        {
          "androidHome": "/some/path",
          "localIp": "127.0.0.1",
          "hubIp": "127.0.0.1",
          "AndroidBuildtoolsVersion": "4.4",
          "runOnGrid": "false",
          "usePhantomJS":"true",
          "useChrome":"true",
          "useFirefox":"false",
          "autoCloseDrivers":"true",
          "RmReportIP":"127.0.0.1",
          "RmReportLivePort":"12345",
          "enableLiveStream":"false"
        }
      }
      """
    And that we validate it
    Then we get no error

  Scenario: override a configuration propery by using a system.property
    Given that the system property "runner.useFirefox" is set to "true"
    When we read the following configuration file:
      """
      runner:
        type: local
        useFirefox: false
      """
    Then the configuration property "runner.useFirefox" is equal to "false"
    Given that we apply the system properties
    Then the configuration property "runner.useFirefox" is equal to "true"
