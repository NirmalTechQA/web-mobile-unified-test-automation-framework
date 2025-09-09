package testrunner;

import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.CucumberOptions.SnippetType;
import org.testng.annotations.*;

import utils.GenericWrappers;
import utils.SeWrappers;

@CucumberOptions(features = {"src/test/resources/features"}, glue = {"pages",
"hooks"},monochrome = true, publish = true, dryRun=false, snippets = SnippetType.CAMELCASE, plugin = {"pretty",
"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"})

public class TestRunnerMobile extends SeWrappers {

/*
 * mvn command
 * clean test -Dsurefire.suiteXmlFiles=testng_lambdatest_mobile.xml -Dcucumber.filter.tags="@loginiOS1" -DautomationType="mobile"
 */
	@BeforeSuite
	public void bs() {

//		startAppiumServer();
	}

	@Parameters({"platformName", "deviceName", "udid", "appPackage", "appActivity", "automationName",
		"chromeDriverPort", "systemPort", "xcodeOrgId", "xcodeSigningId", "bundleId", "app", "mjpegServerPort",
	"wdaLocalPort", "projectName", "buildName"})
	@BeforeMethod
	public void bm( String platformName,  String deviceName, @Optional("") String udid, @Optional("") String appPackage,
			@Optional("") String appActivity, @Optional("") String automationName,
			@Optional("") String chromeDriverPort, @Optional("") String systemPort, @Optional("") String xcodeOrgId,
			@Optional("") String xcodeSigningId, @Optional("") String bundleId, @Optional("") String app,
			@Optional("") String mjpegServerPort, @Optional("") String wdaLocalPort, @Optional("") String projectName, @Optional("") String buildName) {

		launchApp(platformName, deviceName, udid, appPackage, appActivity, automationName, chromeDriverPort, systemPort,
				xcodeOrgId, xcodeSigningId, bundleId, app, mjpegServerPort, wdaLocalPort, projectName, buildName);
	}

	@AfterMethod(alwaysRun = true)
	public void am() {
		closeApp();
	}

	@AfterSuite
	public void as() {
//		stopAppiumServer();
	}
}