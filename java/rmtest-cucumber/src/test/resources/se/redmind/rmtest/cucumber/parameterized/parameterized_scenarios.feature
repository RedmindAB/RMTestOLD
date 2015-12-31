Feature: we want to be able to use a parameterized scenario in another scenario and have it possibly located in another feature file

  Scenario: we call a parameterized scenario that will have all its sub steps printed out
    When we count the letters in "something else" and multiply it by 1
    Then this number is 14

  Scenario: we call a parameterized scenario that will be considered as a single step
    When we quietly count the letters in "saturday" and multiply it by 2
    Then this number is 16
