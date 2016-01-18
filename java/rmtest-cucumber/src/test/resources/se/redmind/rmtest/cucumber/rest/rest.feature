Feature: Test REST applications

  Background: init the URL and port
    Given that url is "http://localhost"
    And port is 4567

  Scenario: basic call works
    When we get "/"
    Then content is "hello"

  Scenario: send back json object
    Given we send:
      """
      {
        "key":"value"
      }
      """
    When we get "/json"
    Then json key "key" is "value"
    Then parameter "key" has the value "value"

  Scenario: send back json array
    Given we send:
      """
      [
        { "key0" : "value0" },
        { "key1" : "value1" },
        { "key2" : "value2" }
      ]
      """
    When we get "/json"
    Then index 1 has the key "key1" and value "value1"
    Then index 0 has the key "key0" and value "value0"
    Then parameter "[1].key1" has the value "value1"

    Scenario: send back json object with array
      Given we send:
        """
        { "array" :
          [
            { "key" : "value0" },
            { "key" : "value1" },
            { "key" : "value2" }
          ]
        }
        """
      When we get "/json"
      Then parameter "array[1].key" has the value "value1"

  Scenario: header is application/json
    When we get "/json"
    Then header "Content-Type" is "application/json"
