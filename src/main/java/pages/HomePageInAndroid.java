package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.cucumber.java.en.Then;
import utils.SeWrappers;

/*
 * This is a sample Page class and the corresponding glue code for Android platform. 
 * The locators values are not valid and need to be replaced with actual values.
 * The methods are also sample and need to be implemented as per the application under test.
 * The step definitions are also sample and need to be implemented as per the feature file.
 * The class extends GenericWrappers which contains the reusable methods for Appium.
 * The class uses PageFactory to initialize the elements.
 * The class uses Cucumber annotations for the step definitions.
 * The class uses TestNG assertions for validation.
 * The class uses try-catch blocks to handle exceptions.
 */

public class HomePageInAndroid extends SeWrappers {
	
	@FindBy(xpath = "//*[@resource-id='home']")
	private WebElement homeButton;
	
	@FindBy(xpath = "//*[@resource-id='search']")
	private WebElement search;
	
	public HomePageInAndroid() {
		PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
	}
	
	@Then("Verify App Home page is displayed")
	public HomePageInAndroid verifyAppHomePageIsDisplayed() {
		try
		{
			pollingWaitForApp(homeButton, 20);
			pollingWaitForApp(search, 20);
			click(search);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return this;
	}
	@Then("Click on App Home page in android")
	public void clickOnAppHomePageInAndroid() {
		try
		{
			scrollToElemenForApp(homeButton);
			click(homeButton);
		}catch(Exception ex)
		{
			ex.printStackTrace();
			Assert.fail();
		}
	}
	
	

}
