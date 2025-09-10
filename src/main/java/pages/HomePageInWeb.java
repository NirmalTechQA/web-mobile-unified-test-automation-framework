package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.cucumber.java.en.Then;
import utils.SeWrappers;

/*
 * This is a sample Page class and the corresponding glue code for Web. 
 * The locators values are not valid and need to be replaced with actual values.
 * The methods are also sample and need to be implemented as per the application under test.
 * The step definitions are also sample and need to be implemented as per the feature file.
 * The class extends SeWrappers which contains the reusable methods for Appium.
 * The class uses PageFactory to initialize the elements.
 * The class uses Cucumber annotations for the step definitions.
 * The class uses TestNG assertions for validation.
 * The class uses try-catch blocks to handle exceptions.
 */

public class HomePageInWeb extends SeWrappers {
	
	@FindBy(xpath = "//span[text()='Home']")
	private WebElement homePage;
	
	public HomePageInWeb() {
		PageFactory.initElements(new AppiumFieldDecorator(getWebDriver()), this);
	}
	
	@Then("Verify Home page is displayed")
	public HomePageInWeb verifyHomePage()
	{
		Assert.assertTrue(isElementVisible(homePage, explicitTimeOut));
		return this;
	}
	
	
	

}
