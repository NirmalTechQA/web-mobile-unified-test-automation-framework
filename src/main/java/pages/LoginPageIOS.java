package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.cucumber.java.en.Given;
import utils.GenericWrappers;

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

public class LoginPageIOS extends GenericWrappers{

	@iOSXCUITFindBy(xpath="//*[@value='enter mobile number']")
	private WebElement mobileNo;

	@iOSXCUITFindBy(xpath="//*[contains(@value,'+')]")
	private WebElement countryCode;

	@iOSXCUITFindBy(xpath="//*[@label='Sign In']")
	private WebElement signInBtn;

	@iOSXCUITFindBy(xpath="//*[@type='Search']")
	private WebElement searchCountry;

	@iOSXCUITFindBy(xpath="//*[@type='INDIA']")
	private WebElement searchCountrytype;

	@iOSXCUITFindBy(xpath="//*[@type='+91']")
	private WebElement selectIndia;

	@iOSXCUITFindBy(xpath="//*[@type='home']")
	private WebElement otpText;

	@iOSXCUITFindBy(xpath="//*[@type=\"Verify\" and @type=\"*\"]")
	private WebElement verifyBtn;

	
	public LoginPageIOS() {
		try
		{
			PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
			if(! eleIsDisplayed(mobileNo))
			{
				throw new RuntimeException("Login page is not displayed");
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	
	@Given("Click on Sign In button in iOS")
	public LoginPageIOS clickOnSignInButtonInIOS() {
		try
		{
			if(eleIsDisplayed(signInBtn))
			{
				click(signInBtn);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			Assert.fail();
		}
		return this;
	}
	
	@Given("Enter the valid mobile number {string}")
	public LoginPageIOS enterTheValidMobileNumber(String validMobileNo) {
		try
		{
			if(eleIsDisplayed(mobileNo))
			{
				enterValue(mobileNo, validMobileNo);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			Assert.fail();
		}
		return this;
	}
	
	@Given("Select the Valid country code")
	public LoginPageIOS selectTheValidCountryCode() {
		try
		{
			if(eleIsDisplayed(countryCode))
			{
				click(countryCode);
				enterValue(searchCountry, "india");
				click(selectIndia);

			}

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			Assert.fail();
		}
		return this;
	}
	
	@Given("Enter the default OTP {string} in iOS")
	public LoginPageIOS enterTheDefaultOTPInIOS(String validOtp) {
		try
		{

				click(otpText);
				enterValue(otpText, validOtp,false);


		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			Assert.fail();
		}
		return this;
	}
	
	@Given("Click on Verify button")
	public HomePageIos clickOnVerifyButton() {
		try
		{
			if(verifyBtn.isEnabled())
			{
				click(verifyBtn);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			Assert.fail();
		}
		return new HomePageIos();
	}
	
	
}
