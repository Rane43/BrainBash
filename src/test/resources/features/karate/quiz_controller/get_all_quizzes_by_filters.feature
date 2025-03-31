Feature: Fetch All Quizzes by filters: Title, Difficulty Rating, and Age Rating
	Background: Login as Quizzer (testQuizzer)
		* def quizzerResponse = call read("classpath:features/karate/helpers/login_as_quizzer.feature") 
		* def token = quizzerResponse.response.token
	
	Scenario: Successfully get all quizzes by title only
		Given url baseUrl
  	* header Authorization = 'Bearer ' + token
  	* header Accept = 'application/json'
    And path '/api/quizzes/search'
    And param middleTitle = 'new'
    When method get
    Then status 200
    * def parsedResponse = karate.toJson(response)
    # Quiz Summary info returned
    And match parsedResponse[0].id == '#present'
		And match parsedResponse[0].title == '#present'
		And match parsedResponse[0].description == '#present'
		And match parsedResponse[0].image == '#present'
		And match parsedResponse[0].category == '#present'

	Scenario: Successfully get all quizzes by age rating only
   Given url baseUrl
  	* header Authorization = 'Bearer ' + token
  	* header Accept = 'application/json'
    And path '/api/quizzes/search'
    And param ageRating = 'CHILDREN'
    When method get
    Then status 200
    * def parsedResponse = karate.toJson(response)
    # Quiz Summary info returned
    And match parsedResponse[0].id == '#present'
		And match parsedResponse[0].title == '#present'
		And match parsedResponse[0].description == '#present'
		And match parsedResponse[0].image == '#present'
		And match parsedResponse[0].category == '#present'
	
	Scenario: Successfully get all quizzes by difficulty rating only
		Given url baseUrl
  	* header Authorization = 'Bearer ' + token
  	* header Accept = 'application/json'
    And path '/api/quizzes/search'
    And param difficulyRating = 'EASY'
    When method get
    Then status 200
    * def parsedResponse = karate.toJson(response)
    # Quiz Summary info returned
    And match parsedResponse[0].id == '#present'
		And match parsedResponse[0].title == '#present'
		And match parsedResponse[0].description == '#present'
		And match parsedResponse[0].image == '#present'
		And match parsedResponse[0].category == '#present'
	
  Scenario: Successfully get all quizzes by title, age rating and difficulty rating
  Given url baseUrl
  	* header Authorization = 'Bearer ' + token
  	* header Accept = 'application/json'
    And path '/api/quizzes/search'
    And param middleTitle = 'new'
    And param difficultyRating = 'EASY'
    And param AgeRating = 'CHILDREN'
    When method get
    Then status 200
    * def parsedResponse = karate.toJson(response)
    # Quiz Summary info returned
    And match parsedResponse[0].id == '#present'
		And match parsedResponse[0].title == '#present'
		And match parsedResponse[0].description == '#present'
		And match parsedResponse[0].image == '#present'
		And match parsedResponse[0].category == '#present'
