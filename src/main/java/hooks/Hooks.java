package hooks;

import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;
import utils.GenericWrappers;
import utils.SeWrappers;

import org.openqa.selenium.OutputType;

public class Hooks extends GenericWrappers {
	
	public static String automationType, testEnvUrl="";

	@AfterStep
	public void afterEachStep(Scenario scenario) {
			if(automationType.equalsIgnoreCase("web"))
			{
				if (scenario.isFailed()) {
					byte[] screenshot = SeWrappers.getWebDriver().getScreenshotAs(OutputType.BYTES);
					scenario.attach(screenshot, "image/png", scenario.getId());
					SeWrappers.getWebDriver().executeScript("lambda-status=" + (scenario.isFailed() ? "failed" : "passed"));
			        System.out.println(SeWrappers.getWebDriver().getSessionId());
				}
				else if(! scenario.isFailed())
				{
					SeWrappers.getWebDriver().executeScript("lambda-status=" + (scenario.isFailed() ? "failed" : "passed"));
			        System.out.println(SeWrappers.getWebDriver().getSessionId());
				}

			}
			else if (automationType.equalsIgnoreCase("mobile"))
			{
				if (scenario.isFailed()) {
					byte[] screenshot = getDriver().getScreenshotAs(OutputType.BYTES);
					scenario.attach(screenshot, "image/png", scenario.getId());
					getDriver().executeScript("lambda-status=" + (scenario.isFailed() ? "failed" : "passed"));
			        System.out.println(getDriver().getSessionId());
				}
				else if(! scenario.isFailed())
				{
					getDriver().executeScript("lambda-status=" + (scenario.isFailed() ? "failed" : "passed"));
//			        System.out.println(SeWrappers.getWebDriver().getSessionId());
				}
			}
	}

	@BeforeStep
	public void updateName(Scenario scenario) throws InterruptedException {
		Thread.sleep(30);
		automationType=System.getProperty("automationType");
		System.out.println("Test type-->"+automationType);
		
		testEnvUrl=System.getProperty("envUrl");
		System.out.println("Test Url-->"+testEnvUrl);

		if(automationType.equalsIgnoreCase("web"))
		{
			SeWrappers.getWebDriver().executeScript("lambda-name=" + scenario.getName());	
		}
		else if (automationType.equalsIgnoreCase("mobile"))
		{
			getDriver().executeScript("lambda-name=" + scenario.getName());
		}
	}
}