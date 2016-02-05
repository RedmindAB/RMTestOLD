Feature: cucumber tags are properly overriden

  Scenario: no options
    Given that the cucumber options are ""
    Then the overriden value is "--tags ~@ignore"

  Scenario: monochrome
    Given that the cucumber options are "-m"
    Then the overriden value is "-m --tags ~@ignore"

  Scenario: a single tag
    Given that the cucumber options are "--tags @test"
    Then the overriden value is "--tags @test,@parameterized,~@ignore"

  Scenario: multiple AND tags
    Given that the cucumber options are "--tags @test --tags @devops"
    Then the overriden value is "--tags @test,@parameterized,~@ignore --tags @devops,@parameterized,~@ignore"

  Scenario: multiple OR tags
    Given that the cucumber options are "--tags @test,@devops"
    Then the overriden value is "--tags @test,@devops,@parameterized,~@ignore"

  Scenario: multiple tags and monochrome
    Given that the cucumber options are "-m --tags @test --tags @devops"
    Then the overriden value is "-m --tags @test,@parameterized,~@ignore --tags @devops,@parameterized,~@ignore"
