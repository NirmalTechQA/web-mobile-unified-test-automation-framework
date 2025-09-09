package utils;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.StartsActivity;
import io.appium.java_client.android.connection.ConnectionStateBuilder;
import io.appium.java_client.android.connection.HasNetworkConnection;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.android.nativekey.PressesKey;

import java.util.HashMap;

import com.google.common.collect.ImmutableMap;

public class AndroidNativeWrappers extends CommonWebWrappers {

	/**
	 * Launches an Android app with the specified package, activity, and other parameters.
	 * 
	 * @param deviceName The name of the Android device.
	 * @param appPackage The package name of the app to be launched.
	 * @param appActivity The main activity of the app to be launched.
	 * @param automationName The name of the automation framework.
	 * @param app The path to the app file (.apk) to be installed.
	 * @return True if the app is successfully launched, false otherwise.
	 */
	public boolean launchAndroidApp(String deviceName, String appPackage, String appActivity, String automationName,
			String app) {
		return launchApp("Android", deviceName, "", appPackage, appActivity, automationName, "", "", "", "", "", app,
				"", "","","");
	}

	/**
	 * Launches an Android app in parallel mode with advanced configurations.
	 * 
	 * @param deviceName The name of the Android device.
	 * @param udid The unique device identifier (UDID) of the device.
	 * @param appPackage The package name of the app to be launched.
	 * @param appActivity The main activity of the app to be launched.
	 * @param automationName The name of the automation framework.
	 * @param chromeDriverPort The port number for ChromeDriver.
	 * @param mjpegServerPort The port number for the MJPEG server.
	 * @param systemPort The system port number for the appium server.
	 * @param app The path to the app file (.apk) to be installed.
	 * @return True if the app is successfully launched, false otherwise.
	 */
	public boolean launchAndroidAppInParallel(String deviceName, String udid, String appPackage, String appActivity,
			String automationName, String chromeDriverPort, String mjpegServerPort, String systemPort, String app) {
		return launchApp("Android", deviceName, udid, appPackage, appActivity, automationName, chromeDriverPort,
				systemPort, "", "", "", app, mjpegServerPort, "","","");
	}

	/**
	 * Starts an app using the specified package and activity.
	 * 
	 * @param appPackage The package name of the app to start.
	 * @param appActivity The main activity of the app to start.
	 * @return True if the app is successfully started, false otherwise.
	 */
	public boolean startAnAppUsingActivity(String appPackage, String appActivity) {
		try {
			//            ((StartsActivity) getDriver()).startActivity(new Activity(appPackage, appActivity));
			HashMap<String, Object> params = new HashMap<>();
			params.put("intent", appPackage + "/" + appActivity);
			getDriver().executeScript("mobile: startActivity", params);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Presses the Enter key on the Android device.
	 * 
	 * @return True if the Enter key is successfully pressed, false otherwise.
	 */

	public boolean pressEnter() {
		try {
	        ((PressesKey) getDriver()).pressKey(new KeyEvent(AndroidKey.ENTER));
	        return true;
	    } catch (Exception e) {
	        System.out.println("Error pressing Enter key: " + e.getMessage());
	        return false;
	    }
	}

	/**
	 * Presses the Back key on the Android device.
	 * 
	 * @return True if the Back key is successfully pressed, false otherwise.
	 */
	public boolean pressBack() {
		((PressesKey) getDriver()).pressKey(new KeyEvent(AndroidKey.BACK));
		return true;
	}

	/**
	 * Shows the notification menu on the Android device.
	 */
	public void showNotificationMenu() {
		((AndroidDriver) getDriver()).openNotifications();
	}

	/**
	 * Hides the notification menu on the Android device.
	 */
	public void hideNotificationMenu() {
		pressBack();
	}

	/**
	 * Turns off mobile data and WIFI on the Android device.
	 * 
	 * @return True if mobile data and WIFI is successfully turned off, false otherwise.
	 */
	public boolean dataOffInAndroid() {
		((HasNetworkConnection) getDriver()).setConnection(new ConnectionStateBuilder().withWiFiDisabled().build());
		((HasNetworkConnection) getDriver()).setConnection(new ConnectionStateBuilder().withDataDisabled().build());
		return true;
	}

	/**
	 * Turns on mobile data and WIFI on the Android device.
	 * 
	 * @return True if mobile data and WIFI is successfully turned on, false otherwise.
	 */
	public boolean dataOnInAndroid() {
		((HasNetworkConnection) getDriver()).setConnection(new ConnectionStateBuilder().withWiFiEnabled().build());
		((HasNetworkConnection) getDriver()).setConnection(new ConnectionStateBuilder().withDataEnabled().build());
		return true;
	}

	/**
	 * Retrieves the current activity running on the Android device.
	 * 
	 * @return The name of the current activity.
	 */
	public String getCurrentActivity() {
		return ((StartsActivity) getDriver()).currentActivity();
	}

	/**
	 * Retrieves the package name of the current app running on the Android device.
	 * 
	 * @return The package name of the current app.
	 */
	public String getCurrentAppPackage() {
		return ((StartsActivity) getDriver()).getCurrentPackage();
	}
	
	/**
	 * Presses the Enter key on the Android device using ADB.
	 * 
	 * @return True if the Enter key is successfully pressed, false otherwise.
	 */
	public boolean pressEnterUsingADB() {
	    try {
	        getDriver().executeScript("mobile: shell", ImmutableMap.of("command", "input keyevent 66"));
	        return true;
	    } catch (Exception e) {
	        System.out.println("Error pressing Enter via ADB: " + e.getMessage());
	        return false;
	    }
	}

}
