@backgroundExample
Feature: Demonstrate Background
  I want to use this feature to demonstrate the background keyword.

  Background:
    Given we go to Google.ca

  @googleUo
  Scenario: Google uOttawa and view results
    Given uOttawa is entered into the search box
    When The search button is hit
    Then The results about uOttawa will be displayed

  @googleCarleton
  Scenario: Google Carleton and view results
    Given Carleton is entered into the search box
    When The search button is hit
    Then The results about Carleton will be displayed