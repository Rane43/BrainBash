Feature: Log in as Admin
  Background: 
  	* def baseUrl = karate.get('baseUrl')
  	
  Scenario: Authenticate Admin User and Get Token
    Given url baseUrl + '/api/auth/login'
    * header Content-Type = 'application/json'
    And request { username: "testAdmin", password: "TestPassword123!" }
    When method post
    Then status 200
		* def token = response.token
		* match token != null
		* match token != ''
		
	Scenario: Invalid Login as Admin
		Given url baseUrl + '/api/auth/login'
    * header Content-Type = 'application/json'
    And request { username: "testAdmin", password: "InvalidPassword123!" }
    When method post
    Then status 401
		* def message = response
		* match message == 'Invalid password.'
		
	Scenario: Invalid http body request # To cover validation exception handler
		Given url baseUrl + '/api/auth/login'
    * header Content-Type = 'application/json'
    And request { USAME: "testAdmin", password: "InvalidPassword123!" }
    When method post
    Then status 400
		* def message = response
