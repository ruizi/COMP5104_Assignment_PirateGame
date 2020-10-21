@exampleTable
Feature: how to use a table

  @tag1
  Scenario Outline: exampleTableScenario
    Given I want to write a step with <name>
    When I check for the <value> in step
    Then I verify the <status> in step

    Examples:
      | name  | value | status |
      | Bob   | smith | status |
      | Tom   | ryan  | fail   |