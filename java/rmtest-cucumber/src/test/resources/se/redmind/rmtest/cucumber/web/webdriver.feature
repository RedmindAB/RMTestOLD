Feature: WebDriver functionalities
  The functions are grouped in big scenarios because having one Scenario per function
  would cause the webdriver to start over and over again, making this test really slow.

  Scenario: basic functions and assertions of element
    Given that we navigate to "http://anvoz.github.io/bootstrap-tldr/"
    And that we maximize the window
    # string predicates
    Then the title reads "Bootstrap TLDR"
    And the title contains "oots"
    And the title starts with "Boot"
    And the title ends with "TLDR"
    # elements by id, xpath, class, css selector, link text, partial link text, tag
    Then the element with id "typography" reads "Typography"
    Then the element with xpath "//*[@id="typography"]" reads "Typography"
    Then the element with class "text-left" reads ".text-left"
    Then the element with css "body > div.container > div > div.col-md-9 > blockquote > p" reads "Bootstrap TLDR"
    Then the element with link text "Typography" links to "http://anvoz.github.io/bootstrap-tldr/#typography"
    Then the element with partial link text "Typo" links to "http://anvoz.github.io/bootstrap-tldr/#typography"
    And we select the element with tag "body"
    # alias
    Given that we know the element with xpath "//*[@id="sidebar"]/a" as "Back to top"
    # click
    When we click on "Back to top"
    # current url
    Then the current url ends with "#top"
    # javascript
    And executing "return window.scrollY;" returns 0
    # select an element
    Given this alias:
      | type  | id                                                               | value       |
      | xpath | /html/body/div[2]/div/div[1]/div[13]/div/table/tbody/tr[3]/td[2] | Success box |
    When we select the "Success box"
    # assert the current element
    Then it reads "Indicates a successful or positive action"
    # css check
    And its property "background-color" equals "rgba(223, 240, 216, 1)"
    # one liners on properties and attributes
    Then the property "background-color" of the "Success box" equals "rgba(223, 240, 216, 1)"
    Then the attribute "colspan" of the "Success box" is "4"
    # selection
    When we click on the checkbox with xpath "/html/body/div[2]/div/div[1]/div[16]/form/div[4]/div/div/label/input"
    Then this element is selected
    # navigation
    When we navigate to "http://anvoz.github.io/bootstrap-tldr/#css"
    Then the current url ends with "#css"
    When we go back
    Then the current url ends with "#top"
    When we go forward
    Then the current url ends with "#css"
    Then we refresh
    # regex
    Then the current url matches "http://.+#css"
    # loading aliases from an external file
    Given the aliases defined in the file "src/test/resources/se/redmind/rmtest/cucumber/web/aliases"
    When we click the element "input.form-control"
    And that we input "test"
    Then this element reads "test"

  #    Then we wait 10 seconds
  Scenario: cookies
    Given that we navigate to our local spark at "/"
    And that we add those cookies:
      | name          | value                 |
      | Authorization | base64(user:password) |
      | base          | something cool        |
      | sessionid     | 1lknsdf912lk12eas90   |
    When we navigate to our local spark at "/cookie/valueOf/Authorization"
    Then the page content is "base64(user:password)"
    Given that we delete the cookie "Authorization"
    When we navigate to our local spark at "/cookie/valueOf/Authorization"
    Then the page content is "null"
    Given that we delete all the cookies
    When we navigate to our local spark at "/cookie/valueOf/sessionid"
    Then the page content is "null"
