package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import utils.GenericWrappers;

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

public class LoginPageInAndroid extends GenericWrappers{
	
	@AndroidFindBy(xpath="//*[@resource-id='android:id/button1']")
	private WebElement allowBtn;
	
	@AndroidFindBy(id="com.home:id/authPhone")
	private WebElement mobileNo;
	
	@AndroidFindBy(id="com.home:id/countryCode")
	private WebElement countryCode;
	
	@AndroidFindBy(xpath="//*[@resource-id='android:id/text1' and @text='+91']")
	private WebElement indiaCountryCode;
	
	@AndroidFindBy(id="com.home:id/authSubmit")
	private WebElement loginBtn;

	@AndroidFindBy(id="com.home:id/authOtp")
	private WebElement otp;
	
	@AndroidFindBy(xpath="//*[@resource-id='com.home:id/authSubmit']")
	private WebElement submitBtn;

	
	public LoginPageInAndroid() {
		try
		{
			PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
			if(! eleIsDisplayed(allowBtn))
			{
				throw new RuntimeException("Login page is not displayed");
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	@Given("Accept the app permission popup")
	public void acceptTheAppPermissionPopup() {
		try
		{
			if(eleIsDisplayed(allowBtn))
			{
				click(allowBtn);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			Assert.fail();
		}
	}
	
	@Given("Enter the valid mobile number {string} along with country code")
	public LoginPageInAndroid enterTheValidMobileNumberAlongWithCountryCode(String mobileNumber) {
		try
		{
			enterValue(mobileNo, mobileNumber);
			swipeDownInAppWithWebElement(indiaCountryCode);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			Assert.fail();
		}
	
		return this;
	}
	@And("Click on Login button")
	public LoginPageInAndroid clickOnLoginButton() {
		try
		{
			if(eleIsDisplayed(loginBtn))
			{
				click(loginBtn);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	
		return this;
	}
	@Given("Enter the default OTP {string}")
	public LoginPageInAndroid enterTheDefaultOTP(String otpValue) {
		try
		{
			if(eleIsDisplayed(otp))
			{
				enterValue(otp, otpValue);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			Assert.fail();
		}
	
		return this;
	}
	@And("Click on Submit button")
	public LoginPageInAndroid clickOnSubmitButton() {
		try
		{
				eleIsDisplayed(submitBtn);
				
				if(eleIsDisplayed(otp))
				{
					click(submitBtn);
				}
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			Assert.fail();
		}
		return this;
	}

	
}