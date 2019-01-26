package agent.internal;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import agent.IAgent;
import central.Configuration;
import enums.ConfigType;
import enums.DesktopBrowser;
import enums.Platform;

public class WebAgentFactory {
	static WebDriver driver = null;
	public static final String USERNAME = "mytest20";
	public static final String ACCESS_KEY = "77f6834d-60ce-4830-b882-f02c09374228";
	public static final String accessURL = "https://" + USERNAME + ":" + ACCESS_KEY + "@ondemand.saucelabs.com:443/wd/hub";


	public static IAgent createAgent(Configuration config) throws Exception {
		Platform platform = config.getPlatform();
		DesktopBrowser browser = DesktopBrowser.valueOf(getProperty("browser", config).toUpperCase());
		switch (platform) {
			case DESKTOP_WEB:
				initDriver(config, browser);
//				initDriverinCloud();
//				Dimension resolution = new Dimension(Integer.parseInt(System.getProperty("browser_resolution_width")),Integer.parseInt(System.getProperty("browser_resolution_height")));
//				driver.manage().window().setSize(resolution);
				driver.manage().window().maximize();
//				driver.get(getProperty("app_browser_url", config));
				driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
				break;
			default:
				throw new Exception("Invalid platform, Supported platform is desktop");
		}

		return new DesktopWebAgent(config, driver);
	}


	public static WebDriver initDriverinCloud() throws MalformedURLException {
		DesiredCapabilities caps = DesiredCapabilities.chrome();
		caps.setCapability("platform", "Windows 10");
		caps.setCapability("version", "latest");
		String name = new Object(){}.getClass().getEnclosingMethod().getName();
		caps.setCapability("name",name);
		driver = new RemoteWebDriver(new URL(accessURL), caps);
		return driver;

	}

	private static WebDriver initDriver(Configuration config, DesktopBrowser browser) throws Exception {
		DesiredCapabilities caps = null;
		switch (browser) {
			case CHROME:
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--disable-notifications");
				options.addArguments("--disable-infobars");
//				options.setBinary(System.getProperty("browser_bin_path"));
				caps = DesiredCapabilities.chrome();

				caps.setCapability(ChromeOptions.CAPABILITY, options);
				caps.setCapability("pageLoadStrategy", "none");
				caps.setVersion(ConfigType.PLATFORM_VER.toString());
				setPropertyByOS(browser);
				driver = new ChromeDriver(caps);

				break;
			case EDGE:
				caps = DesiredCapabilities.internetExplorer();
				caps.setVersion(ConfigType.PLATFORM_VER.toString());
				setPropertyByOS(browser);
				driver = new EdgeDriver(caps);
				break;
			case FIREFOX:
				FirefoxProfile Profile = new FirefoxProfile();
				Profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/xml");
				setPropertyByOS(browser);
				driver = new FirefoxDriver();
				break;
			case IE:
				caps = DesiredCapabilities.internetExplorer();
				caps.setVersion(ConfigType.PLATFORM_VER.toString());
				caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				driver = new InternetExplorerDriver(caps);
				break;
			case SAFARI:
				caps = DesiredCapabilities.safari();
				caps.setVersion(ConfigType.PLATFORM_VER.toString());
				driver = new SafariDriver(caps);
				break;
			default:
				break;
		}
		return driver;
	}

	private static void setPropertyByOS(DesktopBrowser browser) throws Exception {
		String driverPath = System.getProperty("user.dir") + "/src/test/resources/drivers/desktop/";
		switch (getOS()) {
			case "OS_WINDOWS":
				switch (browser) {
					case CHROME:
						System.setProperty("webdriver.chrome.bin", System.getProperty("browser_bin_path"));
						System.setProperty("webdriver.chrome.driver", System.getProperty("browser_driver_path"));
						break;
					case FIREFOX:
						System.setProperty("webdriver.firefox.bin", System.getProperty("browser_bin_path"));
						System.setProperty("webdriver.gecko.driver", System.getProperty("browser_driver_path"));
						break;
					case EDGE:
						//System.setProperty("webdriver.edge.driver", driverPath + "MicrosoftWebDriver.exe");
						break;
					default:
						break;
				}
				break;
			case "OS_MAC":
				switch (browser) {
					case CHROME:
						System.setProperty("webdriver.chrome.bin", System.getProperty("browser_bin_path"));
						System.setProperty("webdriver.chrome.driver", System.getProperty("browser_driver_path"));
//				System.setProperty("webdriver.chrome.driver", driverPath + "chromedriver.exe");
						break;
					case FIREFOX:
						System.setProperty("webdriver.firefox.bin", System.getProperty("browser_bin_path"));
						System.setProperty("webdriver.gecko.driver", System.getProperty("browser_driver_path"));
						break;
					default:
						break;
				}
				break;
			case "OS_LINUX":
				switch (browser) {
					case CHROME:
//						System.setProperty("webdriver.chrome.bin", System.getProperty("browser_bin_path"));
//						System.setProperty("webdriver.chrome.driver", System.getProperty("browser_driver_path"));
//						System.setProperty("webdriver.chrome.driver", driverPath + "chromedriver.exe");
						break;
					case FIREFOX:
//						System.setProperty("webdriver.firefox.bin", System.getProperty("browser_bin_path"));
//						System.setProperty("webdriver.gecko.driver", System.getProperty("browser_driver_path"));
						break;
					default:
						break;
				}
				break;
			default:
				throw new Exception("Unknown OS, Please check the Operating System Paramter");
		}
	}

	public static String getOS() {
		String osType = null;
		String osName = System.getProperty("os.name");
		String osNameMatch = osName.toLowerCase();
		if (osNameMatch.contains("linux")) {
			osType = "OS_LINUX";
		} else if (osNameMatch.contains("windows")) {
			osType = "OS_WINDOWS";
		} else if (osNameMatch.contains("solaris") || osNameMatch.contains("sunos")) {
			osType = "OS_SOLARIS";
		} else if (osNameMatch.contains("mac os") || osNameMatch.contains("macos") || osNameMatch.contains("darwin")) {
			osType = "OS_MAC";
		} else {
			osType = "Unknown";
		}
		return osType;
	}

	public static String getProperty(String arg, Configuration config) {
		String ret_val="";

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
				ret_val=config.getValue(ConfigType.APP_URL);
		}
		return ret_val;
	}
}