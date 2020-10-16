package com.rui.pirate.cucumber.test;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class glueCodeImplementation {
    int number = 0;

    @Given("I want to write a step with precondition")
    public void i_want_to_write_a_step_with_precondition() {
        // Write code here that turns the phrase above into concrete actions
        number += 1;
    }

    @When("I complete action")
    public void i_complete_action() {
        // Write code here that turns the phrase above into concrete actions
        number += 2;
    }

    @Then("I validate the outcomes")
    public void i_validate_the_outcomes() {
        // Write code here that turns the phrase above into concrete actions
        if(number==3){
            System.out.println("correct answer");
        }

    }

}
