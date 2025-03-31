Feature: Login as Quizzer
	Scenario: Login as Quizzer
    Given url baseUrl + '/api/auth/login'
    * header Content-Type = 'application/json'
    And request { username: "testQuizDesigner", password: "TestPassword123!" }
    When method post
    Then status 200
		* def token = response.token