package utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import io.appium.java_client.AppiumDriver;

import java.util.HashMap;
import java.util.Map;

public class IosNativeWrappers extends AndroidWebWrappers {

	/**
	 * Launches an iOS app with the specified device, UDID, Xcode organization ID, bundle ID, and app file.
	 * 
	 * @param deviceName The name of the iOS device.
	 * @param udid The unique device identifier (UDID) of the iOS device.
	 * @param xcodeOrgId The Xcode organization ID.
	 * @param bundleId The bundle ID of the iOS app to be launched.
	 * @param app The path to the iOS app file (.ipa) to be installed.
	 * @return True if the app is successfully launched, false otherwise.
	 */
	public boolean launchIosApp(String deviceName, String udid, String xcodeOrgId, String bundleId, String app) {
		return launchApp("iOS", deviceName, udid, "", "", "XCUITest", "", "", xcodeOrgId, "iPhone Developer", bundleId,
				app, "", "","","");
	}

	/**
	 * Launches an iOS app in parallel mode with advanced configurations.
	 * 
	 * @param deviceName The name of the iOS device.
	 * @param udid The unique device identifier (UDID) of the iOS device.
	 * @param xcodeOrgId The Xcode organization ID.
	 * @param bundleId The bundle ID of the iOS app to be launched.
	 * @param app The path to the iOS app file (.ipa) to be installed.
	 * @param wdaLocalPort The WebDriverAgent local port.
	 * @return True if the app is successfully launched, false otherwise.
	 */
	public boolean launchIosAppInParallel(String deviceName, String udid, String xcodeOrgId, String bundleId,
			String app, String wdaLocalPort) {
		return launchApp("iOS", deviceName, udid, "", "", "XCUITest", "", "", xcodeOrgId, "iPhone Developer", bundleId,
				app, "", wdaLocalPort,"","");
	}

	/**
	 * Chooses the next option in the picker wheel using the specified locator and locator value.
	 * 
	 * @param locator The locator strategy (e.g., "id", "name").
	 * @param locatorValue The value of the locator.
	 * @return True if the next option is successfully chosen, false otherwise.
	 */
	public boolean chooseNextOptionInPickerWheel(String locator, String locatorValue) {
		HashMap<String, Object> params = new HashMap<>();
		params.put("order", "next");
		params.put("offset", 0.15);
		params.put("element", getWebElement(locator, locatorValue));
		getDriver().executeScript("mobile: selectPickerWheelValue", params);
		return true;
	}

	/**
	 * Chooses the next option in the picker wheel using the specified WebElement.
	 * 
	 * @param ele The WebElement representing the picker wheel.
	 * @return True if the next option is successfully chosen, false otherwise.
	 */
	public boolean chooseNextOptionInPickerWheel(WebElement ele) {
		HashMap<String, Object> params = new HashMap<>();
		params.put("order", "next");
		params.put("offset", 0.15);
		params.put("element", ele);
		getDriver().executeScript("mobile: selectPickerWheelValue", params);
		return true;
	}

	/**
	 * Chooses the previous option in the picker wheel using the specified locator and locator value.
	 * 
	 * @param locator The locator strategy (e.g., "id", "name").
	 * @param locatorValue The value of the locator.
	 * @return True if the previous option is successfully chosen, false otherwise.
	 */
	public boolean choosePreviousOptionInPickerWheel(String locator, String locatorValue) {
		HashMap<String, Object> params = new HashMap<>();
		params.put("order", "previous");
		params.put("offset", 0.15);
		params.put("element", getWebElement(locator, locatorValue));
		getDriver().executeScript("mobile: selectPickerWheelValue", params);
		return true;
	}

	/**
	 * Chooses the previous option in the picker wheel using the specified WebElement.
	 * 
	 * @param ele The WebElement representing the picker wheel.
	 * @return True if the previous option is successfully chosen, false otherwise.
	 */
	public boolean choosePreviousOptionInPickerWheel(WebElement ele) {
		HashMap<String, Object> params = new HashMap<>();
		params.put("order", "previous");
		params.put("offset", 0.15);
		params.put("element", ele);
		getDriver().executeScript("mobile: selectPickerWheelValue", params);
		return true;
	}
	
	/**
	 * Sends the Enter key (`\n`) to the specified WebElement.
	 * This is commonly used in iOS automation to simulate pressing the Return key
	 * on the iOS keyboard after entering text into a field.
	 *
	 * @param element The WebElement (typically a MobileElement) to send the Enter key to.
	 *                Should not be null and should be focused or ready to receive input.
	 */
	public void pressEnterWithSendKeys(WebElement element) {
	    if (element != null) {
	        element.sendKeys("\n");
	    } else {
	        System.out.println("Element is null. Cannot send Enter key.");
	    }
	}
	
	/**
	 * Executes a mobile: type command with the Enter key (`\n`) using Appium's XCUITest driver.
	 * This method simulates typing the Enter/Return key on the iOS keyboard without needing a focused element.
	 *
	 * @param driver The AppiumDriver instance currently controlling the iOS device.
	 *               It must be initialized and connected to an active iOS session.
	 */
	public void pressEnterWithMobileType(AppiumDriver driver) {
	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    Map<String, Object> params = new HashMap<>();
	    params.put("keys", "\n");
	    js.executeScript("mobile: type", params);
	}

}
