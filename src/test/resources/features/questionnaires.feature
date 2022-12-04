Feature: This feature will cover questionnaires and questionnaires/{id}/questions endpoints of application
  Details of endpoints you can see in the swagger - http://localhost:8080/swagger-ui/index.html#/

  Background: Setup RestAssured
    Given Setup Rest Assured

  Scenario Outline: Get all questionnaires and questions, validate status code and body
    When Request to get by endpoint <endpoint>
    Then Validate status code is <statusCode>
    And Validate response body against JSON schema for <schema>

    Examples:
      | endpoint                       | schema                    | statusCode |
      | questionnaires                 | questionnaires            | 200        |
      | questionnaires/1/questions     | questionsOfQuestionnaires | 200        |
      | questionnaires/ERROR/questions | questionsError            | 404        |
      | questionnaires/22/questions    | questionsError            | 404        |
      | questionnaires/%%/questions    | questionsError            | 404        |
