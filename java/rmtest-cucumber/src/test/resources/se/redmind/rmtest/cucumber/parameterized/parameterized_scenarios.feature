Feature: we want to be able to use a parameterized scenario in another scenario

  Scenario:
    When I count the letters in "test"
    Then this number is 4

  Scenario:
    When I count the letters in "something else"
    Then this number is 14

  Scenario:
    When I count the letters in "saturday"
    Then this number is 8

  Scenario:
    When I count the letters in "dog"
    Then this number is 3

  @parameterized
  Scenario: When I count the letters in <value>
    Then we write down the amount of letters in <value>
    And I do some other stuff

