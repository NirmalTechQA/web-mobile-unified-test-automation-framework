package utils;

public class AndroidWebWrappers extends AndroidNativeWrappers {

	/**
	 * Launches the Chrome browser on an Android device and navigates to the specified URL.
	 * 
	 * @param deviceName The name of the Android device.
	 * @param URL The URL to navigate to.
	 * @return True if the browser is successfully launched, false otherwise.
	 */

	public boolean launchChromeBrowser(String deviceName, String URL) {
		return launchBrowser("Android", "Chrome", deviceName, URL, "", "", "", "", "");
	}

	/**
	 * Launches the Chrome browser on an Android device in parallel mode with advanced configurations.
	 * 
	 * @param deviceName The name of the Android device.
	 * @param URL The URL to navigate to.
	 * @param udid The unique device identifier (UDID) of the device.
	 * @param chromeDriverPort The port number for ChromeDriver.
	 * @param mjpegServerPort The port number for the MJPEG server.
	 * @return True if the browser is successfully launched, false otherwise.
	 */

	public boolean launchChromeBrowserInParallel(String deviceName, String URL, String udid, String chromeDriverPort,
			String mjpegServerPort) {
		return launchBrowser("Android", "Chrome", deviceName, URL, udid, chromeDriverPort, "", mjpegServerPort, "");
	}

	/**
	 * Deletes all cookies from the Chrome browser instance.
	 * 
	 * @return True if the cookies are successfully deleted, false otherwise.
	 */

	public boolean deleteChromeCookies() {
		getDriver().manage().deleteAllCookies();
		return true;
	}

}
