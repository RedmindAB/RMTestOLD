Feature: WebDriver functionalities
  The functions are grouped in Scenarios because having one Scenario per function
  would cause the webdriver to start over and over again, making this test really slow.

  Background:
    Given that we navigate to "http://anvoz.github.io/bootstrap-tldr/"

  Scenario: basic functions and assertions of element
    # string predicates
    Then the title is "Bootstrap TLDR"
    And the title contains "oots"
    And the title starts with "Boot"
    And the title ends with "TLDR"
    # elements
    And the element with id "typography" reads "Typography"
    And the element with xpath "//*[@id="typography"]" reads "Typography"
    And the element with class "text-left" reads ".text-left"
    And the element with css "body > div.container > div > div.col-md-9 > blockquote > p" reads "Bootstrap TLDR"
    And the element with link text "Typography" links to "http://anvoz.github.io/bootstrap-tldr/#typography"
    And the element with partial link text "Typo" links to "http://anvoz.github.io/bootstrap-tldr/#typography"
    And the element with tag "html" is present
    # alias
    Given that we know the element with xpath "//*[@id="sidebar"]/a" as "Back to top"
    # click
    When we click on "Back to top"
    # current url
    Then the current url ends with "/#top"
    # javascript
    And executing "return window.scrollY;" returns 0
    # select an element
    When we select the element with xpath "/html/body/div[2]/div/div[1]/div[13]/div/table/tbody/tr[3]/td[2]"
    # assert the current element
    Then this element reads "Indicates a successful or positive action"
    # css check
    And this element property "background-color" equals "rgba(223, 240, 216, 1)"

