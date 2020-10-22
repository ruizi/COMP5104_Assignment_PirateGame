package com.rui.pirate.cucumber;

import com.rui.pirate.testForPart1;
import com.rui.pirate.testForPart2;
import cucumber.api.CucumberOptions;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import cucumber.api.junit.Cucumber;


@RunWith(Cucumber.class)
@CucumberOptions(features = {"src/test/java/com/rui/pirate/cucumber/Level1a.feature", "src/test/java/com/rui/pirate/cucumber/Level1b.feature", "src/test/java/com/rui/pirate/cucumber/Level2.feature"})
@Suite.SuiteClasses({

})

public class testSuite {

}
