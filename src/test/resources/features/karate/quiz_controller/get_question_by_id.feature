Feature: Fetch Question By Id
	Background: Login as Quizzer (testQuizzer)
		* def quizzerResponse = call read("classpath:features/karate/helpers/login_as_quizzer.feature") 
		* def token = quizzerResponse.response.token
		
  Scenario: Successfully get question by Id
  	Given url baseUrl
  	* header Authorization = 'Bearer ' + token
    And path '/api/quizzes/questions/1'
    When method get
    Then status 200
    And match response.id == 1
    And match response.text == '#present'
    And match response.quizId == '#present'
    And match response.answerDtos == '#[]'
    