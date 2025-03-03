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

