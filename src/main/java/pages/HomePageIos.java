package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.cucumber.java.en.Then;
import utils.SeWrappers;

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
