@tag3
Feature: those scenarios shouldn't run

  @unit @ignore
  Scenario: Failure because of ignore
    When I run, I fail

  @integration
  Scenario: Failure because of wrong tag
    When I run, I fail
