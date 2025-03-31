Feature: Register as Quiz Developer
  Background: 
  	* def baseUrl = karate.get('baseUrl')
  		
  Scenario: Register with used username
		Given url baseUrl + '/api/auth/register'
    * header Content-Type = 'application/json'
    * def username = 'testquizdesigner'
    And request { username: "#(username)", password: "newPassword123!", role: 'ROLE_QUIZ_DESIGNER'}
    When method post
    * print response
    Then status 409
		* def message = response
		* match message == 'User with username: ' + username + ' already exists.'
  	
  Scenario: Successfully register as Quiz Developer
    Given url baseUrl + '/api/auth/register'
    * header Content-Type = 'application/json'
    And request { username: "newQuizDesigner", password: "newPassword123!", role: 'ROLE_QUIZ_DESIGNER'}
    When method post
    Then status 201
		* def token = response.token
		* match token != null
		* match token != ''
