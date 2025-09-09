package utils;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.SupportsContextSwitching;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Set;

public class CommonWebWrappers extends CommonNativeWrappers {

	/**
	 * Launches the mobile web browser with the specified configuration.
	 *
	 * @param platformName         The platform name (e.g., "Android", "iOS").
	 * @param browserName          The name of the browser to launch.
	 * @param deviceName           The name of the device.
	 * @param URL                  The URL to navigate to after launching the browser.
	 * @param udid                 The Unique Device Identifier (UDID) of the device.
	 * @param chromeDriverPort     The port for ChromeDriver (for Android).
	 * @param wdaLocalPort         The port for WebDriverAgent (for iOS).
	 * @param mjpegServerPort      The port for MJPEG server (for Android).
	 * @param webkitDebugProxyPort The port for WebKit Debug Proxy (for iOS).
	 * @return {@code true} if the browser is successfully launched, {@code false} otherwise.
	 */
	public boolean launchBrowser(String platformName, String browserName, String deviceName, String URL, String udid,
			String chromeDriverPort, String wdaLocalPort, String mjpegServerPort, String webkitDebugProxyPort) {
		try {
			DesiredCapabilities dc = new DesiredCapabilities();
			// To pass the Unique Device Identifier
			if (!udid.equals(""))
				dc.setCapability("udid", udid);
			// Android
			// For web app parallel testing
			if (!chromeDriverPort.equals(""))
				dc.setCapability("chromedriverPort", chromeDriverPort);
			// For web app parallel testing
			if (!mjpegServerPort.equals(""))
				dc.setCapability("mjpegServerPort", mjpegServerPort);
			// iOS
			// For web app parallel testing
			if (!wdaLocalPort.equals(""))
				dc.setCapability("wdaLocalPort", wdaLocalPort);
			// Mandatory desired capabilities
			dc.setCapability("browserName", browserName);
			dc.setCapability("deviceName", deviceName);
			// dc.setCapability("platformName", platformName);
			// Comment the below line based on need
			if (useExistingApp) {
				dc.setCapability("noReset", true);
				dc.setCapability("forceAppLaunch", true);
				dc.setCapability("shouldTerminateApp", true);
			}
			URI uri = new URI(serverURL);
			if (platformName.equalsIgnoreCase("Android")) {
				dc.setCapability("automationName", "UiAutomator2");
				dc.setCapability("autoAcceptAlerts", true);
				dc.setCapability("autoGrantPermissions",true);
				driver.set(new AndroidDriver(uri.toURL(), dc));
			} else if (platformName.equalsIgnoreCase("iOS")) {
				if (!webkitDebugProxyPort.equals(""))
					dc.setCapability("webkitDebugProxyPort", webkitDebugProxyPort);
				// Comment the below line based on need
				dc.setCapability("autoAcceptAlerts", true);
				dc.setCapability("startIWDP", true);
				dc.setCapability("nativeWebTap", true);
				dc.setCapability("automationName", "XCUITest");
				driver.set(new IOSDriver(uri.toURL(), dc));
			}
			getDriver().get(URL);
			getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		return true;
	}

	/**
	 * Switches the context to WEB-VIEW (Note: Not recommended for Android).
	 */
	public void switchWebView() {
		try {
			Set<String> contextNames = ((SupportsContextSwitching) getDriver()).getContextHandles();
			for (String contextName : contextNames) {
				if (contextName.contains("WEBVIEW")) {
					((SupportsContextSwitching) getDriver()).context(contextName);
					break;
				}
			}
			getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Scrolls down in the browser by the specified number of pixels.
	 *
	 * @param pixelsToBeScrolled The number of pixels to scroll down.
	 * @return {@code true} if the scrolling operation is successful, {@code false} otherwise.
	 */
	public boolean scrollDownInBrowser(int pixelsToBeScrolled) {
		try {
			JavascriptExecutor jse = getDriver();
			jse.executeScript("window.scrollBy(0," + pixelsToBeScrolled + "\")", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Navigates back in the browser history.
	 *
	 * @return {@code true} if the navigation is successful, {@code false} otherwise.
	 */
	public boolean navigateBackInBrowser() {
		try {
			getDriver().navigate().back();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Loads the given URL in the browser.
	 *
	 * @param url The URL to load.
	 * @return {@code true} if the URL is successfully loaded, {@code false} otherwise.
	 */
	public boolean loadURL(String url) {
		getDriver().get(url);
		return true;
	}

	/**
	 * Switches to the last window among the open windows.
	 *
	 * @return {@code true} if the switch is successful, {@code false} otherwise.
	 */
	public boolean switchToLastWindow() {
		sleep(5000);
		Set<String> windowHandles = getDriver().getWindowHandles();
		for (String string : windowHandles) {
			getDriver().switchTo().window(string);
		}
		return true;
	}

	/**
	 * Switches to the first window among the open windows.
	 *
	 * @return {@code true} if the switch is successful, {@code false} otherwise.
	 */
	public boolean switchToFirstWindow() {
		sleep(5000);
		Set<String> windowHandles = getDriver().getWindowHandles();
		for (String string : windowHandles) {
			getDriver().switchTo().window(string);
			break;
		}
		return true;
	}
}
