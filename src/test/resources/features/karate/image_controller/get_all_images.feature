Feature: Fetch All Quiz Images
	Background: Login as Quiz Designer
		* def quizzerResponse = call read("classpath:features/karate/helpers/login_as_quiz_designer.feature")
		* def token = quizzerResponse.response.token
		
  Scenario: Successfully fetch difficulty ratings
  	Given url baseUrl
  	* header Authorization = 'Bearer ' + token
    And path '/api/images'
    When method get
    Then status 200 
    