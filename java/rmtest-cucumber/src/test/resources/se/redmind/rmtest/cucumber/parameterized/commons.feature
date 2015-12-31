Feature: this file contains the parameterized scenarios
    The scenario annotated with @quiet will not have its substeps printed out

  @parameterized
  Scenario: we count the letters in <value> and multiply it by <factor>
    Given that we write down the amount of letters in <value>
    And that we multiply it by <factor>

  @parameterized @quiet
  Scenario: we quietly count the letters in <value> and multiply it by <factor>
    Given that we write down the amount of letters in <value>
    And that we multiply it by <factor>
