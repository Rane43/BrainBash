Feature: Fetch Categories
	Background: Login as Quizzer
		* def quizzerResponse = call read("classpath:features/karate/helpers/login_as_quizzer.feature")
		* def token = quizzerResponse.response.token
		
  Scenario: Successfully fetch categories
  	Given url baseUrl
  	* header Authorization = 'Bearer ' + token
    And path '/api/categories'
    When method get
    Then status 200 
    