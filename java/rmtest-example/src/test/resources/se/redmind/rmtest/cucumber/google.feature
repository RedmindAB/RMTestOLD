Feature: This is an example of an HTML test in gherkin

  Scenario: verify the title of the page
    When we navigate to the url "http://www.google.com"
    Then the title contains "Goo"

  Scenario: verify the title of the page
    When we navigate to the url "http://www.redmind.se"
    Then the title contains "Redmind"
    Given that the screen is maximized
    Then the element at //*[@id="menu-item-9"]/a is "OM OSS"
