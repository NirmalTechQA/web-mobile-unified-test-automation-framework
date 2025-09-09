package utils;



import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import hooks.Hooks;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

/**
 * This class provides wrappers for Selenium WebDriver methods to handle common automation tasks.
 */


public class SeWrappers extends GenericWrappers {

	protected String browser = null;
	public WebDriverWait wait;
	public static RemoteWebDriver driver1;
	public static ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<RemoteWebDriver>();
	protected Properties prop;
	public static boolean browserVal=false;
	public static String sHubUrl,sHubPort,localEnvUrl,browserName, timeStamp;
	public final int implicitTimeout=20;
	public final int explicitTimeOut=15;
	
	public static String userName = System.getenv("LT_USERNAME") == null ? "" // Add username here
            : System.getenv("LT_USERNAME");
    public static String accessKey = System.getenv("LT_ACCESS_KEY") == null ? "" // Add accessKey here
            : System.getenv("LT_ACCESS_KEY");
    public static String webGridUrl = System.getenv("LT_GRID_URL") == null ? "" : System.getenv("LT_GRID_URL");


	/**
	 * Constructor for the SeWrappers class.
	 * Initializes properties and loads configuration values.
	 */

	public SeWrappers() {
		prop = new Properties();
		try {
			// Load configuration values from config.properties file

			prop.load(new FileInputStream(new File(System.getProperty("user.dir")+"/src/main/resources/config.properties")));
			sHubUrl = prop.getProperty("HUB");
			sHubPort = prop.getProperty("PORT");
			browserName = prop.getProperty("browser");


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Launches the browser and navigates to the specified environment.
	 * 
	 * @param browser The browser to be launched (e.g., Chrome, Firefox).
	 * @param bRemote Flag indicating whether to run tests remotely (true) or locally (false).
	 * @param env The URL of the environment to navigate to.
	 */


	public void startApp(String browser, String version, String platform, boolean bRemote, String env, String projectName, String buildName) {
		try {
			timeStamp=getTime();
			
			// this is for grid run
			if(bRemote)
			{

				try {
					
					/*
					 * This integration for lambdatest
					 */

					DesiredCapabilities webCapability = new DesiredCapabilities();
					webCapability.setCapability(CapabilityType.BROWSER_NAME, browser);
					webCapability.setCapability(CapabilityType.BROWSER_VERSION, version);
					webCapability.setCapability(CapabilityType.PLATFORM_NAME, platform);
					
					HashMap<String, Object> ltOptions = new HashMap<String, Object>();
					ltOptions.put("project", projectName+"_"+timeStamp);
					ltOptions.put("build", buildName+"_"+timeStamp);
					/*the below capability is for static test name. However we getting the scenario name dynamically from feature name in Hooks
					ltOptions.put("name", "Web Test");
					*/
					
					HashSet<String> uploadedfiles = new HashSet<>();
					uploadedfiles.add("testImage.jpg");
					
					ltOptions.put("plugin", "java-java");
					ltOptions.put("devicelog", true);
					ltOptions.put("network", true);
					ltOptions.put("video", true);
					ltOptions.put("visual", true);
					ltOptions.put("javascriptEnabled", true); // Make sure JavaScript is enabled
					ltOptions.put("lambda:userFiles",uploadedfiles);


					webCapability.setCapability("LT:Options", ltOptions);
					
					String webLambdaTestUrl = "https://" + userName + ":" + accessKey + "@" + webGridUrl + "/wd/hub";
					System.out.println("lambdaTestUrl Web-->"+webLambdaTestUrl);

					
					//To execute in LambdaTest env
					driver.set(new RemoteWebDriver(new URL(webLambdaTestUrl), webCapability));

					/*
					 * This is for remote run

					//Define desired capabilities
					DesiredCapabilities cap=new DesiredCapabilities();
					cap.setCapability("browserName", "Chrome");

					ChromeOptions chromeOptions = new ChromeOptions();
					cap.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

					sHubUrl=System.getProperty("hubService");
					System.out.println("Hub Url: "+sHubUrl+":"+sHubPort+"/wd/hub");
					driver.set( new RemoteWebDriver(new URL(sHubUrl+":"+sHubPort+"/wd/hub"), cap));
					*
					*/
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			else{ // this is for local run

				try
				{

					if(browser.equalsIgnoreCase("chrome")){
						try
						{
							ChromeOptions chromeOptions = new ChromeOptions();
							chromeOptions.addArguments("--disable-notifications");
							chromeOptions.addArguments("--remote-allow-origins=*");

							System.setProperty("webdriver.chrome.silentOutput", "true");
							driver.set(new ChromeDriver(chromeOptions));
							getWebDriver().manage().deleteAllCookies();
							getWebDriver().get("chrome://settings/clearBrowserData");
							getWebDriver().findElement(By.xpath("//settings-ui")).sendKeys(Keys.ENTER);
							System.out.println("Launched chrome");
							browserVal=true;
						}
						catch(Exception ex)
						{
							ex.printStackTrace();
						}
					}
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
			getWebDriver().manage().window().maximize();
			getWebDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitTimeout));
			localEnvUrl=env;
			getWebDriver().get(env);

		} catch (WebDriverException e) {
			System.out.println("The browser: "+browser+" could not be launched");
			browserVal=false;
			e.printStackTrace();
		}
	}


	/**
	 * Overloaded method to start the browser and navigate to the specified environment locally.
	 * 
	 * @param browser The browser to be launched (e.g., Chrome, Firefox).
	 * @param env The URL of the environment to navigate to.
	 */
	public void startApp(String browser, String env) {
		startApp(browser,"","", false, env,"","");
	}

	/**
	 * Returns the WebDriver instance.
	 * 
	 * @return The WebDriver instance.
	 */
	public static synchronized RemoteWebDriver getWebDriver() {
		return driver.get();
	}

	/**
	 * Types text into the specified WebElement reliably.
	 * 
	 * @param element The WebElement to type into.
	 * @param msg The text to be typed.
	 */
	public void typeText(WebElement element, String msg) {
	    try {
	        pollingWait(element, explicitTimeOut);
	        
	        if (element.isEnabled()) {
	            System.out.println("Element is enabled for interaction");
	            Thread.sleep(500);

	            // Ensure the element is cleared before typing
	            try {
	                System.out.println("JS Clear (Ensures no placeholder remains)");
	                JavascriptExecutor js = (JavascriptExecutor) getWebDriver();
	                js.executeScript("arguments[0].value=''; arguments[0].dispatchEvent(new Event('input')); arguments[0].dispatchEvent(new Event('change'));", element);
	                Thread.sleep(500);
	            } catch (Exception ex) {
	                System.out.println("Selenium Clear (Fallback)");
	                element.sendKeys(Keys.CONTROL + "a");
	                element.sendKeys(Keys.BACK_SPACE);
	                Thread.sleep(500);
	            }

	            // Ensure placeholder is removed before typing
	            Thread.sleep(500);
	            
	            // Type text normally
	            try {
	                element.sendKeys(msg);
	            } catch (ElementNotInteractableException ex) {
	                JavascriptExecutor js = (JavascriptExecutor) getWebDriver();
	                js.executeScript("arguments[0].value='" + msg + "'; arguments[0].dispatchEvent(new Event('input')); arguments[0].dispatchEvent(new Event('change'));", element);
	            }
	        }
	    } catch (Exception ex) {
	        System.out.println("Failed to type in WebElement " + element);
	        ex.printStackTrace();
	    }
	}

	/**
	 * Waits explicitly for the specified WebElement to become clickable, visible, and refreshed.
	 * 
	 * @param element The WebElement to wait for.
	 */
	public void waitForMe(WebElement element)
	{
		try
		{
			wait = new WebDriverWait(getWebDriver(), Duration.ofSeconds(explicitTimeOut));
			wait.until(ExpectedConditions.elementToBeClickable(element));
			wait.until(ExpectedConditions.visibilityOf(element));
			wait.until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(element))); 
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

	}

	/**
	 * Waits explicitly for the specified WebElement to become clickable, visible, and refreshed with a specified timeout.
	 * 
	 * @param element The WebElement to wait for.
	 * @param timeOut The timeout value in seconds.
	 */
	public void waitForMeWithTimeout(WebElement element,int timeOut)
	{
		try
		{
			wait = new WebDriverWait(getWebDriver(), Duration.ofSeconds(timeOut));
			wait.until(ExpectedConditions.elementToBeClickable(element));
			wait.until(ExpectedConditions.visibilityOf(element));
			wait.until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(element))); 
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

	}

	/**
	 * Waits explicitly for two WebElements to become clickable and visible.
	 * 
	 * @param first The first WebElement to wait for.
	 * @param second The second WebElement to wait for.
	 */
	public void waitForMe(WebElement first, WebElement second)
	{
		try
		{
			wait = new WebDriverWait(getWebDriver(), Duration.ofSeconds(explicitTimeOut));
			wait.until(ExpectedConditions.elementToBeClickable(first));
			wait.until(ExpectedConditions.visibilityOf(first));

			wait.until(ExpectedConditions.elementToBeClickable(second));
			wait.until(ExpectedConditions.visibilityOf(second));
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

	}

	/**
	 * Clicks on the specified WebElement with polling wait and handles ElementNotInteractableException by clicking via JavaScript if necessary.
	 * 
	 * @param element The WebElement to click.
	 * @throws InterruptedException If the current thread is interrupted while sleeping.
	 */
	public void clickAction(WebElement element) throws InterruptedException {

		try {
			pollingWait(element,explicitTimeOut);
			element.click();
		}
		catch(ElementNotInteractableException ex)
		{
			Thread.sleep(3000);
			clickJavaScriptElement(element);
		}
		catch (Exception ex) {
			System.out.println("Failed to click the Webelement "+element);
			ex.printStackTrace();
		}
	}

	/**
	 * Verifies if clicking on the specified WebElement is successful with polling wait.
	 * 
	 * @param element The WebElement to verify click action.
	 * @return true if clicking is successful, false otherwise.
	 * @throws InterruptedException If the current thread is interrupted while sleeping.
	 */
	public boolean verifyClickAction(WebElement element) throws InterruptedException {
		boolean res= true;
		try {
			pollingWait(element, explicitTimeOut);
			element.click();

		}
		catch(ElementNotInteractableException ex)
		{
			res = false;
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return res;
	}

	/**
	 * Gets the text of the specified WebElement with polling wait.
	 * 
	 * @param element The WebElement to retrieve the text from.
	 * @return The text of the WebElement.
	 */
	public String getText(WebElement element) {

		String eleText="";
		try
		{
			pollingWait(element, explicitTimeOut);
			eleText=element.getText();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return eleText;
	}

	/**
	 * Compares the text of two WebElements, ignoring case sensitivity.
	 * 
	 * @param e1 The first WebElement for comparison.
	 * @param e2 The second WebElement for comparison.
	 * @return true if the text of both WebElements is equal, ignoring case sensitivity, false otherwise.
	 */
	public boolean compareTwoWebElements(WebElement e1, WebElement e2)
	{
		boolean retVal = false;
		try
		{
			String firstElement=e1.getText().toLowerCase();
			String secondElement=e2.getText().toLowerCase();
			if(firstElement.contentEquals(secondElement))
			{
				retVal=true;
			}
		}
		catch(Exception ex)
		{
			retVal=false;
		}
		return retVal;
	}

	/**
	 * Takes a screenshot of the current WebDriver instance.
	 * 
	 * @param driver The WebDriver instance.
	 * @param screenshotName The name of the screenshot file.
	 * @return The path to the captured screenshot.
	 * @throws Exception if an error occurs during the screenshot capture process.
	 */
	public static String getScreenshot(WebDriver driver, String screenshotName) throws Exception {

		String destination="";
		File finalDestination =null;
		try {
			String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
			TakesScreenshot ts = (TakesScreenshot) driver;
			File source = ts.getScreenshotAs(OutputType.FILE);

			screenshotName=screenshotName.replaceAll("\\s+", "");
			destination = "./reports/images/" + screenshotName + dateName
					+ ".jpg";
			finalDestination = new File(destination);
			FileUtils.copyFile(source, finalDestination);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		// Returns the captured file path
		return destination;

	}

	/**
	 * Retrieves the current URL of the WebDriver instance.
	 * 
	 * @return The current URL as a String, or an empty String if an exception occurs.
	 */
	public String getCurrentUrl() {

		String currentURL="";
		try
		{
			currentURL= getWebDriver().getCurrentUrl();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return currentURL;

	}

	/**
	 * Checks if the specified WebElement exists and is displayed.
	 * 
	 * @param ele The WebElement to check for existence and visibility.
	 * @return true if the WebElement exists and is displayed, false otherwise.
	 */
	public boolean elementExist(WebElement ele) {
		try {
			pollingWait(ele,explicitTimeOut);
			ele.isDisplayed();
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * Navigates to the specified URL.
	 * 
	 * @param url The URL to navigate to.
	 */
	public void navigateToUrl(String url)
	{
		try
		{
			System.out.println(url);
			getWebDriver().navigate().to(url);
			Thread.sleep(5000);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Gets the attribute value of the specified WebElement.
	 * 
	 * @param ele The WebElement.
	 * @param attribute The name of the attribute to retrieve.
	 * @return The value of the specified attribute.
	 */

	public String getAttribute(WebElement ele, String attribute) {
		String bReturn = "";
		try {
			waitForMe(ele);
			bReturn=  ele.getAttribute(attribute);

		} catch (WebDriverException ex) {
			ex.printStackTrace();
		}
		return bReturn;
	}

	/**
	 * Selects an option in a dropdown by index.
	 * 
	 * @param element The dropdown WebElement.
	 * @param indexer The index of the option to select.
	 */
	public void selectDropDownUsingIndex(WebElement element, int indexer) {
		try
		{
			waitForMe(element);
			Select sel = new Select(element);
			sel.selectByIndex(indexer);
		}
		catch (WebDriverException ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * Selects an option in a dropdown by visible text.
	 * 
	 * @param element     The dropdown WebElement.
	 * @param visibleText The visible text of the option to select.
	 */
	public void selectDropDownUsingVisibleText(WebElement element, String visibleText) {
		try
		{
			pollingWait(element, explicitTimeOut);
			Select sel = new Select(element);
			sel.selectByVisibleText(visibleText);
		}
		catch (WebDriverException ex) {
			ex.printStackTrace();		
		}

	}

	/**
	 * Selects an option in a dropdown by value.
	 * 
	 * @param element The dropdown WebElement.
	 * @param value   The value attribute of the option to select.
	 */
	public void selectDropDownUsingValue(WebElement element, String value) {
		try
		{
			pollingWaitWithoutClickable(element,explicitTimeOut);
			Select sel = new Select(element);
			sel.selectByValue(value);
		}
		catch (WebDriverException ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * Retrieves the title of the current web page.
	 * 
	 * @return The title of the current web page.
	 */
	public String getTitle() {
		String title = "";
		try {
			title =  getWebDriver().getTitle();
		} catch (WebDriverException ex) {
			ex.printStackTrace();
		}
		return title;
	}

	/**
	 * Verifies if a WebElement is selected.
	 * 
	 * @param ele The WebElement to verify.
	 * @return True if the WebElement is selected, false otherwise.
	 */
	public boolean verifySelected(WebElement ele) {
		boolean bReturn =false;

		try {
			waitForMe(ele);
			if(ele.isSelected()) {
				bReturn=true;
			} else {
				bReturn=false;
			}
		} catch (WebDriverException ex) {
			bReturn=false;
			ex.printStackTrace();
		}
		return bReturn;
	}

	/**
	 * Verifies if a WebElement is enabled.
	 * 
	 * @param ele The WebElement to verify.
	 * @return True if the WebElement is enabled, false otherwise.
	 */
	public boolean verifyEnabled(WebElement ele) {
		boolean retVal=false;
		try {
			if(ele.isEnabled()) {
				retVal=true;

			} else {
				retVal=false;
			}
		} catch (WebDriverException e) {
			retVal=false;
		}
		return retVal;
	}

	/**
	 * Switches to a new window opened and returns the current window handle.
	 * 
	 * @return The current window handle before switching.
	 */
	public String switchWindows() {
		String currentWindowHandle="";
		try {
			// Store the current window handle
			currentWindowHandle = getWebDriver().getWindowHandle();
			// Switch to new window opened

			Set<String> allWindows= getWebDriver().getWindowHandles();
			for (String winHandle : allWindows) {
				if (!winHandle.equals(currentWindowHandle) ) {
					getWebDriver().switchTo().window(winHandle);
					System.out.println("Successfully traversed to the required window");
				}
			}
		}
		catch(Exception ex)
		{
			System.out.println("Problem while traversing to the required window");
		}

		return currentWindowHandle;
	}

	/**
	 * Closes the current window and switches to the parent window.
	 * 
	 * @param parentWindow The window handle of the parent window.
	 */
	public void closeCurrentWindowAndSwitchParentWindow(String parentWindow) {

		try
		{
			// Perform the actions on new window
			// Close the new window, if that window no more required
			getWebDriver().close();
			// Switch back to original browser (first window)
			getWebDriver().switchTo().window(parentWindow);
			System.out.println("Switched to parent window successfully");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();

		}
	}

	/**
	 * Switches to a window by its index in the list of window handles.
	 * 
	 * @param index The index of the window handle to switch to.
	 */
	public void switchWindowByIndex(int index)
	{
		try
		{
			Set<String> allWindowHandles=getWebDriver().getWindowHandles();
			List<String> allWindows=new ArrayList<String>();
			allWindows.addAll(allWindowHandles);
			getWebDriver().switchTo().window(allWindows.get(index));
		}
		catch (NoSuchWindowException ex) {
			ex.printStackTrace();
		} catch (WebDriverException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Switches the driver's context to the frame containing the given web element.
	 * 
	 * @param ele The web element whose frame to switch to.
	 */
	public void switchToFrameByWebElement(WebElement ele)
	{
		try
		{
			pollingWaitWithoutClickable(ele, explicitTimeOut);
			getWebDriver().switchTo().frame(ele);
			System.out.println("Frame");
		}
		catch (NoSuchFrameException ex) {
			ex.printStackTrace();
		} catch (WebDriverException ex) {
			ex.printStackTrace();

		}
	}

	/**
	 * Switches the driver's context to the frame with the specified name or ID.
	 * 
	 * @param frameName_ID The name or ID of the frame to switch to.
	 */
	public void switchToFrameByName(String frameName_ID)
	{
		try
		{
			getWebDriver().switchTo().frame(frameName_ID);
		}
		catch (NoSuchFrameException ex) {
			ex.printStackTrace();
		} catch (WebDriverException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Switches the driver's context to the frame with the specified index.
	 * 
	 * @param index The index of the frame to switch to.
	 */
	public void switchToFrameByIndex(int index)
	{
		try
		{
			getWebDriver().switchTo().frame(index);
		}
		catch (NoSuchFrameException ex) {
			ex.printStackTrace();
		} catch (WebDriverException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Exists out of the frame
	 * 
	 */
	public void exitFrame()
	{
		try
		{
			getWebDriver().switchTo().defaultContent();
		}
		catch (NoSuchFrameException ex) {
			ex.printStackTrace();
		} catch (WebDriverException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Exists from the current frame and traverses to parent frame
	 * 
	 */
	public void goToParentFrame()
	{
		try
		{
			getWebDriver().switchTo().parentFrame();
		}
		catch (NoSuchFrameException ex) {
			ex.printStackTrace();
		} catch (WebDriverException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Accepts the currently displayed alert.
	 */
	public void acceptAlert() {
		try {
			Alert alert = getWebDriver().switchTo().alert();
			alert.accept();
		} catch (NoAlertPresentException ex) {
			ex.printStackTrace();
		} catch (WebDriverException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Dismisses the currently displayed alert.
	 */
	public void dismissAlert() {
		try {
			Alert alert = getWebDriver().switchTo().alert();
			alert.dismiss();
		} catch (NoAlertPresentException ex) {
			ex.printStackTrace();
		} catch (WebDriverException ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * Gets the text of the currently displayed alert.
	 * 
	 * @return The text of the currently displayed alert.
	 */
	public String getAlertText() {
		String alertText = "";
		try {
			Alert alert = getWebDriver().switchTo().alert();
			alertText = alert.getText();
		} catch (NoAlertPresentException ex) {
			ex.printStackTrace();
		} catch (WebDriverException ex) {
			ex.printStackTrace();
		}
		return alertText;
	}

	/**
	 * Closes the current browser window.
	 */
	public void closeBrowser() {
		try {
			getWebDriver().close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Closes all browser windows and quits the WebDriver session.
	 */
	public void closeAllBrowsers() {
		try {
			getWebDriver().quit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Moves the mouse cursor to the specified web element.
	 * 
	 * @param HovertoWebElement The web element to hover over.
	 */
	public static void hoverWebelement(WebElement HovertoWebElement)
	{
		try
		{
			Actions builder = new Actions(getWebDriver());
			builder.moveToElement(HovertoWebElement).perform();
			Thread.sleep(1000);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Scrolls to the specified web element.
	 * 
	 * @param ScrolltoThisElement The web element to scroll to.
	 */
	public  void scrollToElement(WebElement ScrolltoThisElement) {
		try {

			isElementVisible(ScrolltoThisElement,explicitTimeOut);
			Actions act = new Actions(getWebDriver());
			act.moveToElement(ScrolltoThisElement).build().perform();

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Performs a double click action on the specified web element.
	 * 
	 * @param driver The WebDriver instance.
	 * @param ele    The web element to double click.
	 */
	public void doubleClick(WebDriver driver, WebElement ele)
	{
		try
		{
			pollingWait(ele,explicitTimeOut);
			Actions doubleClk = new Actions(driver);
			doubleClk.moveToElement(ele).doubleClick().build().perform();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Performs a right-click action on the specified web element.
	 * 
	 * @param driver The WebDriver instance.
	 * @param ele    The web element to right click.
	 */
	public void rightClick(WebDriver driver, WebElement ele)
	{
		try
		{
			pollingWait(ele,explicitTimeOut);
			Actions rightClk = new Actions(driver);
			rightClk.moveToElement(ele).contextClick().build().perform();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Performs a drag-and-drop action using click-and-hold on the drag element and moves to the drop element.
	 * 
	 * @param driver      The WebDriver instance.
	 * @param dragElement The web element to drag.
	 * @param dropElement The web element to drop.
	 */
	public void dragDropUsingClickAndHold(WebDriver driver, WebElement dragElement,WebElement dropElement)
	{
		try
		{
			waitForMe(dragElement, dropElement);
			Actions dragDrop = new Actions(driver);
			dragDrop.clickAndHold(dragElement).moveToElement(dropElement).build().perform();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Moves the mouse cursor to the submenu.
	 * 
	 * @param driver  The WebDriver instance.
	 * @param menu    The menu web element.
	 * @param subMenu The submenu web element.
	 */
	public void moveToSubMenu(WebDriver driver, WebElement menu, WebElement subMenu)
	{
		try
		{
			waitForMe(menu, subMenu);
			Actions navigateSubMenu = new Actions(driver);
			navigateSubMenu.moveToElement(menu).moveToElement(subMenu).build().perform();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Performs a drag-and-drop action from the drag element to the drop element.
	 * 
	 * @param driver      The WebDriver instance.
	 * @param dragElement The web element to drag.
	 * @param dropElement The web element to drop.
	 */
	public void dragDrop(WebDriver driver, WebElement dragElement,WebElement dropElement)
	{
		try
		{
			waitForMe(dragElement, dropElement);
			Actions dragDrop = new Actions(driver);
			dragDrop.dragAndDrop(dragElement,dropElement).build().perform();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Clicks on the specified WebElement using JavaScript.
	 * 
	 * @param element The WebElement to click.
	 * @throws InterruptedException if the thread is interrupted while waiting.
	 */

	public void clickJavaScriptElement(WebElement element) throws InterruptedException {
		try {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
			executor.executeScript("arguments[0].click()", element);
		} catch (ElementNotInteractableException ex) {
			Thread.sleep(3000);
			System.out.println("Unable to click for the first time and trying one more time");
			element.click();
		} catch (Exception ex) {
			element.click();
			System.out.println("Failed to click the Javascript Webelement");
		}
	}

	/**
	 * Clicks on a disabled web element using JavaScript.
	 * 
	 * @param element The web element to click.
	 * @throws InterruptedException If the thread is interrupted.
	 */
	public void clickDisbledJavaScriptElement(WebElement element) throws InterruptedException {
		try {
			JavascriptExecutor executor = (JavascriptExecutor) getWebDriver();
			executor.executeScript("arguments[0].removeAttribute(\"disabled\");", element);
			executor.executeScript("arguments[0].click()", element);
		} catch (ElementNotInteractableException ex) {
			Thread.sleep(3000);
			System.out.println("Unable to click for the first time and trying one more time");
			element.click();
		} catch (Exception ex) {
			System.out.println("Failed to click the Javascript Webelement");
		}
	}

	/**
	 * Waits for the specified web element to be clickable and visible with a polling frequency and timeout.
	 * 
	 * @param element  The web element to wait for.
	 * @param timeOut  The maximum time to wait in seconds.
	 */
	public void pollingWait(WebElement element,int timeOut) {

		Wait<WebDriver> wait = new FluentWait<WebDriver>(getWebDriver()).withTimeout(Duration.ofSeconds(timeOut))
				.pollingEvery(Duration.ofSeconds(2)).ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class);
 		wait.until(ExpectedConditions.elementToBeClickable(element));
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	/**
	 * Waits for the URL to contain the specified value.
	 * 
	 * @param url The URL value to wait for.
	 * @return True if the URL contains the specified value within the timeout, false otherwise.
	 */
	public boolean waitForUrl(String url)
	{
		boolean retVal=false;
		try
		{
			wait = new WebDriverWait(getWebDriver(), Duration.ofSeconds(20));
			wait.until(ExpectedConditions.urlContains(url)); 
			retVal=true;
		}
		catch(Exception ex)
		{
			System.out.println("Continue CTA is taking more time to load the itinerary");
			ex.printStackTrace();
		}
		return retVal;

	}

	/**
	 * Waits for the specified web element to be visible with a polling frequency and timeout without requiring it to be clickable.
	 * 
	 * @param element The web element to wait for.
	 * @param timeOut The maximum time to wait in seconds.
	 */
	public void pollingWaitWithoutClickable(WebElement element,int timeOut) {

		Wait<WebDriver> wait = new FluentWait<WebDriver>(getWebDriver()).withTimeout(Duration.ofSeconds(timeOut))
				.pollingEvery(Duration.ofSeconds(2)).ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class);
		wait.until(ExpectedConditions.visibilityOf(element));

	}

	/**
	 * Performs a single click action on the specified web element using Actions class.
	 * 
	 * @param driver The WebDriver instance.
	 * @param ele    The web element to click.
	 */
	public void actionsClick(WebElement ele)
	{
		try
		{
			pollingWait(ele,explicitTimeOut);
			Actions singleClick = new Actions(getWebDriver());
			singleClick.moveToElement(ele).click().build().perform();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Types the specified text into the given web element using Actions class.
	 * 
	 * @param ele  The web element to type into.
	 * @param text The text to be typed.
	 */
	public void actionTypeText( WebElement ele,String text) {
		try {
			waitForMe(ele);
			Actions typeText = new Actions(getWebDriver());
			typeText.sendKeys(ele,text).build().perform();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Scrolls the page by the specified scroll value.
	 * 
	 * @param scrollValue The amount to scroll.
	 */
	public void scroll(int scrollValue)
	{
		try
		{
			JavascriptExecutor jse = (JavascriptExecutor)getWebDriver();
			jse.executeScript("scroll(0, "+scrollValue+");");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

	}

	/**
	 * Scrolls the page to bring the specified WebElement into view.
	 * 
	 * @param ScrolltoThisView The WebElement to scroll to.
	 */
	public void scrollToView(WebElement ScrolltoThisView)
	{
		try
		{
			JavascriptExecutor jse = (JavascriptExecutor)getWebDriver();
			jse.executeScript("arguments[0].scrollIntoView();",ScrolltoThisView );
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

	}

	/**
	 * Refreshes the current page.
	 */
	public void pageRefresh() {
		getWebDriver().navigate().refresh();
	}

	/**
	 * Verifies if the specified web element is disabled.
	 * 
	 * @param element The web element to verify.
	 * @return True if the element is disabled, otherwise false.
	 */
	public Boolean verifyIsDisabled(WebElement element) {
		Boolean disabled = false;

		try
		{
			disabled = (Boolean) getWebDriver().executeScript("return arguments[0].hasAttribute(\"disabled\");", element);
			System.out.println(element+" is disabled with value "+disabled);
			return disabled;
		}
		catch(Exception ex)
		{
			System.out.println("Unable to locate the disabled attribute in the webelement: "+element);
			ex.printStackTrace();
		}
		return disabled;


	}

	/**
	 * Opens the specified element in a new window using the keyboard shortcut CTRL+SHIFT+ENTER.
	 * 
	 * @param element The web element to open in a new window.
	 */
	public void openInNewWindow(WebElement element)
	{
		try
		{
			System.out.println(element);
			String keyString =   Keys.CONTROL+Keys.SHIFT.toString()+Keys.ENTER.toString();
			element.sendKeys(keyString);
		}
		catch(Exception ex)
		{
			System.out.println("Error opening in New tab" +ex.getMessage());
			ex.printStackTrace();
		}
	}

	/**
	 * Opens the specified element in a new tab.
	 * 
	 * @param element The web element containing the link to open in a new tab.
	 */
	public void openInNewTab(WebElement element)
	{
		try
		{
			System.out.println(element);
			String link=element.getAttribute("href");
			System.out.println(link);
			((JavascriptExecutor)driver).executeScript("window.open()");
			ArrayList<String> tabs = new ArrayList<String>(getWebDriver().getWindowHandles());
			getWebDriver().switchTo().window(tabs.get(1));
			getWebDriver().get(link);
		}
		catch(Exception ex)
		{
			System.out.println("Error opening in New tab" +ex.getMessage());
		}
	}

	/**
	 * Types a single character into the specified web element.
	 * 
	 * @param element The web element to type into.
	 * @param msg     The character to type.
	 */
	public void typeCharacter(WebElement element, char msg) {
		try
		{
			pollingWait(element,explicitTimeOut);
			Thread.sleep(500);
			System.out.println("ch-->"+msg);
			element.sendKeys(Character.toString(msg));
		}
		catch(Exception ex)
		{
			System.out.println("Failed to type the Webelement "+element);
			ex.printStackTrace();
		}

	}

	/**
	 * Types text into the specified web element using JavaScript.
	 * 
	 * @param ele  The web element to type into.
	 * @param text The text to type.
	 */
	public void typeTextUsingJS(WebElement ele, String text)
	{
		try
		{
			ele.isEnabled();
			WebDriverWait wait = new WebDriverWait(getWebDriver(), Duration.ofSeconds(explicitTimeOut));
			wait.until(ExpectedConditions.elementToBeClickable(ele));
			pollingWait(ele,explicitTimeOut);
			JavascriptExecutor js= (JavascriptExecutor)getWebDriver();
			js.executeScript("arguments[0].value='"+text+"'", ele); 
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

	}

	/**
	 * Finds all elements matching the specified XPath locator.
	 * 
	 * @param xpathLocator The XPath locator expression.
	 * @return True if at least one element is found, otherwise false.
	 */
	public boolean findAllElements(String xpathLocator)
	{
		boolean retVal=false;

		List<WebElement> allElements= getWebDriver().findElements(By.xpath(xpathLocator));
		if(allElements.size() > 0)
		{
			retVal= true;
		}

		return retVal;

	}

	/**
	 * Checks if the specified element is visible within the given timeout.
	 * 
	 * @param element  The web element to check visibility for.
	 * @param timeOut  The maximum time to wait for the element to become visible, in seconds.
	 * @return True if the element is visible within the timeout, otherwise false.
	 */
	public boolean isElementVisible(WebElement element,int timeOut)
	{

		boolean retVal=false;
		try
		{
			wait = new WebDriverWait(getWebDriver(), Duration.ofSeconds(timeOut));
			wait.until(ExpectedConditions.visibilityOf(element));
			retVal=true;
		}
		catch(Exception ex)
		{
			System.out.println("Unable to locate the Webelement: "+element);
			ex.printStackTrace();
		}
		return retVal;
	}


	/**
	 * Selects the first available date element on the page.
	 */
	public void selectavailabledate () {
		try {
			// Find all available elements with a common selector
			List<WebElement> availableElements = getWebDriver().findElements(By.cssSelector("div[aria-disabled='false']"));

			// Check if there are any available elements
			if (!availableElements.isEmpty()) {
				// Click the first available element
				availableElements.get(0).click();
			} else {
				System.out.println("No available elements found.");
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} 
	}

	/**
	 * Presses the ENTER key on the specified web element.
	 * 
	 * @param ele The web element to press ENTER on.
	 */
	public void pressEnter(WebElement ele)
	{
		pollingWait(ele,15);
		ele.sendKeys(Keys.ENTER);

	}
	
	/**
     * Uploads a file by sending the file path to the file input element.
     *
     * @param ele      The file input WebElement where the file path is to be set.
     * @param filePath The absolute or relative path to the file to be uploaded.
     */
	public void fileUpload(WebElement ele, String filePath)
	{
		try
		{
			pollingWait(ele, explicitTimeOut);

	        // Upload the file
	        ele.sendKeys(filePath);
	        
	        System.out.println("Upload successful");
		}
		catch(Exception ex)
		{
			System.out.println("Problem while uploading the file");
			ex.printStackTrace();		
		}
	}
	
	/**
     * Uploads a file by setting the file path to the file input element using JavaScript.
     *
     * @param ele      The file input WebElement where the file path is to be set.
     * @param filePath The absolute or relative path to the file to be uploaded.
     */
    public void fileUploadUsingJS(WebElement ele, String filePath) {
        try {
            // Use JavaScript to set the file path
            JavascriptExecutor jsExecutor = (JavascriptExecutor) getWebDriver();
            jsExecutor.executeScript("arguments[0].style.display='block';", ele); // Make the element visible if it is hidden
            jsExecutor.executeScript("arguments[0].value=arguments[1];", ele, filePath);
        } catch (Exception ex) {
            System.out.println("Problem while uploading the file");
            ex.printStackTrace();
        }
    }
    
    /**
     * Uses the Robot class to simulate file upload by copying the file path to the clipboard and pasting it.
     *
     * @param filePath The absolute path to the file to be uploaded.
     * @throws AWTException If the platform configuration does not allow low-level input control.
     */
    public void uploadFileWithRobotInWindows(String filePath) throws AWTException {
        // Copy the file path to the clipboard
        StringSelection stringSelection = new StringSelection(filePath);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

        // Create an instance of Robot class
        Robot robot = new Robot();

        // Paste the clipboard content
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);

        // Press Enter to upload the file
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }
    
    
    /**
     * Uses the Robot class to simulate file upload by copying the file path to the clipboard and pasting it.
     *
     * @param filePath The absolute path to the file to be uploaded.
     * @throws AWTException If the platform configuration does not allow low-level input control.
     */
    public void uploadFileWithRobotInMac(String filePath) throws AWTException, InterruptedException {
    	
    	try
    	{
    		// Copy the file path to the clipboard
    		StringSelection stringSelection = new StringSelection(filePath);
    		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

    		// Create an instance of Robot class
    		Robot robot = new Robot();

    		// Press Command (⌘) + Shift + G to open the "Go to Folder" dialog
    		robot.keyPress(KeyEvent.VK_META); // Command key
    		robot.keyPress(KeyEvent.VK_SHIFT);
    		robot.keyPress(KeyEvent.VK_G);
    		robot.keyRelease(KeyEvent.VK_G);
    		robot.keyRelease(KeyEvent.VK_SHIFT);
    		robot.keyRelease(KeyEvent.VK_META);

    		// Small delay to allow the dialog to open
    		Thread.sleep(1000);

    		// Paste the clipboard content (file path)
    		robot.keyPress(KeyEvent.VK_META);
    		robot.keyPress(KeyEvent.VK_V);
    		robot.keyRelease(KeyEvent.VK_V);
    		robot.keyRelease(KeyEvent.VK_META);

    		// Press Enter to confirm the path
    		robot.keyPress(KeyEvent.VK_ENTER);
    		robot.keyRelease(KeyEvent.VK_ENTER);

    		// Small delay to allow the folder to open
    		Thread.sleep(1000);

    		// Press Enter again to select the file and close the dialog
    		robot.keyPress(KeyEvent.VK_ENTER);
    		robot.keyRelease(KeyEvent.VK_ENTER);
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    }
    
    /**
     * Executes an AppleScript from the given file path.
     *
     * @param scriptPath The path to the AppleScript file to be executed.
     * @throws IOException If an I/O error occurs.
     * @throws InterruptedException If the current thread is interrupted while waiting.
     */
    public void executeAppleScript(String scriptPath) throws IOException, InterruptedException {
    	try
    	{
	        String[] args = new String[] { "osascript", scriptPath };
	        Process process = new ProcessBuilder(args).start();
	        process.waitFor();
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    }
    
    
    /**
     * Makes a hidden element visible using JavaScriptExecutor.
     *
     * @param element The WebElement to be made visible.
     */
    public void makeElementVisible(WebElement ele) {
    	try
    	{
    		JavascriptExecutor jsExecutor = (JavascriptExecutor) getWebDriver();
    		boolean isDisplayedEle = (boolean) jsExecutor.executeScript("return arguments[0].offsetParent !== null;", ele);
            System.out.println("Element visibility -->"+isDisplayedEle);
            jsExecutor.executeScript("arguments[0].style.visibility='visible';", ele);
            jsExecutor.executeScript("arguments[0].style.display='block';", ele);
            
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
 
    }
    
    /**
     * This method generates a random 4-digit number for user id
     */
    
    public String generateFourDigitRandomNumber()
    {
    	 Random random = new Random();
         int randomNumber = 1000 + random.nextInt(9000); // Generates a number between 1000 and 9999
         System.out.println("Random 4-digit number: " + randomNumber);
         String randomNo= String.valueOf(randomNumber);
         return randomNo;
    }
    
    /**
     * This method generates a random 10-digit number for mobile number
     */
    public String generateTenDigitRandomNumber()
    {
    	Random random = new Random();

    	// Generate a 10-digit number
    	long min = 1000000000L; // Minimum 10-digit number
    	long max = 9999999999L; // Maximum 10-digit number
    	long randomNumber = min + ((long)(random.nextDouble() * (max - min)));

    	System.out.println("Random 10-digit number: " + randomNumber);
    	String randomNo= String.valueOf(randomNumber);
    	return randomNo;
    }
    
    /**
	 * Fetches all elements matching the specified XPath locator.
	 * 
	 * @param xpathLocator The XPath locator expression.
	 * @return List of WebElement
	 */
	public List<WebElement> fetchAllElements(String xpathLocator)
	{

		List<WebElement> allElements= getWebDriver().findElements(By.xpath(xpathLocator));
		
		return allElements;

	}
	/**
	 * Waits for the specified web element to be stable with a polling frequency and timeout.
	 * 
	 * @param locValue  The locator value of the webelement.
	 * @param timeOut  The maximum time to wait in seconds.
	 */
	public void waitForTheElementToBeStable(String locValue,int timeOut) {

		Wait<WebDriver> wait = new FluentWait<WebDriver>(getWebDriver()).withTimeout(Duration.ofSeconds(timeOut))
				.pollingEvery(Duration.ofSeconds(2)).ignoring(NoSuchElementException.class);
		wait.until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(By.xpath(locValue))));
	}

	/**
	 * Waits until the specified WebElement becomes invisible within the given timeout duration.
	 * <p>
	 * This method uses FluentWait with polling and ignores {@link NoSuchElementException} and 
	 * {@link StaleElementReferenceException}. It is useful when you want to wait for elements 
	 * such as loaders, spinners, or overlays to disappear before proceeding.
	 * </p>
	 *
	 * @param element the {@link WebElement} to wait for invisibility
	 * @param timeOut the maximum time to wait in seconds before timing out
	 * 
	 * @throws TimeoutException if the element does not become invisible within the specified timeout
	 */
	public void waitUntilElementIsInvisible(WebElement element,int timeOut) {

		Wait<WebDriver> wait = new FluentWait<WebDriver>(getWebDriver()).withTimeout(Duration.ofSeconds(timeOut))
				.pollingEvery(Duration.ofSeconds(2)).ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class);
		wait.until(ExpectedConditions.invisibilityOf(element));
	}
	/**
	 * Clicks and returns the boolean value on the specified WebElement using JavaScript.
	 * 
	 * @param element The WebElement to click.
	 * @throws InterruptedException if the thread is interrupted while waiting.
	 */

	public boolean clickWithReturnValue(WebElement element) throws InterruptedException {
		boolean retVal=false;
		try {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
			executor.executeScript("arguments[0].click()", element);
			retVal=true;
		} catch (Exception ex) {
			element.click();
			System.out.println("Failed to click the Javascript Webelement");
		}
		return retVal;
	}

	/**
     * Simulates a real pointer click on a Konva.js canvas at the given percentage coordinates.
     * This triggers pointer, mouse, and touch events in sequence.
     *
     * @param canvas    The canvas WebElement
     * @param percentX  X coordinate as a percentage of canvas width (0.0 to 1.0)
     * @param percentY  Y coordinate as a percentage of canvas height (0.0 to 1.0)
     */
    public void clickKonvaCanvas(WebElement canvas, double percentX, double percentY) {
    	
//		WebElement canvas = getWebDriver().findElement(By.cssSelector("#canvasStage canvas"));
    	
    	Rectangle rect = canvas.getRect();
		System.out.println("Canvas size: " + rect.width + "x" + rect.height);
		System.out.println("Clicking at: " + rect.width * 0.5 + ", " + rect.height * 0.5);
    	
        JavascriptExecutor js = (JavascriptExecutor) getWebDriver();

        String script =
            "var canvas = arguments[0];" +
            "var rect = canvas.getBoundingClientRect();" +
            "var x = rect.left + rect.width * arguments[1];" +
            "var y = rect.top + rect.height * arguments[2];" +

            // Move pointer into canvas (simulate hover)
            "['pointerover','pointerenter','mouseover','mouseenter'].forEach(type => {" +
            "  var evt = new PointerEvent(type, {clientX:x, clientY:y, bubbles:true});" +
            "  canvas.dispatchEvent(evt);" +
            "});" +

            // Simulate pointer down + touch start
            "['pointerdown','mousedown','touchstart'].forEach(type => {" +
            "  var evt = new PointerEvent(type, {clientX:x, clientY:y, bubbles:true});" +
            "  canvas.dispatchEvent(evt);" +
            "});" +

            // Small pointer move (helps Konva detect)
            "['pointermove','mousemove','touchmove'].forEach(type => {" +
            "  var evt = new PointerEvent(type, {clientX:x+1, clientY:y+1, bubbles:true});" +
            "  canvas.dispatchEvent(evt);" +
            "});" +

            // Pointer up + click sequence
            "['pointerup','mouseup','click','touchend'].forEach(type => {" +
            "  var evt = new PointerEvent(type, {clientX:x, clientY:y, bubbles:true});" +
            "  canvas.dispatchEvent(evt);" +
            "});";

        js.executeScript(script, canvas, percentX, percentY);
    }
    /**
     * Set the color of a Chakra UI color input at runtime.
     *
     * @param colorInput The <input type="color"> WebElement
     * @param colorHex   The color to set (e.g., "#ff0000")
     */
    public void setColor(WebElement colorInput, String colorHex) {
        JavascriptExecutor js = (JavascriptExecutor) getWebDriver();

        js.executeScript(
            "arguments[0].value = arguments[1];" +
            "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
            "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
            colorInput, colorHex
        );
    }

    /**
     * Set the color and update the label text as well.
     *
     * @param colorInput The <input type="color"> WebElement
     * @param label      The <label> element showing the color
     * @param colorHex   The color to set (e.g., "#ff0000")
     */
    public void setColorWithLabel(WebElement colorInput, WebElement label, String colorHex) {
        JavascriptExecutor js = (JavascriptExecutor) getWebDriver();

        js.executeScript(
            "arguments[0].value = arguments[2];" +
            "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
            "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));" +
            "arguments[1].innerText = arguments[2];",
            colorInput, label, colorHex
        );
    }

}
