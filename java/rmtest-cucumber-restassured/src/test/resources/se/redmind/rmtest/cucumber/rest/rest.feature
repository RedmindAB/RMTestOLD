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
        "foo":"bar"
      }
      """
    When we get "/json"
    Then json key "foo" is "bar"
    Then parameter "foo" is "bar"

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
    Then parameter "[1].key1" is "value1"

  Scenario: send back json object with array
    Given we send:
      """
      { "array" :
        [
          { "key" : "value0", "key2" : { "number" : 1 } },
          { "key" : "value1" },
          { "key" : 2.1 }
        ]
      }
      """
    When we get "/json"
    Then parameter "array[1].key" is "value1"
    Then size of "array" is 3
    Then parameter "array[0].key2.number" is 1
    Then parameter "array[2].key" is 2.1

  Scenario: header is application/json
    When we get "/json"
    Then header "Content-Type" is "application/json"

  Scenario: get custom status
    When we get "/status/500"
    Then status is 500

  Scenario: meassure time
    When we get "/"
    Then time is below 1000 milliseconds
    Then time is below 1 seconds
    Then time is below 1000

  Scenario: send params
    Given we send param "user" with value "me"
    And we send param "user2" with "me2"
    When we get "/param"
    Then parameter "user" is "me"
    Then parameter "user2" is "me2"