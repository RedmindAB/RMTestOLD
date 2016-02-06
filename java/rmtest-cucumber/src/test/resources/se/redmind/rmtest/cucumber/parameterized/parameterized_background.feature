@tag2
Feature: we want to be able to use a parameterized scenario in another feature's background

  Background:
    #here we test that all the composition methods work from a background as well
    When that we count the letters in "saturday" and multiply it by 2
    Then this number is 16
    When that we count the letters in "saturday" and multiply it by 2 @full
    Then this number is 16
    When that we count the letters in "saturday" and multiply it by 2 @quiet
    Then this number is 16
    When that we count the letters in "saturday" and multiply it by 2 twice
    Then this number is 32
    When that we count the letters in "saturday" and multiply it by 2 twice @full
    Then this number is 32
    When that we count the letters in "saturday" and multiply it by 2 twice @quiet

  Scenario: this is the end of the background, as a background alone would not run
    Then this number is 32
