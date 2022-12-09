Feature: Get resources feature will cover requests for getting different resources.
  Here will be covered users/{id}, articles, articles/{id}, projects, specialists
  For more information see swagger - http://localhost:8088/swagger-ui/index.html#/

  Background: Setup Rest Assured
    Given Setup Rest Assured

  Scenario Outline: Create new resources
    When Request to POST by endpoint
      | endpoint | <endpoint> |
      | bodyName | <bodyName> |
    Then Validate status code is 201
    And Validate response body against JSON schema for <schema>

    Examples:
      | endpoint    | bodyName    | schema                       |
      | SPECIALISTS | specialists | resources/create_specialists |
      | PROJECTS    | projects    | resources/create_projects    |
      | ARTICLES    | articles    | resources/create_articles    |

  Scenario Outline: Get resources by the given endpoints
    When Request to GET by endpoint <endpoint>
    Then Validate status code is 200
    And Validate response body against JSON schema for <schema>

    Examples:
      | endpoint    | schema                 |
      | articles    | resources/articles     |
      | articles/1  | resources/articlesByID |
      | projects    | resources/projects     |
      | specialists | resources/specialists  |

  Scenario: Get user by ID and change credentials
    When Login with existed email and password
    And Save access token value and type
    And Save ID of the user
    Then Request to GET by token, id and endpoint users/{id}
    And Validate status code is 200
    And Validate response body against JSON schema for auth/signUp
    When Request to PATCH by endpoint USERS_ID_UPDATE
    Then Validate status code is 200
    And Validate required field value has been changed
