package utils;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServerHasNotBeenStartedLocallyException;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class GenericWrappers extends IosWebWrappers {
	
	/**
	 * Starts the Appium server.
	 * This method sets up and starts the Appium server with the specified configuration.
	 */
	public void startAppiumServer() {
		HashMap<String, String> environment = new HashMap<String, String>();
		environment.put("PATH",
				"/usr/local/bin:/Users/mac/Library/Android/sdk:/Users/mac/Library/Android/sdk/platform-tools:/Users/mac/Library/Android/sdk/tools:/Users/mac/Library/Android/sdk/build-tools"
						+ System.getenv("PATH"));
		environment.put("ANDROID_HOME", "/Users/" + System.getProperty("user.name")+ "/Library/Android/sdk");
		environment.put("JAVA_HOME",
		"/Library/Java/JavaVirtualMachines/jdk-20.jdk/Contents/Home");
		builder = new AppiumServiceBuilder();
		builder.usingAnyFreePort();
		builder.usingDriverExecutable(new File("/usr/local/bin/node"));
		builder.withAppiumJS(new File("//usr/local/lib/node_modules/appium/build/lib/main.js"));
		builder.withEnvironment(environment);
		builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
		builder.withLogFile(createFile());
			
		
		service = AppiumDriverLocalService.buildService(builder);
		service.start();
		if (service == null || !service.isRunning()) {
			System.out.println("Appium server not started. ABORT!!!");
			throw new AppiumServerHasNotBeenStartedLocallyException("Appium server not started. ABORT!!!");
		}
		serverURL = String.valueOf(service.getUrl());
	}

	/**
	 * Stops the Appium server.
	 * This method stops the currently running Appium server.
	 */
	public void stopAppiumServer() {
		service.stop();
	}
	
	/**
	 * Gets the current date and time in the format "dd-MM-yyyy-HH:mm".
	 *
	 * @return A string representing the current date and time.
	 */
	public static String getTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm");
		Date date = new Date();
		// System.out.println(formatter.format(date));
		return formatter.format(date);
	}
	
    /**
     * Creates a new file for logging Appium server output.
     * The file name includes the current date and time.
     *
     * @return A File object representing the newly created log file.
     */
    public static File createFile() {
        File logFile = null;
        try {
            // Construct file name with current date and time
            String fileName =  "appiumserver_logs_"+getTime()+".log";
            logFile = new File(System.getProperty("user.dir") + File.separator+ "appiumserver_logs"+ File.separator + fileName);

            // Create parent directories if they do not exist
            File parentDirectory = logFile.getParentFile();
            if (!parentDirectory.exists()) {
                parentDirectory.mkdirs();
            }

            // Create the file if it does not exist
            if (logFile.createNewFile()) {
                System.out.println("File created: " + logFile.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException ex) {
            System.out.println("An error occurred.");
            ex.printStackTrace();
        }
        return logFile;
    }
    
}
