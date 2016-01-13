#encoding: ISO-8859-1
Feature: Developing automated test for MyNumbers Web site

  Background: Load web page aliases
    Given I have loaded Web element aliases
    And I maximize the window
    And I navigate to "https://epmweb-st.azurewebsites.net/"

  Scenario: I navigate to Users and groups in Settings as Administrator
    When I Login as "Tommy Pettersson" @full
    And I wait 3 seconds
    And I click "menu.Settings"
    And I wait 2 seconds
    Then "SettingsMenu.UserGroups" equals "Användare och grupper"
