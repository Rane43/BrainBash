Feature: Fetch Points for given quiz
	Background: Login as Quizzer (testQuizzer)
		* def quizzerResponse = call read("classpath:features/karate/helpers/login_as_quizzer.feature") 
		* def token = quizzerResponse.response.token
		
  Scenario: Successfully get points for quiz
  	Given url baseUrl
  	* header Authorization = 'Bearer ' + token
    And path '/api/points'
    And param quiz_id = '1'
    When method get
    Then status 200
    And match response == '1'
    