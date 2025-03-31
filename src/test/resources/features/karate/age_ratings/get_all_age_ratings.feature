Feature: Fetch Age ratings
	Background: Login as Quizzer
		* def quizzerResponse = call read("classpath:features/karate/helpers/login_as_quizzer.feature")
		* def token = quizzerResponse.response.token
		
  Scenario: Successfully fetch age ratings
  	Given url baseUrl
  	* header Authorization = 'Bearer ' + token
    And path '/api/age-ratings'
    When method get
    Then status 200 
    