package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.cucumber.java.en.Then;
import utils.SeWrappers;

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
