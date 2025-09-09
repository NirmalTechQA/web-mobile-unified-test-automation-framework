package testrunner;

import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.CucumberOptions.SnippetType;
import org.testng.annotations.*;

import hooks.Hooks;
import utils.GenericWrappers;
import utils.SeWrappers;

@CucumberOptions(features = {"src/test/resources/features"}, glue = {"pages",
"hooks"}, monochrome = true, dryRun=false, snippets = SnippetType.CAMELCASE, plugin = {"pretty",
"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"})

public class TestRunnerWeb extends SeWrappers {

	/*
	 * mvn command
	 * clean test -Dsurefire.suiteXmlFiles=testng_web.xml -Dcucumber.filter.tags="@login1" -DautomationType="web"
	 */

	@BeforeMethod
	@Parameters({"executionType","browserName","version","platform","projectName","buildName","envUrl"})
	public void bm(String executionType,String browserName, @Optional("") String version, @Optional("") String platform, @Optional("") String projectName, @Optional("") String buildName, @Optional("") String testEnvUrl) {
		if(executionType.equalsIgnoreCase("remote"))
		{
			startApp(browserName, version, platform, true, testEnvUrl,projectName, buildName);

		}
		else if(executionType.equalsIgnoreCase("local"))
		{
			startApp(browserName,"https://google.com/");

		}
	}

	@AfterMethod(alwaysRun = true)
	public void am() throws InterruptedException {
		Thread.sleep(3000);
		closeAllBrowsers();
	}

}