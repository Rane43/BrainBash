Feature: Create Quiz
	Background: Login as Quiz Designer (testQuizDesigner)
		* def quizDesignerResponse = call read("classpath:features/karate/helpers/login_as_quiz_designer.feature") 
		* def token = quizDesignerResponse.response.token
	
	Scenario: Successfully create quiz
		Given url baseUrl
  	* header Authorization = 'Bearer ' + token
  	* header Accept = 'application/json'
    And path '/api/quizzes'
    And request { title: "newQuiz", description: "New Quiz for testing", image: "test-image.jpeg", ageRating: 'TEEN', difficultyRating: 'EASY', category: 'ANATOMY'}
    When method post
    Then status 201
		