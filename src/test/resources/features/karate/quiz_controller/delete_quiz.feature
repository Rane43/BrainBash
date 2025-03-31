Feature: Delete Quiz
	Background: Login as Quiz Designer (testQuizDesigner)
		* def quizDesignerResponse = call read("classpath:features/karate/helpers/login_as_quiz_designer.feature") 
		* def token = quizDesignerResponse.response.token
	
	Scenario: Successfully get all quizzes by title only
		Given url baseUrl
  	* header Authorization = 'Bearer ' + token
  	* header Accept = 'application/json'
    And path '/api/quizzes/16'
    When method delete
    Then status 204
		