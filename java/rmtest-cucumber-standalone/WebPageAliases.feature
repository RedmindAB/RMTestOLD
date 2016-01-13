Feature: Aliases for Web elements (not test cases)

@parameterized
Scenario: I have loaded Web element aliases
    Given that we know the element with xpath "//nav/div[4]/ul/li[1]/a" as "menu.Username"
    And we know the element with xpath "//*[@id="applicationHost"]/div/div[1]/header/nav/div[4]/ul/li[3]/a/i" as "menu.Settings"
    And we know the element with xpath "//*[@id="applicationHost"]/div/div[1]/div[2]/div[1]/div[1]/div/nav/ul/li//a[@href='#/settings/usergroup']/span" as "SettingsMenu.UserGroups"
    And we know the element with xpath "//*[@id="applicationHost"]/div/div[1]/div[2]/div[2]/div[2]/div[2]/div/div[1]/h2" as "Settings.PageHeader"

