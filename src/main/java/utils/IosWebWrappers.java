package utils;

import io.appium.java_client.remote.SupportsContextSwitching;

public class IosWebWrappers extends IosNativeWrappers {

	/**
	 * Launches the Safari browser on an iOS device with the specified device name, URL, and UDID.
	 * 
	 * @param deviceName The name of the iOS device.
	 * @param URL The URL to be opened in the Safari browser.
	 * @param udid The unique device identifier (UDID) of the iOS device.
	 * @return True if the Safari browser is successfully launched, false otherwise.
	 */

	public boolean launchSafariBrowser(String deviceName, String URL, String udid) {
		return launchBrowser("iOS", "Safari", deviceName, URL, udid, "", "", "", "");
	}

	/**
	 * Launches the Safari browser in parallel mode with advanced configurations.
	 * 
	 * @param deviceName The name of the iOS device.
	 * @param URL The URL to be opened in the Safari browser.
	 * @param udid The unique device identifier (UDID) of the iOS device.
	 * @param wdaLocalPort The WebDriverAgent local port.
	 * @param webkitDebugProxyPort The WebKit Debug Proxy (WDP) port.
	 * @return True if the Safari browser is successfully launched, false otherwise.
	 */

	public boolean launchSafariBrowserInParallel(String deviceName, String URL, String udid, String wdaLocalPort,
			String webkitDebugProxyPort) {
		return launchBrowser("iOS", "Safari", deviceName, URL, udid, "", wdaLocalPort, "", webkitDebugProxyPort);
	}

	/**
	 * Deletes cookies in the Safari browser.
	 * 
	 * @return True if the cookies are successfully deleted, false otherwise.
	 */

	public boolean deleteSafariCookies() {
		stopRunningApp("com.apple.Preferences");
		switchToAnotherApp("com.apple.Preferences");
		switchNativeView();
		swipe("down");
		enterValue(getWebElement(Locators.XPATH.asString(), "//*[@label='Search']"), "Safari");
		try {
			click(getWebElement(Locators.XPATH.asString(), "//XCUIElementTypeCell[2]//*[@name='Safari']"));
		} catch (Exception e) {
			click(getWebElement(Locators.XPATH.asString(), "//*[@label='Safari']"));
		}
		sleep(1000);
		swipeUpInAppUntilElementIsVisible(Locators.XPATH.asString(),
				"//*[@value='Clear History and Website Data' and @visible='true']");
		click(getWebElement(Locators.XPATH.asString(),
				"//*[@value='Clear History and Website Data' and @visible='true']"));
		click(getWebElement(Locators.XPATH.asString(), "//*[@label='Clear' or @label='Clear History and Data']"));
		stopRunningApp("com.apple.Preferences");
		switchToAnotherApp("com.apple.mobilesafari");
		switchWebView();
		return true;
	}

	/**
	 * Clicks a given button on the iOS keyboard by name.
	 * 
	 * @param name The name of the button to be clicked.
	 */

	public void clickGivenKeyboardButtonInIosByName(String name) {
		boolean isNative = ((SupportsContextSwitching) getDriver()).getContext().equalsIgnoreCase("NATIVE_APP");
		String context = ((SupportsContextSwitching) getDriver()).getContext();
		if (!isNative) {
			switchNativeView();
		}
		if (isKeyboardShown()) {
			click(getWebElement(Locators.NAME.asString(), name));
		}
		if (!isNative) {
			switchContext(context);
		}
	}

	/**
	 * Clicks a given button on the iOS keyboard by accessibility ID.
	 * 
	 * @param accessId The accessibility ID of the button to be clicked.
	 */

	public void clickGivenKeyboardButtonInIosByAccessibilityId(String accessId) {
		boolean isNative = ((SupportsContextSwitching) getDriver()).getContext().equalsIgnoreCase("NATIVE_APP");
		String context = ((SupportsContextSwitching) getDriver()).getContext();
		if (!isNative) {
			switchNativeView();
		}
		if (isKeyboardShown()) {
			click(getWebElement(Locators.ACCESSIBILITY_ID.asString(), accessId));
		}
		if (!isNative) {
			switchContext(context);
		}
	}

	/**
	 * Clicks a given button on the iOS keyboard by XPath.
	 * 
	 * @param xPath The XPath of the button to be clicked.
	 */
	public void clickGivenKeyboardButtonInIosByXpath(String xPath) {
		boolean isNative = ((SupportsContextSwitching) getDriver()).getContext().equalsIgnoreCase("NATIVE_APP");
		String context = ((SupportsContextSwitching) getDriver()).getContext();
		if (!isNative) {
			switchNativeView();
		}
		if (isKeyboardShown()) {
			click(getWebElement(Locators.XPATH.asString(), xPath));
		}
		if (!isNative) {
			switchContext(context);
		}
	}
}
