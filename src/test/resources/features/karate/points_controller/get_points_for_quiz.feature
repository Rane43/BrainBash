Feature: Fetch Points for given quiz
	Background: Login as Quizzer
		* def quizzerResponse = call read("classpath:features/karate/helpers/login_as_quizzer.feature")
		* def token = quizzerResponse.response.token
		
  Scenario: Successfully fetch difficulty ratings
  	Given url baseUrl
  	* header Authorization = 'Bearer ' + token
    And path '/api/difficulty-ratings'
    When method get
    Then status 200 
    