Feature: Common scenarios for logging in (functions, not test cases)

@parameterized
Scenario: I Login as "Tommy Pettersson"
   When I Login with user "tommy.pettersson@Redmind.se" and password "TommyRedmind"


@parameterized
Scenario: I Login with user <username> and password <password>
   When I input <username> in the element named "Email"
   And I input <password> in the element named "Password"
   And I click on the element with id "btnLogin"