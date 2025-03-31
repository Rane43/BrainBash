@tag
Feature: Quiz Play
  @tag1
  Scenario: Success - Successfully play game
		Given I am logged in as a quizzer
		And I am on the homepage
		When I click play on a quiz card
		And I answer all the questions (either right or wrong)
		Then a message appears detailing my best score so far and my score for the game I just played.
	
	