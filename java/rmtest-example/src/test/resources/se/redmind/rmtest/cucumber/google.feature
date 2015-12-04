Feature: This is an example of an HTML test in gherkin

  Scenario: verify the title of the page
    When we navigate to the url "http://www.google.com"
    Then the title contains "Goo"
