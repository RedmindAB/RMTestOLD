Feature: we want to be able to use a parameterized scenario in another scenario

  Scenario:
    When we count the letters in "test" and multiply it by 3
    Then this number is 12

  Scenario:
    When we count the letters in "something else" and multiply it by 1
    Then this number is 14

  Scenario:
    When we count the letters in "saturday" and multiply it by 2
    Then this number is 16

  Scenario:
    When we count the letters in "dog" and multiply it by 0
    Then this number is 0

  @parameterized
  Scenario: we count the letters in <value> and multiply it by <factor>
    Given that we write down the amount of letters in <value>
    And that we multiply it by <factor>
