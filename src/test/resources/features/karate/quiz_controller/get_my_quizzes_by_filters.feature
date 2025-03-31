Feature: Fetch My Quizzes by filters: Title, Difficulty Rating, and Age Rating
	Background: Login as Quiz Designer (testQuizDesigner)
		* def quizDesigerResponse = call read("classpath:features/karate/helpers/login_as_quiz_designer.feature") 
		* def token = quizDesigerResponse.response.token
	
	Scenario: Successfully get my quizzes by title only
		Given url baseUrl
  	* header Authorization = 'Bearer ' + token
    And path '/api/quizzes/mine/search'
    And param middleTitle = 'test'
    When method get
    Then status 200
    # HATEOAS Links
    And match response._links == '#present'
    And match response._links.createQuiz.href == '#present'
    And match response._links.categories.href == '#present'
    And match response._links.ageRatings.href == '#present'
    And match response._links.difficultyRatings.href == '#present'
    And match response._links.quizImages.href == '#present'
	
	Scenario: Successfully get my quizzes by age rating only
    Given url baseUrl
  	* header Authorization = 'Bearer ' + token
    And path '/api/quizzes/mine/search'
    And param ageRating = 'TEEN'
    When method get
    Then status 200
    # HATEOAS Links
    And match response._links == '#present'
    And match response._links.createQuiz.href == '#present'
    And match response._links.categories.href == '#present'
    And match response._links.ageRatings.href == '#present'
    And match response._links.difficultyRatings.href == '#present'
    And match response._links.quizImages.href == '#present'
	
	Scenario: Successfully get my quizzes by difficulty rating only
		Given url baseUrl
  	* header Authorization = 'Bearer ' + token
    And path '/api/quizzes/mine/search'
    And param difficultyRating = 'EASY'
    When method get
    Then status 200
    # HATEOAS Links
    And match response._links == '#present'
    And match response._links.createQuiz.href == '#present'
    And match response._links.categories.href == '#present'
    And match response._links.ageRatings.href == '#present'
    And match response._links.difficultyRatings.href == '#present'
    And match response._links.quizImages.href == '#present'
	
  Scenario: Successfully get my quizzes by title, age rating and difficulty rating
    Given url baseUrl
  	* header Authorization = 'Bearer ' + token
    And path '/api/quizzes/mine/search'
    And param middleTitle = 'test'
    And param difficultyRating = 'EASY'
    And param AgeRating = 'TEEN'
    When method get
    Then status 200
    # HATEOAS Links
    And match response._links == '#present'
    And match response._links.createQuiz.href == '#present'
    And match response._links.categories.href == '#present'
    And match response._links.ageRatings.href == '#present'
    And match response._links.difficultyRatings.href == '#present'
    And match response._links.quizImages.href == '#present'
    