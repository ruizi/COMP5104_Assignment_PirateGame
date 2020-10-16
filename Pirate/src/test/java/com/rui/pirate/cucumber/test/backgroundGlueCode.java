package com.rui.pirate.cucumber.test;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class backgroundGlueCode {
    int number = 0;

    @Given("we go to Google.ca")
    public void we_go_to_Google_ca() {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("Go to Google.ca");
        number += 1;
    }

    @Given("uOttawa is entered into the search box")
    public void uottawa_is_entered_into_the_search_box() {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("uOttawa is entered into the search box");
        number += 1;
    }

    @When("The search button is hit")
    public void the_search_button_is_hit() {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("The search button is hit");
        number += 1;
    }

    @Then("The results about uOttawa will be displayed")
    public void the_results_about_uOttawa_will_be_displayed() {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("The results about uOttawa will be displayed");
        if (number == 3) {
            System.out.println("right");
        }
    }

    @Given("Carleton is entered into the search box")
    public void carleton_is_entered_into_the_search_box() {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("Carleton is entered into the search box");
        number += 2;
    }


    @Then("The results about Carleton will be displayed")
    public void the_results_about_Carleton_will_be_displayed() {
        // Write code here that turns the phrase above into concrete actions
        System.out.println("The results about Carleton will be displayed");
        if (number == 4) {
            System.out.println("right");
        }
    }
}
