Feature: Authorization feature will cover login/sign up/reset password functionality.
  Here will be covered signup/user, authentication/login, authentication/reset-password, authentication/reset-forgotten-password,
  authentication/send-mail endpoints
  For more information see swagger - http://localhost:8088/swagger-ui/index.html#/

  Background: Setup RestAssured
    Given Setup Rest Assured

  Scenario: Create new user with saved credentials from property file
    When Request to POST by endpoint
      | bodyName | newUser |
      | endpoint | SIGN_UP |
    Then Validate status code is 201

  Scenario: Verify link sent to email and activate user
    And Get link from email message
    When Request to GET by endpoint CONFIRM_EMAIL
    Then Validate status code is 200

  Scenario: Sign up with correct credentials
    When Request to POST by endpoint
      | bodyName | userWithCorrectCredentials |
      | endpoint | SIGN_UP                    |
    Then Validate status code is 201
    And Validate response body against JSON schema for auth/signUp

  Scenario Outline: Check validations of sign up functionality
    When Request to POST by endpoint
      | bodyName | <bodyName> |
      | endpoint | <endpoint> |
      | name     | <name>     |
      | password | <password> |
      | email    | <email>    |
      | text     | <text>     |
    Then Validate status code is <statusCode>
    And Validate response body against JSON schema for auth/signUpError
    And Validate error message contains <text>

    Examples:
      | bodyName    | endpoint | name | password   | email                 | text                                      | statusCode |
      | invalidUser | SIGN_UP  | 1    | Password!  | email@gmail.com       | size must be between 2 and 50             | 404        |
      | invalidUser | SIGN_UP  | name | Password2! | heghine9696@gmail.com | the provided email address already exists | 417        |
      | invalidUser | SIGN_UP  | ---- | password   | emailgmail.com        | must be a well-formed email address       | 404        |
      | invalidUser | SIGN_UP  | ---- | "       "  | emailgmail.com        | Password must                             | 404        |

  Scenario: Login with correct credentials
    When Login with existed email and password
    Then Validate status code is 200
    And Validate response body against JSON schema for auth/loginResponse

  Scenario Outline: Login with incorrect credentials
    When Login with incorrect credentials - <username> and <password>
    Then Validate status code is <statusCode>
    And Validate response body against JSON schema for auth/signUpError
    And Validate error message contains <text>

    Examples:
      | username              | password        | statusCode | text              |
      | Heghine9696@gmail.com | WrongPa$sword2! | 401        | Bad credentials   |
      | gmail.com             | My3rdPa$word    | 401        | Bad credentials   |
      | non-existed username  | wrong password  | 409        | Password must     |
      | username              |                 | 409        | Password must     |
      |                       | PaS$word2       | 409        | must not be blank |
      |                       |                 | 409        | must not be blank |

  Scenario: Reset password with token
    When Login with existed email and password
    And Save access token value and type
    When Request to POST by endpoint
      | bodyName | resetPassword  |
      | endpoint | RESET_PASSWORD |
    Then Validate status code is 200
    And Save password value
    And Validate success message for resetting password

  Scenario: Reset forgotten password by receiving message to mail
    When Login with existed email and password
    When Request to POST by query params: "email" "TestAutomationArmenia@gmail.com" using endpoint "SEND_MAIL"
    Then Validate status code is 200
    And Get code from email message
    When Request to POST by endpoint
      | bodyName | resetForgottenPassword   |
      | endpoint | RESET_FORGOTTEN_PASSWORD |
    Then Validate status code is 200
    And Save password value
