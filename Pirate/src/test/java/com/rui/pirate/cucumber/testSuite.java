package com.rui.pirate.cucumber;

import com.rui.pirate.testForPart1;
import com.rui.pirate.testForPart2;
import cucumber.api.CucumberOptions;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import cucumber.api.junit.Cucumber;


@RunWith(Cucumber.class)
@CucumberOptions(features = {"src/test/java/com/rui/pirate/cucumber/basicDyingAndScoring.feature", "src/test/java/com/rui/pirate/cucumber/advancedScoring.feature", "src/test/java/com/rui/pirate/cucumber/networkedGameTest.feature"})
@Suite.SuiteClasses({

})

public class testSuite {
}
