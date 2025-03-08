@tag
Feature: User Login
  I want to use this template for my feature file

  @tag1
  Scenario: Success - Valid credentials
		Given I am on the login page
		When I enter a valid username "<username>"
		And I enter a valid password "<password>"
		And I click the login button
		Then I am redirected to the homepage
		And my avatar appears in the top right to indicate I’m logged in
		
		Examples: 
      | username   | password         |
      | testadmin | TestPassword123! |
  
  @tag2
  Scenario: Failure - Invalid credentials
  	Given I am on the login page 
  	When I enter an invalid username "<username>" or password "<password>"
  	And I click the login button
  	Then an error message "<error_message>" appears.
  	
  	Examples: 
      | username     | password     | error_message |
      | invalid_user | Password123! | Invalid Username or Password |
      
