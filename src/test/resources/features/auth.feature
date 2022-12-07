Feature: Authorization feature will cover login/sign up/reset password functionality.
  Here will be covered signup/user, authentication/login, authentication/reset-password, authentication/reset-forgotten-password,
  authentication/send-mail endpoints
  For more information see swagger - http://localhost:8088/swagger-ui/index.html#/

  Background: Setup RestAssured
    Given Setup Rest Assured

  Scenario: Sign up with correct credentials
    When Request to POST body parameters for newUser by endpoint SIGN_UP
    Then Validate status code is 201
    And Validate response body against JSON schema for auth/signUp

  Scenario Outline: Check validations of sign up functionality
    When Request to POST <name> <email> <password> invalidUser by endpoint SIGN_UP
    Then Validate status code is <statusCode>
    And Validate response body against JSON schema for auth/signUpError
    And Validate error message contains <text>

    Examples:
      | name | password   | email                 | text                                      | statusCode |
      |      | password   | email@gmail.com       | size must be between 2 and 50             | 404        |
      | name | Password2! | heghine9696@gmail.com | the provided email address already exists | 417        |
      | ---- | password   | emailgmail.com        | must be a well-formed email address       | 404        |
      | ---- |            | emailgmail.com        | Password must                             | 404        |

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
      When Request to POST body parameters for resetPassword by endpoint RESET_PASSWORD
      Then Validate status code is 200
      And Validate success message for resetting password
