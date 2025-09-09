package utils;

import io.appium.java_client.*;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.SupportsContextSwitching;
import io.appium.java_client.remote.SupportsRotation;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.PointerInput.Kind;
import org.openqa.selenium.interactions.PointerInput.MouseButton;
import org.openqa.selenium.interactions.PointerInput.Origin;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CommonNativeWrappers extends AbstractTestNGCucumberTests {
	public static final int MAX_SCROLL = 10;
	public static int explicitTimeout = 30;

	public boolean useExistingApp = false;
	public static ThreadLocal<AppiumDriver> driver = new ThreadLocal<AppiumDriver>();
	public AppiumDriverLocalService service;
	public AppiumServiceBuilder builder;
//	public String serverURL;
	public String serverURL = "http://0.0.0.0:4723";
	public static String userName = System.getenv("LT_USERNAME") == null ? "" // Add username here
            : System.getenv("LT_USERNAME");
    public static String accessKey = System.getenv("LT_ACCESS_KEY") == null ? "" // Add accessKey here
            : System.getenv("LT_ACCESS_KEY");
    public static String appId;
    public static String mobileGridUrl = System.getenv("LT_GRID_URL") == null ? "mobile-hub.lambdatest.com" : System.getenv("LT_GRID_URL");
    public static String timeStamp;

	/**
	 * Retrieves the current instance of the AppiumDriver.
	 * 
	 * @return The current instance of the AppiumDriver.
	 */

	public static synchronized AppiumDriver getDriver() {
		return driver.get();
	}


	/**
	 * Launches the mobile application (Native/Hybrid) on the specified platform.
	 * 
	 * @param platformName     The name of the platform (Android/iOS).
	 * @param deviceName       The name of the device/emulator.
	 * @param udid             The Unique Device Identifier (UDID) of the device (optional).
	 * @param appPackage       The package name of the Android application (optional).
	 * @param appActivity      The activity name of the Android application (optional).
	 * @param automationName   The name of the automation framework (e.g., XCUITest, UiAutomator2).
	 * @param chromeDriverPort The port for ChromeDriver (optional).
	 * @param systemPort       The port for system interactions (optional).
	 * @param xcodeOrgId       The Xcode organization ID (optional).
	 * @param xcodeSigningId   The Xcode signing ID (optional).
	 * @param bundleId         The bundle ID of the iOS application (optional).
	 * @param app              The absolute path of the application (optional).
	 * @param mjpegServerPort  The port for MJPEG server (optional).
	 * @param wdaLocalPort     The port for WebDriverAgent (optional).
	 * @return                  True if the application is launched successfully, otherwise false.
	 */

	public boolean launchApp(String platformName, String deviceName, String udid, String appPackage, String appActivity,
			String automationName, String chromeDriverPort, String systemPort, String xcodeOrgId, String xcodeSigningId,
			String bundleId, String app, String mjpegServerPort, String wdaLocalPort, String projectName, String buildName) {
		try {
			
			timeStamp=GenericWrappers.getTime();
			
			DesiredCapabilities dc = new DesiredCapabilities();
			
			// To pass the Unique Device Identifier
			if (!udid.equals(""))
				dc.setCapability("udid", udid);
			// To pass the absolute path of the application
			if (!app.equals(""))
			{
				appId = System.getenv("LT_APP_ID") == null ? app : System.getenv("LT_APP_ID");      //Enter your LambdaTest App ID at the place of lt://proverbial-android

				if(platformName.equalsIgnoreCase("ios"))
				{
					dc.setCapability("app", appId);
				}
				else if (platformName.equalsIgnoreCase("android"))
				{
					dc.setCapability("app", appId);
//					dc.setCapability("app", "lt://APP1016058431732715441660250");
					/*
					 * This is for local run
					dc.setCapability("app", System.getProperty("user.dir") + app);
					*/
				}
				
			}
			// Android
			if (!appPackage.equals(""))
				dc.setCapability("appPackage", appPackage);
			if (!appActivity.equals(""))
				dc.setCapability("appActivity", appActivity);
			// For hybrid app parallel testing
			if (!chromeDriverPort.equals(""))
				dc.setCapability("chromedriverPort", chromeDriverPort);
			// For native app parallel testing
			if (!mjpegServerPort.equals(""))
				dc.setCapability("mjpegServerPort", mjpegServerPort);
			// For hybrid/native app parallel testing
			if (!systemPort.equals(""))
				dc.setCapability("systemPort", systemPort);
			// iOS
			// For hybrid/native app parallel testing
			if (!wdaLocalPort.equals(""))
				dc.setCapability("wdaLocalPort", wdaLocalPort);
			// To pass the Xcode Org ID if the application and WDA are built with different developer certificates
			if (!xcodeOrgId.equals(""))
				dc.setCapability("xcodeOrgId", xcodeOrgId);
			// To pass the Xcode Signing ID if the application and WDA are built with different developer certificates
			if (!xcodeSigningId.equals(""))
				dc.setCapability("xcodeSigningId", xcodeSigningId);
			if (!bundleId.equals(""))
				dc.setCapability("bundleId", bundleId);
			// Mandatory desired capabilities
			// To set the device name
			dc.setCapability("deviceName", deviceName);
		
			// Comment the below line based on need
			if (useExistingApp) {
				dc.setCapability("noReset", true);
				dc.setCapability("forceAppLaunch", true);
				dc.setCapability("shouldTerminateApp", true);
			}
			// ✅ Correct way to pass Appium setting snapshotMaxDepth
			Map<String, Object> settings = new HashMap<String, Object>();
			settings.put("snapshotMaxDepth", 200);
			dc.setCapability("settings", settings);

			if (platformName.equalsIgnoreCase("Android")) {	
				
//				String app_id = System.getenv("LT_APP_ID") == null ? "lt://APP10160181451716978964359342" : System.getenv("LT_APP_ID");      //Enter your LambdaTest App ID at the place of lt://proverbial-android
				
			    /*For local app run
			    dc.setCapability("uiautomator2ServerInstallTimeout", 90000);
				dc.setCapability("adbExecTimeout", 30000);
				dc.setCapability("autoGrantPermissions", true);
				*/
				
				dc.setCapability("w3c", true);
				dc.setCapability("platformName", platformName);
				dc.setCapability("deviceName", deviceName);
//				dc.setCapability("platformVersion", platformVersion);
				dc.setCapability("project", projectName+"_"+timeStamp);
				dc.setCapability("build", buildName+"_"+timeStamp);
				/*the below capability is for static test name. However we getting the scenario name dynamically from feature name in Hooks
				dc.setCapability("name", "Mobile Test");
				*/
				dc.setCapability("timezone", "UTC+05:30");
				dc.setCapability("isRealMobile", true);
				dc.setCapability("devicelog", true);
				dc.setCapability("autoGrantPermissions", true);
				dc.setCapability("autoAcceptAlerts", true);
				dc.setCapability("network", false);
				dc.setCapability("video", true);
				dc.setCapability("visual", true);
				// To pass the VDM
				dc.setCapability("automationName", "UiAutomator2");
				
		        String androidLambdaTestUrl = "https://" + userName + ":" + accessKey + "@" + mobileGridUrl + "/wd/hub";
	            System.out.println("lambdaTestUrl Android-->"+androidLambdaTestUrl);

	            //To execute in LambdaTest env
		        driver.set(new AndroidDriver(new URL(androidLambdaTestUrl), dc));

				/*
				 * Commenting for testing lambdatest
				//For establishing session through automatic appium server startup
				driver.set(new AndroidDriver(new URI(serverURL).toURL(), dc));
				
				//For establishing session through manual appium server startup

//					driver.set(new AndroidDriver(new URI("http://127.0.0.1:4723/").toURL(), dc));
 * 
 */


			} else if (platformName.equalsIgnoreCase("iOS")) {
				
				 String iOSLambdaTestUrl= "https://" + userName + ":" + accessKey + "@" + mobileGridUrl + "/wd/hub";
		         System.out.println("lambdaTestUrl iOS-->"+iOSLambdaTestUrl);
				
				// Comment the below line based on need
				dc.setCapability("w3c", true);
				dc.setCapability("platformName", platformName);
				dc.setCapability("deviceName", deviceName);
				dc.setCapability("project", projectName+"_"+timeStamp);
				dc.setCapability("build", buildName+"_"+timeStamp);
				/*the below capability is for static test name. However we getting the scenario name dynamically from feature name in Hooks
				dc.setCapability("name", "Mobile Test");
				*/
				dc.setCapability("timezone", "UTC+05:30");
				dc.setCapability("idleTimeout", 20);
				dc.setCapability("autoGrantPermissions", true);
				dc.setCapability("autoAcceptAlerts", true);
				dc.setCapability("isRealMobile", true);
	            dc.setCapability("devicelog", true);
	            dc.setCapability("network", true);
	            dc.setCapability("video", true);
				dc.setCapability("visual", true);
//				dc.setCapability("respectSystemAlerts", true);
				// To pass the VDM
				dc.setCapability("automationName", "XCUITest");
	            /* For local run
				driver.set(new IOSDriver(new URI(serverURL).toURL(), dc));
				*/
	         
				
	            //To execute in LambdaTest env
				driver.set( new IOSDriver(
	                    new URL(iOSLambdaTestUrl),
	                    dc));
			}
			getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Verifies if the application with the specified bundle ID or package name is installed.
	 * If installed, the application is removed and then re-installed from the provided app path.
	 * 
	 * @param bundleIdOrAppPackage The bundle ID or package name of the application to verify/install.
	 * @param appPath               The path to the application file to install.
	 * @return                      True if the installation is successful, otherwise false.
	 */

	public boolean verifyAndInstallApp(String bundleIdOrAppPackage, String appPath) {
		boolean bInstallSuccess = false;
		try {
			if (((InteractsWithApps) getDriver()).isAppInstalled(bundleIdOrAppPackage)) {
				((InteractsWithApps) getDriver()).removeApp(bundleIdOrAppPackage);
			}
			((InteractsWithApps) getDriver()).installApp(System.getProperty("user.dir") + appPath);
			bInstallSuccess = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bInstallSuccess;
	}

	/**
	 * Pauses the execution for the specified duration in milliseconds.
	 * Note: This method is not recommended for use in app testing as it introduces unnecessary delays.
	 * 
	 * @param mSec  The duration to sleep in milliseconds.
	 */
	public void sleep(int mSec) {
		try {
			Thread.sleep(mSec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Prints the available contexts in the application.
	 */
	public void printContext() {
		try {
			Set<String> contexts = ((SupportsContextSwitching) getDriver()).getContextHandles();
			for (String context : contexts) {
				System.out.println(context);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Switches the context of the application to the specified context.
	 * 
	 * @param context The context to switch to.
	 */
	public void switchContext(String context) {
		try {
			((SupportsContextSwitching) getDriver()).context(context);
			getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Switches the context of the application to the Native view.
	 * This method finds the context containing "NATIVE_APP" and switches to it.
	 */
	public void switchNativeView() {
		try {
			Set<String> contextNames = ((SupportsContextSwitching) getDriver()).getContextHandles();
			for (String contextName : contextNames) {
				if (contextName.contains("NATIVE_APP"))
					((SupportsContextSwitching) getDriver()).context(contextName);
			}
			getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves the WebElement based on the locator and locator value.
	 * 
	 * @param locator   The type of locator (e.g., id, name, xpath).
	 * @param locValue  The value of the locator.
	 * @return          The WebElement corresponding to the locator and locator value.
	 */
	public WebElement getWebElement(String locator, String locValue) {
		try {
			switch (locator) {
			case "id":
				//                    return getDriver().findElement(AppiumBy.id(locValue));
				return getDriver().findElement(AppiumBy.xpath("//*[@resource-id='" + locValue + "' or @id='" + locValue + "']"));
			case "name":
				//					return getDriver().findElement(AppiumBy.name(locValue));
				return getDriver().findElement(AppiumBy.xpath("//*[@name='" + locValue + "']"));
			case "className":
				return getDriver().findElement(AppiumBy.className(locValue));
			case "link":
				return getDriver().findElement(AppiumBy.linkText(locValue));
			case "partialLink":
				return getDriver().findElement(AppiumBy.partialLinkText(locValue));
			case "tag":
				return getDriver().findElement(AppiumBy.tagName(locValue));
			case "css":
				return getDriver().findElement(AppiumBy.cssSelector(locValue));
			case "xpath":
				return getDriver().findElement(AppiumBy.xpath(locValue));
			case "accessibilityId":
				return getDriver().findElement(AppiumBy.accessibilityId(locValue));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Takes a screenshot of the current application screen.
	 * 
	 * @return  The unique number assigned to the screenshot.
	 */
	public long takeScreenShot() {
		long number = (long) Math.floor(Math.random() * 900000000L) + 10000000L;
		try {
			File srcFiler = getDriver().getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(srcFiler, new File("./reports/images/" + number + ".png"));
		} catch (WebDriverException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("The snapshot could not be taken");
		}
		return number;
	}

	/**
	 * Checks if the WebElement is displayed on the screen.
	 * 
	 * @param ele   The WebElement to check.
	 * @return      True if the WebElement is displayed, false otherwise.
	 */
	public boolean eleIsDisplayed(WebElement ele) {
		try {
			return ele.isDisplayed();
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * Verifies if the text of the WebElement matches the expected text.
	 * 
	 * @param ele       The WebElement to verify.
	 * @param Expected  The expected text.
	 * @return          True if the text matches, false otherwise.
	 */
	public boolean verifyText(WebElement ele, String Expected) {
		try {
			return ele.getText().equals(Expected);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Scrolls within the application from the specified starting coordinates to the specified ending coordinates.
	 * 
	 * @param startX    The starting x-coordinate of the scroll.
	 * @param startY    The starting y-coordinate of the scroll.
	 * @param endX      The ending x-coordinate of the scroll.
	 * @param endY      The ending y-coordinate of the scroll.
	 * @return          True if the scroll operation is successful, false otherwise.
	 */
	private boolean scroll(int startX, int startY, int endX, int endY) {
		try {
			PointerInput finger = new PointerInput(Kind.TOUCH, "finger");
			Sequence sequence = new Sequence(finger, 1);
			sequence.addAction(finger.createPointerMove(Duration.ZERO, Origin.viewport(), startX, startY));
			sequence.addAction(finger.createPointerDown(MouseButton.LEFT.asArg()));
			sequence.addAction(finger.createPointerMove(Duration.ofSeconds(2), Origin.viewport(), endX, endY));
			sequence.addAction(finger.createPointerUp(MouseButton.LEFT.asArg()));
			getDriver().perform(Collections.singletonList(sequence));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Performs a single tap using the specified webelement.
	 * 
	 * @param element The Webelement to perform the action
	 * 
	 */
	public void singleTapUsingElement(WebElement element) {
		
//		WebElement element = getDriver().findElement(By.xpath(ele));

		// Create PointerInput instance for touch
		PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");

		// Get center coordinates of the element
		Point center = element.getLocation();
		Dimension size = element.getSize();
		int centerX = center.getX() + size.getWidth() / 2;
		int centerY = center.getY() + size.getHeight() / 2;

		// Create tap sequence
		Sequence tap = new Sequence(finger, 1);
		tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX, centerY));
		tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
		tap.addAction(new Pause(finger, Duration.ofMillis(200))); // Optional: short pause
		tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

		// Perform the action
		getDriver().perform(Arrays.asList(tap));
	}
	
	/**
	 * Performs a single tap at the specified coordinates.
	 * 
	 * @param x The x-coordinate of the tap.
	 * @param y The y-coordinate of the tap.
	 */
	public void singleTap(int x, int y) {
		
	    PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
	    Sequence tap = new Sequence(finger, 1);

	    tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
	    tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
	    tap.addAction(new Pause(finger, Duration.ofMillis(100)));
	    tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

	    getDriver().perform(Collections.singletonList(tap));
	}



	/**
	 * Performs a double tap at the specified coordinates.
	 * 
	 * @param x The x-coordinate of the tap.
	 * @param y The y-coordinate of the tap.
	 */
	public void doubleTap(int x, int y) {
		PointerInput finger = new PointerInput(Kind.TOUCH, "finger");
		Sequence doubleTap = new Sequence(finger, 1);
		doubleTap.addAction(finger.createPointerMove(Duration.ZERO, Origin.viewport(), x, y));
		doubleTap.addAction(finger.createPointerDown(MouseButton.LEFT.asArg()));
		doubleTap.addAction(new Pause(finger, Duration.ofMillis(100)));
		doubleTap.addAction(finger.createPointerUp(MouseButton.LEFT.asArg()));
		doubleTap.addAction(new Pause(finger, Duration.ofMillis(100)));
		doubleTap.addAction(finger.createPointerDown(MouseButton.LEFT.asArg()));
		doubleTap.addAction(new Pause(finger, Duration.ofMillis(100)));
		doubleTap.addAction(finger.createPointerUp(MouseButton.LEFT.asArg()));
		getDriver().perform(Collections.singletonList(doubleTap));
	}

	/**
	 * Performs a long press at the specified coordinates.
	 * 
	 * @param x The x-coordinate of the long press.
	 * @param y The y-coordinate of the long press.
	 */
	public void longPress(int x, int y) {
		PointerInput finger = new PointerInput(Kind.TOUCH, "finger");
		Sequence longPress = new Sequence(finger, 1);
		longPress.addAction(finger.createPointerMove(Duration.ZERO, Origin.viewport(), x, y));
		longPress.addAction(finger.createPointerDown(MouseButton.LEFT.asArg()));
		longPress.addAction(new Pause(finger, Duration.ofMillis(2000)));
		longPress.addAction(finger.createPointerUp(MouseButton.LEFT.asArg()));
		getDriver().perform(Collections.singletonList(longPress));
	}

	/**
	 * Performs a pinch gesture in the application.
	 */
	public void pinchInApp() {
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		int maxY = getDriver().manage().window().getSize().getHeight();
		int maxX = getDriver().manage().window().getSize().getWidth();
		PointerInput finger1 = new PointerInput(Kind.TOUCH, "finger1");
		Sequence a = new Sequence(finger1, 1);
		a.addAction(finger1.createPointerMove(Duration.ofSeconds(0), Origin.viewport(), (int) (maxX * 0.75),
				(int) (maxY * 0.25)));
		a.addAction(finger1.createPointerDown(MouseButton.LEFT.asArg()));
		a.addAction(finger1.createPointerMove(Duration.ofSeconds(1), Origin.viewport(), (int) (maxX * 0.5),
				(int) (maxY * 0.5)));
		a.addAction(finger1.createPointerUp(MouseButton.LEFT.asArg()));
		PointerInput finger2 = new PointerInput(Kind.TOUCH, "finger2");
		Sequence b = new Sequence(finger2, 1);
		b.addAction(finger2.createPointerMove(Duration.ofSeconds(0), Origin.viewport(), (int) (maxX * 0.25),
				(int) (maxY * 0.75)));
		b.addAction(finger2.createPointerDown(MouseButton.LEFT.asArg()));
		b.addAction(finger2.createPointerMove(Duration.ofSeconds(1), Origin.viewport(), (int) (maxX * 0.5),
				(int) (maxY * 0.5)));
		b.addAction(finger2.createPointerUp(MouseButton.LEFT.asArg()));
		getDriver().perform(Arrays.asList(a, b));
	}

	/**
	 * Performs a zoom gesture in the application.
	 */
	public void zoomInApp() {
		int maxY = getDriver().manage().window().getSize().getHeight();
		int maxX = getDriver().manage().window().getSize().getWidth();
		PointerInput finger1 = new PointerInput(Kind.TOUCH, "qa-finger1");
		Sequence a = new Sequence(finger1, 1);
		a.addAction(finger1.createPointerMove(Duration.ofSeconds(0), Origin.viewport(), (int) (maxX * 0.5),
				(int) (maxY * 0.5)));
		a.addAction(finger1.createPointerDown(MouseButton.LEFT.asArg()));
		a.addAction(finger1.createPointerMove(Duration.ofSeconds(1), Origin.viewport(), (int) (maxX * 0.75),
				(int) (maxY * 0.25)));
		a.addAction(finger1.createPointerUp(MouseButton.LEFT.asArg()));
		PointerInput finger2 = new PointerInput(Kind.TOUCH, "qa-finger2");
		Sequence b = new Sequence(finger2, 1);
		b.addAction(finger2.createPointerMove(Duration.ofSeconds(0), Origin.viewport(), (int) (maxX * 0.5),
				(int) (maxY * 0.5)));
		b.addAction(finger2.createPointerDown(MouseButton.LEFT.asArg()));
		b.addAction(finger2.createPointerMove(Duration.ofSeconds(1), Origin.viewport(), (int) (maxX * 0.25),
				(int) (maxY * 0.75)));
		b.addAction(finger2.createPointerUp(MouseButton.LEFT.asArg()));
		getDriver().perform(Arrays.asList(a, b));
	}

	/**
	 * Swipes in the specified direction within the application.
	 * 
	 * @param direction The direction of the swipe (up, down, left, right).
	 */
	public void swipe(String direction) {
		switch (direction.toLowerCase()) {
		case "up":
			swipeUpInApp();
			break;
		case "down":
			swipeDownInApp();
			break;
		case "left":
			swipeLeftInApp();
			break;
		case "right":
			swipeRightInApp();
			break;
		default:
			throw new RuntimeException("Invalid direction. So could not perform swipe");
		}
	}

	/**
	 * Scrolls up within the application.
	 * 
	 * @return True if the scroll operation is successful, false otherwise.
	 */
	private boolean swipeUpInApp() {
		Dimension size = getDriver().manage().window().getSize();
		int startX = (int) (size.getWidth() * 0.5);
		int startY = (int) (size.getHeight() * 0.8);
		int endX = (int) (size.getWidth() * 0.5);
		int endY = (int) (size.getHeight() * 0.2);
		return scroll(startX, startY, endX, endY);
	}

	/**
	 * Scrolls down within the application.
	 * 
	 * @return True if the scroll operation is successful, false otherwise.
	 */
	private boolean swipeDownInApp() {
		Dimension size = getDriver().manage().window().getSize();
		int startX = (int) (size.getWidth() * 0.5);
		int startY = (int) (size.getHeight() * 0.2);
		int endX = (int) (size.getWidth() * 0.5);
		int endY = (int) (size.getHeight() * 0.8);
		return scroll(startX, startY, endX, endY);
	}

	/**
	 * Scrolls left within the application.
	 * 
	 * @return True if the scroll operation is successful, false otherwise.
	 */
	private boolean swipeLeftInApp() {
		Dimension size = getDriver().manage().window().getSize();
		int startX = (int) (size.getWidth() * 0.8);
		int startY = (int) (size.getHeight() * 0.5);
		int endX = (int) (size.getWidth() * 0.2);
		int endY = (int) (size.getHeight() * 0.5);
		return scroll(startX, startY, endX, endY);
	}

	/**
	 * Scrolls right within the application.
	 * 
	 * @return True if the scroll operation is successful, false otherwise.
	 */
	private boolean swipeRightInApp() {
		Dimension size = getDriver().manage().window().getSize();
		int startX = (int) (size.getWidth() * 0.2);
		int startY = (int) (size.getHeight() * 0.5);
		int endX = (int) (size.getWidth() * 0.8);
		int endY = (int) (size.getHeight() * 0.5);
		return scroll(startX, startY, endX, endY);
	}

	/**
	 * Scrolls down within a specific web element in the application.
	 * 
	 * @param direction The direction of the swipe (up, down, left, right).
	 * @param ele       The web element within which to perform the swipe.
	 */
	public void swipeWithinWebElement(String direction, WebElement ele) {
		switch (direction.toLowerCase()) {
		case "up":
			swipeUpInAppWithWebElement(ele);
			break;
		case "down":
			swipeDownInAppWithWebElement(ele);
			break;
		case "left":
			swipeLeftInAppWithWebElement(ele);
			break;
		case "right":
			swipeRightInAppWithWebElement(ele);
			break;
		default:
			throw new RuntimeException("Invalid direction. So could not perform swipe");
		}
	}

	/**
	 * Swipes down within the specified web element in the application.
	 *
	 * @param ele The web element within which the swipe action is performed.
	 * @return True if the swipe action is successful, false otherwise.
	 */
	protected boolean swipeDownInAppWithWebElement(WebElement ele) {
		Rectangle rect = ele.getRect();
		int startX = (int) (((rect.getWidth()) * 0.5) + rect.getX());
		int startY = (int) (((rect.getHeight()) * 0.2) + rect.getY());
		int endX = (int) (((rect.getWidth()) * 0.5) + rect.getX());
		int endY = (int) (((rect.getHeight()) * 0.8) + rect.getY());
		return scroll(startX, startY, endX, endY);
	}

	/**
	 * Swipes up within the specified web element in the application.
	 *
	 * @param ele The web element within which the swipe action is performed.
	 * @return True if the swipe action is successful, false otherwise.
	 */
	private boolean swipeUpInAppWithWebElement(WebElement ele) {
		Rectangle rect = ele.getRect();
		int startX = (int) (((rect.getWidth()) * 0.5) + rect.getX());
		int startY = (int) (((rect.getHeight()) * 0.8) + rect.getY());
		int endX = (int) (((rect.getWidth()) * 0.5) + rect.getX());
		int endY = (int) (((rect.getHeight()) * 0.2) + rect.getY());
		return scroll(startX, startY, endX, endY);
	}

	/**
	 * Swipes right within the specified web element in the application.
	 *
	 * @param ele The web element within which the swipe action is performed.
	 * @return True if the swipe action is successful, false otherwise.
	 */
	private boolean swipeRightInAppWithWebElement(WebElement ele) {
		Rectangle rect = ele.getRect();
		int startX = (int) (((rect.getWidth()) * 0.2) + rect.getX());
		int startY = (int) (((rect.getHeight()) * 0.5) + rect.getY());
		int endX = (int) (((rect.getWidth()) * 0.8) + rect.getX());
		int endY = (int) (((rect.getHeight()) * 0.5) + rect.getY());
		return scroll(startX, startY, endX, endY);
	}

	/**
	 * Swipes left within the specified web element in the application.
	 *
	 * @param ele The web element within which the swipe action is performed.
	 * @return True if the swipe action is successful, false otherwise.
	 */
	private boolean swipeLeftInAppWithWebElement(WebElement ele) {
		Rectangle rect = ele.getRect();
		int startX = (int) (((rect.getWidth()) * 0.8) + rect.getX());
		int startY = (int) (((rect.getHeight()) * 0.5) + rect.getY());
		int endX = (int) (((rect.getWidth()) * 0.2) + rect.getX());
		int endY = (int) (((rect.getHeight()) * 0.5) + rect.getY());
		return scroll(startX, startY, endX, endY);
	}

	/**
	 * Scrolls up until the specified web element identified by the locator and location value is displayed in the application.
	 *
	 * @param locator  The method used to locate the web element (e.g., "id", "name", "xpath").
	 * @param locValue The value of the locator to identify the web element.
	 * @return True if the web element becomes visible after scrolling, false otherwise.
	 */
	public boolean swipeUpInAppUntilElementIsVisible(String locator, String locValue) {
		int i = 1;
		while (!eleIsDisplayed(getWebElement(locator, locValue))) {
			swipeUpInApp();
			i++;
			if (i == MAX_SCROLL)
				break;
		}
		return true;
	}

	/**
	 * Scrolls down until the specified web element identified by the locator and location value is displayed in the application.
	 *
	 * @param locator  The method used to locate the web element (e.g., "id", "name", "xpath").
	 * @param locValue The value of the locator to identify the web element.
	 * @return True if the web element becomes visible after scrolling, false otherwise.
	 */
	public boolean swipeDownInAppUntilElementIsVisible(String locator, String locValue) {
		int i = 1;
		while (!eleIsDisplayed(getWebElement(locator, locValue))) {
			swipeDownInApp();
			i++;
			if (i == MAX_SCROLL)
				break;
		}
		return true;
	}

	/**
	 * Scrolls left until the specified web element identified by the locator and location value is displayed in the application.
	 *
	 * @param locator  The method used to locate the web element (e.g., "id", "name", "xpath").
	 * @param locValue The value of the locator to identify the web element.
	 * @return True if the web element becomes visible after scrolling, false otherwise.
	 */
	public boolean swipeLeftInAppUntilElementIsVisible(String locator, String locValue) {
		int i = 1;
		while (!eleIsDisplayed(getWebElement(locator, locValue))) {
			swipeLeftInApp();
			i++;
			if (i == MAX_SCROLL)
				break;
		}
		return true;
	}

	/**
	 * Scrolls right until the specified web element identified by the locator and location value is displayed in the application.
	 *
	 * @param locator  The method used to locate the web element (e.g., "id", "name", "xpath").
	 * @param locValue The value of the locator to identify the web element.
	 * @return True if the web element becomes visible after scrolling, false otherwise.
	 */
	public boolean swipeRightInAppUntilElementIsVisible(String locator, String locValue) {
		int i = 1;
		while (!eleIsDisplayed(getWebElement(locator, locValue))) {
			swipeRightInApp();
			i++;
			if (i == MAX_SCROLL)
				break;
		}
		return true;
	}

	/**
	 * Pulls a file from the device to the specified destination path.
	 *
	 * @param phonePath       The path of the file on the device to pull.
	 * @param destinationPath The destination path on the local system where the file will be saved.
	 * @return {@code true} if the file is successfully pulled, {@code false} otherwise.
	 */
	public boolean pullFileFromDevice(String phonePath, String destinationPath) {
		byte[] srcData = ((PullsFiles) getDriver()).pullFile(phonePath);
		Path destData = Paths.get(destinationPath);
		try {
			Files.write(destData, srcData);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Closes all the applications opened in the current session.
	 */
	public void closeApp() {
		if (getDriver() != null) {
			try {
				getDriver().quit();
			} catch (Exception ignored) {
			}
		}
	}

	/**
	 * Sets the device orientation to portrait mode.
	 *
	 * @return {@code true} if the orientation is successfully set to portrait, {@code false} otherwise.
	 */
	public boolean setPortraitOrientation() {
		((SupportsRotation) getDriver()).rotate(ScreenOrientation.PORTRAIT);
		return true;
	}

	/**
	 * Sets the device orientation to landscape mode.
	 *
	 * @return {@code true} if the orientation is successfully set to landscape, {@code false} otherwise.
	 */
	public boolean setLandscapeOrientation() {
		((SupportsRotation) getDriver()).rotate(ScreenOrientation.LANDSCAPE);
		return true;
	}

	/**
	 * Hides the on-screen keyboard if it is visible.
	 * Note: This will not work for numerical keyboards on iOS devices.
	 */

	public void hideKeyboard() {
		if (isKeyboardShown()) {
			try {
				((HidesKeyboard) getDriver()).hideKeyboard();
			} catch (Exception e) {
				if (getDriver().getCapabilities().getPlatformName().toString().equalsIgnoreCase("iOS")) {
					String context = ((SupportsContextSwitching) getDriver()).getContext();
					assert context != null;
					boolean isNative = context.equalsIgnoreCase("NATIVE_APP");
					if (!isNative) {
						switchNativeView();
					}
					if (isKeyboardShown()) {
						click(getWebElement(Locators.ACCESSIBILITY_ID.toString(), "Done"));
					}
					if (!isNative) {
						switchContext(context);
					}
				}
			}
		}
	}

	/**
	 * Checks whether the on-screen keyboard is currently shown.
	 *
	 * @return {@code true} if the keyboard is shown, {@code false} otherwise.
	 */
	public boolean isKeyboardShown() {
		return ((HasOnScreenKeyboard) getDriver()).isKeyboardShown();
	}

	/**
	 * Retrieves the current orientation set in the application.
	 *
	 * @return A string representing the current orientation (e.g., "PORTRAIT", "LANDSCAPE").
	 */
	public String getOrientation() {
		return ((SupportsRotation) getDriver()).getOrientation().toString();
	}

	/**
	 * Enters data into a web element.
	 *
	 * @param ele          The web element where the data will be entered.
	 * @param data         The data to be entered.
	 * @return {@code true} if the data is successfully entered, {@code false} otherwise.
	 */
	public boolean enterValue(WebElement ele, String data) {
		return enterValue(ele, data, true);
	}

	/**
	 * Enters data into a web element.
	 *
	 * @param ele          The web element where the data will be entered.
	 * @param data         The data to be entered.
	 * @param hideKeyboard Whether to hide the keyboard after entering data.
	 * @return {@code true} if the data is successfully entered, {@code false} otherwise.
	 */
	public boolean enterValue(WebElement ele, String data, boolean hideKeyboard) {
		ele.clear();
		ele.sendKeys(data);
		if (hideKeyboard) {
			hideKeyboard();
		}
		return true;
	}

	/**
	 * Clicks on a web element.
	 *
	 * @param ele The web element to click.
	 * @return {@code true} if the click operation is successful, {@code false} otherwise.
	 */
	public boolean click(WebElement ele) {
		try {
			ele.click();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Retrieves the text from a web element.
	 *
	 * @param ele The web element from which to retrieve text.
	 * @return The text content of the web element.
	 */
	public String getText(WebElement ele) {
		return ele.getText();
	}

	/**
	 * Switches to another application installed on the device.
	 *
	 * @param bundleIdOrAppPackage The bundle ID or package name of the application to switch to.
	 */
	public void switchToAnotherApp(String bundleIdOrAppPackage) {
		((InteractsWithApps) getDriver()).activateApp(bundleIdOrAppPackage);
	}

	/**
	 * Closes the specified application installed on the device.
	 *
	 * @param bundleIdOrAppPackage The bundle ID or package name of the application to close.
	 */
	public void stopRunningApp(String bundleIdOrAppPackage) {
		((InteractsWithApps) getDriver()).terminateApp(bundleIdOrAppPackage);
	}

	/**
	 * Enumerates the locators used to identify web elements.
	 */
	public enum Locators {
		ID("id"),
		NAME("name"),
		CLASS_NAME("className"),
		LINK_TEXT("link"),
		PARTIAL_LINK_TEXT("partialLink"),
		TAG_NAME("tag"),
		CSS("css"),
		XPATH("xpath"),
		ACCESSIBILITY_ID("accessibilityId");

		private final String value;

		Locators(String value) {
			this.value = value;
		}

		public String asString() {
			return this.value;
		}
	}
	
	/**
	 * Waits for the specified web element to be clickable and visible with a polling frequency and timeout.
	 * 
	 * @param element  The web element to wait for.
	 * @param timeOut  The maximum time to wait in seconds.
	 */
	public void pollingWaitForApp(WebElement element,int timeOut) {

		Wait<WebDriver> wait = new FluentWait<WebDriver>(getDriver()).withTimeout(Duration.ofSeconds(timeOut))
				.pollingEvery(Duration.ofSeconds(2)).ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class);
		wait.until(ExpectedConditions.elementToBeClickable(element));
		wait.until(ExpectedConditions.visibilityOf(element));
	}
	
	/**
	 * Scrolls to the specified web element.
	 * 
	 * @param ScrolltoThisElement The web element to scroll to.
	 */
	public  void scrollToElemenForApp(WebElement ScrolltoThisElement) {
		try {

			eleIsDisplayed(ScrolltoThisElement);
			Actions act = new Actions(getDriver());
			act.moveToElement(ScrolltoThisElement).build().perform();

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
