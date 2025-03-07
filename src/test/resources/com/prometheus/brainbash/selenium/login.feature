@tag
Feature: User Login
  I want to use this template for my feature file

  @tag1
  Scenario: Success - Valid credentials
		Given I am on the login page
		When I enter a valid email "<email>"
		And I enter a valid password "<password>"
		And I click the login button
		Then I am redirected to the homepage
		And my avatar appears in the top right to indicate I’m logged in
		
		Examples: 
      | email  | password |
      | test_admin | TestPassword123! |
