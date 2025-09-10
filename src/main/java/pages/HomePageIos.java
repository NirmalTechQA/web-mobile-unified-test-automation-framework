package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.cucumber.java.en.Then;
import utils.SeWrappers;

/*
 * This is a sample Page class and the corresponding glue code for iOS platform. 
 * The locators values are not valid and need to be replaced with actual values.
 * The methods are also sample and need to be implemented as per the application under test.
 * The step definitions are also sample and need to be implemented as per the feature file.
 * The class extends GenericWrappers which contains the reusable methods for Appium.
 * The class uses PageFactory to initialize the elements.
 * The class uses Cucumber annotations for the step definitions.
 * The class uses TestNG assertions for validation.
 * The class uses try-catch blocks to handle exceptions.
 */

public class HomePageIos extends SeWrappers {
	
	@iOSXCUITFindBy(xpath = "//*[@name='This is your home']")
	private WebElement homePageLabel;

	@iOSXCUITFindBy(xpath = "//*[@type='home']")
	private WebElement homePageBtn;
	
	@iOSXCUITFindBy(xpath = "//*[@label=\"Automation\"]")
	private WebElement label;
	
	public HomePageIos() {
		PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
	}
	
	@Then("Verify App Home page is displayed in iOS")
	public HomePageIos verifyAppHomePageIsDisplayedInIOS() {
		try
		{
			Assert.assertTrue(eleIsDisplayed(homePageLabel));
			click(label);
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			Assert.fail();
		}
		return this;
	}
	
	
	
	

}
