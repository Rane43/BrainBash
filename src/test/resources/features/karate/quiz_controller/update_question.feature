Feature: Update Question
	Background: Login as Quiz Designer (testQuizDesigner)
		* def quizDesignerResponse = call read("classpath:features/karate/helpers/login_as_quiz_designer.feature") 
		* def token = quizDesignerResponse.response.token
	
	Scenario: Should not be able to update a question of a quiz they didnt create
		Given url baseUrl
  	* header Authorization = 'Bearer ' + token
    And path '/api/quizzes/questions/1'
    And request {text: "Test Question?", answerRequestDtos: [{text: "answer1", correct: true}, {text: "answer2", correct: false}, {text: "answer3", correct: false}]}				
    When method put
    Then status 401
    
  Scenario: Successfully update question
  	Given url baseUrl
  	* header Authorization = 'Bearer ' + token
    And path '/api/quizzes/questions/29'
    And request {text: "Test Question?", answerRequestDtos: [{text: "answer1", correct: true}, {text: "answer2", correct: false}, {text: "answer3", correct: false}]}				
    When method put
    Then status 200
    