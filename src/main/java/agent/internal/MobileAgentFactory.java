/**

 * 

 */
package agent.internal;

import java.io.File;
import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.tools.ant.property.GetProperty;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import agent.IAgent;
import central.Configuration;
import central.AppachhiCentral;
import enums.ConfigType;
import enums.Platform;

public class MobileAgentFactory {
	private static Logger logger = AppachhiCentral.getLogger();

	public static IAgent createAgent(Configuration config) throws Exception {
		AppiumDriver<MobileElement> driver = null;
		DesiredCapabilities caps = null;
		Platform platform = config.getPlatform();
		logger.debug("Populating desired capabilities for : " + platform);
		switch (System.getProperty("platform")) {
		case "IOS":
			caps = DesiredCapabilities.iphone();
			break;
		case "IOS_WEB":
			caps = DesiredCapabilities.safari();
			break;
		case"ANDROID":
			caps = DesiredCapabilities.android();
			break;
		case "ANDROID_WEB":
			caps = DesiredCapabilities.chrome();
			break;
		default:
			throwWrongPlatformException(config);
		}

		initCaps(config, caps);
		populatePlatformSpecificCaps(config, caps);
		logger.debug("Creating mobile driver." + caps.asMap());
		driver = new AppiumDriver<MobileElement>(new URL(System.getProperty("appium_url")), caps);
		logger.debug("Successfully created mobile driver.");
		switch (platform) {
		case IOS:
			return new IOSAppAgent(config, driver);
		case IOS_WEB:
			driver.get(getProperty("app_browser_url", config));
			return new IOSAppAgent(config, driver);
		case ANDROID:
			return new AndroidAppAgent(config, driver);
		case ANDROID_WEB:
			driver.get(getProperty("app_browser_url", config));
			return new AndroidWebAgent(config, driver);
		default:
			logger.error("Failed to Create Agent");
			throwWrongPlatformException(config);
		}

		return null;
	}

	private static void throwWrongPlatformException(Configuration config) {
		String msg = String.format("Unsupported platform for mobile automation", config.getPlatform());
		logger.error(msg);
	}

	private static void initCaps(Configuration config, DesiredCapabilities caps) {
		caps.setCapability(MobileCapabilityType.DEVICE_NAME, System.getProperty("device_name"));
		caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, System.getProperty("platform_version"));
		caps.setCapability("newCommandTimeout", 1500);
		caps.setCapability("udid", System.getProperty("mobile_udid"));
	}

	private static void populateAppDetails(Configuration config, DesiredCapabilities caps) throws Exception {
		File appFile = new File(System.getProperty("app_file_path"));
		caps.setCapability("app", appFile.getAbsolutePath());
		caps.setCapability(MobileCapabilityType.FULL_RESET, true);
//		caps.setCapability(MobileCapabilityType.NO_RESET, config.getValue(ConfigType.NO_RESET));
	}

	private static void populatePlatformSpecificCaps(Configuration config, DesiredCapabilities caps) throws Exception {

		switch (config.getPlatform()) {
		case IOS:
			populateAppDetails(config, caps);
			caps.setCapability(MobileCapabilityType.PLATFORM, Platform.IOS);
			caps.setCapability(IOSMobileCapabilityType.AUTO_ACCEPT_ALERTS, true);
			caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, config.getValue(ConfigType.IOS_AUTOMATION_NAME));
			caps.setCapability(IOSMobileCapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, true);
			caps.setCapability("xcodeConfigFile", config.getValue(ConfigType.XCODE_CONF));
			caps.setCapability("autoAcceptAlerts", true);
			caps.setCapability("autoGrantPermissions", true);
			break;
		case IOS_WEB:
			caps.setCapability(MobileCapabilityType.BROWSER_NAME,  System.getProperty("browser"));
			caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, config.getValue(ConfigType.IOS_AUTOMATION_NAME));
			caps.setCapability("xcodeConfigFile", "conf/config.xcconfig");
			caps.setCapability("nativeWebTap", true);// This is for Site Window Pop-up
			caps.setCapability("autoGrantPermissions", true);
			caps.setCapability(MobileCapabilityType.PLATFORM, Platform.IOS);
			break;
		case ANDROID:
			populateAppDetails(config, caps);
			caps.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.ANDROID);
			caps.setCapability(MobileCapabilityType.PLATFORM, Platform.ANDROID);
			String pkg = config.getValue(ConfigType.APP_PACKAGE);
			caps.setCapability("appPackage", pkg);
			caps.setCapability("appActivity", String.format(config.getValue(ConfigType.ACTIVITY_MAIN), pkg));
			caps.setCapability(MobileCapabilityType.AUTOMATION_NAME,
					config.getValue(ConfigType.ANDROID_AUTOMATION_NAME));
			caps.setCapability("autoGrantPermissions", true);
			caps.setCapability("resetKeyboard", true);
			caps.setCapability("unicodeKeyboard", true);
			break;
		case ANDROID_WEB:
			ChromeOptions options = new ChromeOptions();
			options.addArguments("disable-notifications");
			caps.setCapability(ChromeOptions.CAPABILITY, options);
			caps.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.ANDROID);
			caps.setCapability(MobileCapabilityType.PLATFORM, Platform.ANDROID);
			// caps.setCapability(MobileCapabilityType.AUTOMATION_NAME,
			// config.getValue(ConfigType.ANDROID_AUTOMATION_NAME));
			caps.setCapability(MobileCapabilityType.BROWSER_NAME, getProperty("browser", config) );
			caps.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, config.getValue(ConfigType.ALERT_BEHAVIOR));
			caps.setCapability("autoDismissAlerts", true);
			caps.setCapability("unicodeKeyboard", true);
			caps.setCapability("resetKeyboard", true);
			caps.setCapability("autoGrantPermissions", true);
			break;
		default:
			throwWrongPlatformException(config);
		}
	}
	
	
	public static String getProperty(String arg, Configuration config) {
        String ret_val="";
        
        if ( arg.equalsIgnoreCase("appium_url") ) {
            if ( System.getProperty("appium_url") != null )
                ret_val=System.getProperty("appium_url");
            else
                ret_val=config.getValue(ConfigType.APPIUM_URL);
        } 
        
        if ( arg.equalsIgnoreCase("device_name") ) {
            if ( System.getProperty("device_name") != null )
                ret_val=System.getProperty("device_name");
            else
                ret_val=config.getValue(ConfigType.DEVICE_NAME);
        } 
        
        if ( arg.equalsIgnoreCase("platform_version") ) {
            if ( System.getProperty("platform_version") != null )
                ret_val=System.getProperty("platform_version");
            else
                ret_val=config.getValue(ConfigType.PLATFORM_VER);
        } 
        
        if ( arg.equalsIgnoreCase("mobile_udid") ) {
            if ( System.getProperty("mobile_udid") != null )
                ret_val=System.getProperty("mobile_udid");
            else
                ret_val=config.getValue(ConfigType.UDID);
        }
        
//        if ( arg.equalsIgnoreCase("app_file_path") ) {           It has to include in configuration.java
//            if ( System.getProperty("app_file_path") != null )
//                ret_val=System.getProperty("app_file_path");
//            else
//                ret_val=config.getValue(ConfigType.UDID);
//        } 
        
        if ( arg.equalsIgnoreCase("browser") ) {
            if ( System.getProperty("browser") != null )
                ret_val=System.getProperty("browser");
            else
                ret_val=config.getValue(ConfigType.BROWSER);
        }

		if ( arg.equalsIgnoreCase("app_browser_url") ) {
			if ( System.getProperty("app_browser_url") != null )
				ret_val=System.getProperty("app_browser_url");
			else
				ret_val="https://appachhi.com";
		}

        return ret_val;
    }
}
