Feature: Delete Question
	Background: Login as Quiz Designer (testQuizDesigner)
		* def quizDesignerResponse = call read("classpath:features/karate/helpers/login_as_quiz_designer.feature") 
		* def token = quizDesignerResponse.response.token
	
	Scenario: Should not be able to delete a question of a quiz they didnt create
		Given url baseUrl
  	* header Authorization = 'Bearer ' + token
    And path '/api/quizzes/questions/1'
    When method delete
    Then status 401
    
  Scenario: Successfully delete question
  	Given url baseUrl
  	* header Authorization = 'Bearer ' + token
    And path '/api/quizzes/questions/29'
    When method delete
    Then status 204
    