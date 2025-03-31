Feature: Update points for a given quiz
	Background: Login as Quizzer (testQuizzer)
		* def quizzerResponse = call read("classpath:features/karate/helpers/login_as_quizzer.feature") 
		* def token = quizzerResponse.response.token
		
  Scenario: Successfully fetch difficulty ratings
  	Given url baseUrl
  	* header Authorization = 'Bearer ' + token
    And path '/api/points'
    And request { quizId: '1', points: '2' }
    When method put
    Then status 200
    And match response == '2'
    