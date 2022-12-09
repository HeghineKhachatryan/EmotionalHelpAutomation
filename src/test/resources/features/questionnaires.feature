Feature: This feature will cover questionnaires and questionnaires/{id}/questions endpoints of application
  Details of endpoints you can see in the swagger - http://localhost:8080/swagger-ui/index.html#/

  Background: Setup RestAssured
    Given Setup Rest Assured

  Scenario Outline: Get all questionnaires and questions, validate status code and body
    When Request to GET by endpoint <endpoint>
    Then Validate status code is <statusCode>
    And Validate response body against JSON schema for <schema>

    Examples:
      | endpoint                        | schema                                   | statusCode |
      | questionnaires                  | questionnaires/questionnaires            | 200        |
      | questionnaires/1/questions      | questionnaires/questionsOfQuestionnaires | 200        |
      | questionnaires/ERROR/questions  | questionnaires/questionsError            | 404        |
      | questionnaires/822232/questions | questionnaires/questionsError            | 404        |
      | questionnaires/\%\%/questions   | questionnaires/questionsError            | 400        |

  Scenario: Create new questionnaire
    When Request to POST by endpoint
      | bodyName | questionnaires |
      | endpoint | QUESTIONNAIRES |
    Then Validate status code is 201
    And Validate response body against JSON schema for questionnaires/create_questionnaires