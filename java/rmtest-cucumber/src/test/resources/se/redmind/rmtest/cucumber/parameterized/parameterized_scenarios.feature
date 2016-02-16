@unit @tag1
Feature: we want to be able to use a parameterized scenario in another scenario

  Scenario: we call a parameterized scenario that will replace the current step
    When we count the letters in "saturday" and multiply it by 2
    Then this number is 16

  Scenario: we call a parameterized scenario that will have all its sub steps printed out (full)
    When we count the letters in "saturday" and multiply it by 2 @full
    Then this number is 16

  Scenario: we call a parameterized scenario that will be considered as a single step (quiet)
    When we count the letters in "saturday" and multiply it by 2 @quiet
    Then this number is 16

  Scenario: we call a parameterized scenario that is a composite of another parameterized scenario and a step
    When we count the letters in "saturday" and multiply it by 2 twice
    Then this number is 32

  Scenario: we call a parameterized scenario that is a composite of another parameterized scenario and a step (full)
    When we count the letters in "saturday" and multiply it by 2 twice @full
    Then this number is 32

  Scenario: we call a parameterized scenario that is a composite of another parameterized scenario and a step (quiet)
    When we count the letters in "saturday" and multiply it by 2 twice @quiet
    Then this number is 32

