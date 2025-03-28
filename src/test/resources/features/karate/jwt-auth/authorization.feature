Feature: Role-based access

	# Unauthorized access - no authorization token in header
  Scenario: Unauthorized access
  	Given url baseUrl
    And path '/test/api/roles/quizzer'
    When method get
    Then status 403   	
		
	# --------------- QUIZZER-SPECIFIC ACCESS -------------------
  Scenario: Quizzer can access quizzer-specific endpoints
  	# Log in as Quizzer
  	Given url baseUrl + '/api/auth/login'
    * header Content-Type = 'application/json'
    And request { username: "testQuizzer", password: "TestPassword123!" }
    When method post
    Then status 200
		* def token = response.token
		* match token != null
		* match token != ''
  
  	# Make request to quizzer-specific endpoint
  	Given url baseUrl + '/test/api/roles/quizzer'
    * header Content-Type = 'application/json'
    * header Authorization = 'Bearer ' + token
    When method get
    Then status 200
		* match response == 'All Good!'
	
	Scenario: Quizzer cant access quiz_designer-specific endpoints
  	# Log in as Quizzer
  	Given url baseUrl + '/api/auth/login'
    * header Content-Type = 'application/json'
    And request { username: "testQuizzer", password: "TestPassword123!" }
    When method post
    Then status 200
		* def token = response.token
		* match token != null
		* match token != ''
  
  	# Make request to quiz_designer-specific endpoint
  	Given url baseUrl + '/test/api/roles/quiz_designer'
    * header Content-Type = 'application/json'
    * header Authorization = 'Bearer ' + token
    When method get
    Then status 403
		
		
	# --------------- QUIZ_DESIGNER-SPECIFIC ACCESS -------------------
  Scenario: Quiz deisgner can access quiz-designer-specific endpoints
  	# Log in as Quiz Designer
  	Given url baseUrl + '/api/auth/login'
    * header Content-Type = 'application/json'
    And request { username: "testQuizDesigner", password: "TestPassword123!" }
    When method post
    Then status 200
		* def token = response.token
		* match token != null
		* match token != ''
  
  	# Make request to quiz-designer-specific endpoint
  	Given url baseUrl + '/test/api/roles/quiz_designer'
    * header Content-Type = 'application/json'
    * header Authorization = 'Bearer ' + token
    When method get
    Then status 200
		* match response == 'All Good!'
		
	Scenario: Quiz Designer cant access quizzer-specific endpoints
  	# Log in as Quiz Designer
  	Given url baseUrl + '/api/auth/login'
    * header Content-Type = 'application/json'
    And request { username: "testQuizDesigner", password: "TestPassword123!" }
    When method post
    Then status 200
		* def token = response.token
		* match token != null
		* match token != ''
  
  	# Make request to quizzer-specific endpoint
  	Given url baseUrl + '/test/api/roles/quizzer'
    * header Content-Type = 'application/json'
    * header Authorization = 'Bearer ' + token
    When method get
    Then status 403

