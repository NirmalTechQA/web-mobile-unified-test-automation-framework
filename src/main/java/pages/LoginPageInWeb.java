package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import utils.SeWrappers;
import org.testng.Assert;


public class LoginPageInWeb extends SeWrappers{
	
	@FindBy(xpath = "//input[@placeholder='Enter Email']")
	private WebElement enterEmail;
	
	@FindBy(xpath = "//span[text()='Request OTP']")
	private WebElement requestOTP;
	
	@FindBy(xpath = "//input[@placeholder='Enter OTP']")
	private WebElement enterOtp;
	
	@FindBy(xpath = "//span[text()='Submit OTP']")
	private WebElement submitOTP;

	
	public LoginPageInWeb() {
		try
		{
			PageFactory.initElements(new AppiumFieldDecorator(getWebDriver()), this);
			if(! elementExist(enterEmail))
			{
				throw new RuntimeException("Login page is not displayed");
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	@Given("Enter the valid email {string}")
	public LoginPageInWeb enterEmailAddress(String email)
	{
		try
		{
			typeText(enterEmail, email);
			pollingWait(requestOTP, explicitTimeOut);
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			System.out.println("Problem while entering email");
		}
		return this;
	}
	
	@And("Click on Request OTP")
	public LoginPageInWeb clickRequestOTP()
	{
		try
		{
			clickAction(requestOTP);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			System.out.println("Problem while clicking Request OTP");
		}
		return this;
	}
	
	@Given("Enter the OTP {string}")
	public LoginPageInWeb enterOTP(String Otp)
	{
		try
		{
			pollingWait(enterOtp, explicitTimeOut);
			typeText(enterOtp, Otp);
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			System.out.println("Problem while entering OTP");
		}
		return this;
	}
	
	@And("Click on Submit OTP")
	public HomePageInWeb clickSubmitOTP()
	{
		try
		{
			clickAction(submitOTP);
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			System.out.println("Problem while clicking Request OTP");
			Assert.fail();
		}
		
		return new HomePageInWeb();
	}
	
}