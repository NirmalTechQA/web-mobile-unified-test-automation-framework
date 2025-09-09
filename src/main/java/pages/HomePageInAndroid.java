package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.cucumber.java.en.Then;
import utils.SeWrappers;

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
