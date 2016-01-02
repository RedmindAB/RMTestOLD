Feature: we want to be able to use a parameterized scenario in another scenario and have it possibly located in another feature file

  Scenario: we call a parameterized scenario that will have all its sub steps printed out
    When we count the letters in "saturday" and multiply it by 2
    Then this number is 16

  Scenario: we call a parameterized scenario that will be considered as a single step
    When we quietly count the letters in "saturday" and multiply it by 2
    Then this number is 16

  Scenario: we call a parameterized scenario that will replace the current step
    When we count in place the letters in "saturday" and multiply it by 2
    Then this number is 16

  Scenario: we call a parameterized scenario that is a composite of another parameterized scenario and a step
    When we count the letters in "saturday" and multiply it by 2 twice
    Then this number is 32

  Scenario: we call a parameterized scenario that is a composite of another but quiet parameterized scenario and a step
    When we quietly count the letters in "saturday" and multiply it by 2 twice
    Then this number is 32

  Scenario: we call a parameterized scenario that is a composite of another but in place parameterized scenario and a step
    When we count in place the letters in "saturday" and multiply it by 2 twice
    Then this number is 32
