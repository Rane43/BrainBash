Feature: Create Question
	Background: Login as Quiz Designer (testQuizDesigner)
		* def quizDesignerResponse = call read("classpath:features/karate/helpers/login_as_quiz_designer.feature") 
		* def token = quizDesignerResponse.response.token
		
  Scenario: Successfully create question
  	Given url baseUrl
  	* header Authorization = 'Bearer ' + token
    And path '/api/quizzes/15/questions'
    And request {text: "Test Question?", answerRequestDtos: [{text: "answer1", correct: true}, {text: "answer2", correct: false}, {text: "answer3", correct: false}]}				
    When method post
    Then status 201
    