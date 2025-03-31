Feature: Fetch Quiz By Id
	Background: Login as Quizzer (testQuizzer)
		* def quizzerResponse = call read("classpath:features/karate/helpers/login_as_quizzer.feature") 
		* def token = quizzerResponse.response.token
		
  Scenario: Successfully get quiz by Id
  	Given url baseUrl
  	* header Authorization = 'Bearer ' + token
    And path '/api/quizzes/1'
    When method get
    Then status 200
    And match response.id == 1
    And match response.title == '#present'
    And match response.description == '#present'
    And match response.image == '#present'
    And match response.questionIds == '#present'
    And match response.ageRating == '#present'
    And match response.difficultyRating == '#present'
    And match response.category == '#present'
    