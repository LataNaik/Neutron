package agent.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import agent.IAgent;
import central.Configuration;
import central.AppachhiCentral;
import enums.ConfigType;
import enums.Direction;
import enums.Platform;
import utils.JavaUtils;

public abstract class WebAgent implements IAgent {
	protected static Logger logger = AppachhiCentral.getLogger();
	private Configuration config = null;
	private WebDriver driver;
	private WebDriverWait wait;
	private String screenShotsDir = null;
	private String screenDateFormat = null;
	private boolean snapshotsEnabled = false;
	private boolean alwaysTakeSnapshot = false;
	private int scrollPixelCount;
	

	public WebAgent(Configuration config, WebDriver driver) throws Exception {
		this.config = config;
		this.driver = driver;
		this.scrollPixelCount = Integer.parseInt(this.getConfig().getValue(ConfigType.SCROLL_PIXEL_COUNT));
		this.screenShotsDir = AppachhiCentral.INSTANCE.getScreenShotsDir();
		this.screenDateFormat = AppachhiCentral.INSTANCE.getScreenShotTimeStampFormat();
		configureImplicitWait();
		createWaiter();
		String screenFlag = config.getValue(ConfigType.SCREENSHOT_ONLY_ON_ERROR);
		if (screenFlag.toUpperCase().equals("FALSE")) {
			alwaysTakeSnapshot = true;
		}
		snapshotsEnabled = Boolean.parseBoolean(config.getValue(ConfigType.SCREENSHOTS_ON).toLowerCase());
	}

	protected void configureImplicitWait() {
		driver.manage().timeouts().implicitlyWait(Integer.valueOf(config.getValue(ConfigType.IMPLICIT_WAIT)),
				TimeUnit.SECONDS);
	}

	protected void createWaiter() {
		wait = new WebDriverWait(driver, Integer.valueOf(config.getValue(ConfigType.EXPLICIT_WAIT)));
	}

	protected Platform getPlatform() {
		return this.config.getPlatform();
	}

	protected Configuration getConfig() {
		return this.config;
	}

	protected String getScreenShotsDir() {
		return this.screenShotsDir;
	}

	protected boolean shouldAlwaysTakeSnapShot() {
		return this.alwaysTakeSnapshot;
	}

	public WebDriver getWebDriver() throws Exception {
		return driver;
	}

	public WebDriverWait getWaiter() {
		return wait;
	}

	protected void throwAgentException(String msg) throws Exception {
		this.takeSnapShot();
		logger.error(msg);
		throw new Exception(String.format("(%s) %s", this.getPlatform(), msg));
	}

	protected void throwAgentException(Exception e, String additionalMessage) throws Exception {
		throwAgentException(String.format("%s : %s ", additionalMessage, e.getMessage()));
	}

	protected void throwActionException(String action, Exception e) throws Exception {
		throwAgentException(e, String.format("Issue in %s action", action));
	}

	protected void throwActionException(Exception e) throws Exception {
		throwActionException(JavaUtils.getCaller(), e);
	}

	protected void throwUnsupportedActionException() throws Exception {
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		StackTraceElement stackTraceElement = stackTraceElements[1];
		throw new Exception(String.format("%s action is not supported for %s platform",
				stackTraceElement.getMethodName(), this.getPlatform()));
	}

	public void takeConditionalSnapShot() throws Exception {
		if (this.snapshotsEnabled && shouldAlwaysTakeSnapShot()) {
			takeSnapShot();
		}
	}

	@Override
	public void takeSnapShot() throws Exception {
		if (!this.snapshotsEnabled)
			return;
		try {
			TakesScreenshot scrShot = ((TakesScreenshot) driver);
			File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
			File DestFile = new File(
					this.screenShotsDir + File.separator +"eGov-"+ JavaUtils.datetime(screenDateFormat) + ".jpg");
			FileUtils.copyFile(SrcFile, DestFile);
		} catch (Exception e) {
			logger.error("Issue with takeSnapShot : " + e.getMessage());
		}
	}

	@Override
	public void goTo(String url) throws Exception {
		try {
			logger.debug(String.format("Navigating to %s", url));
			driver.get(url);
			this.takeConditionalSnapShot();
		} catch (Exception e) {
			throwAgentException(String.format("Issue while Navigating to %s : %s", url, e.getMessage()));
		}
	}

	@Override
	public void goBack() throws Exception {
		try {
			logger.debug("Navigate one step back in browser history");
			driver.navigate().back();
			this.takeConditionalSnapShot();
		} catch (Exception e) {
			this.throwActionException(e);
		}
	}
	
	@Override
	public void switchToNewWindow() throws Exception {
		try {
			logger.debug("Switching to new Window");
			Set<String> s = driver.getWindowHandles();
			Iterator<String> itr = s.iterator();
			itr.next();
			String w2 = (String) itr.next();
			driver.switchTo().window(w2);
			this.takeConditionalSnapShot();
		} catch (Exception e) {
			this.throwActionException(e);
		}
	}

	@Override
	public void switchToMainWindow() throws Exception {
		try {
			logger.debug("Switching to Default  Window");
			List<String> windows = new ArrayList<String>(driver.getWindowHandles());
			for (int i = 1; i < windows.size(); i++) {
				driver.switchTo().window(windows.get(i));
				driver.close();
			}
			driver.switchTo().window(windows.get(0));
			this.takeConditionalSnapShot();
		} catch (Exception e) {
			this.throwActionException(e);
		}
	}

	@Override
	public void acceptAlert() throws Exception {
		try {
			logger.debug("Accepting the alert ");
			Alert alert = driver.switchTo().alert();
			alert.accept();
			this.takeConditionalSnapShot();
		} catch (Exception e) {
			this.throwActionException(e);
		}
	}

	private void scroll(Direction direction, int count) throws Exception {
		try {
			logger.debug(String.format("Trying to Scroll %s for %d times", direction, count));
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			int spc = 0;
			if (direction == Direction.DOWN) {
				spc = scrollPixelCount;
			} else if (direction == Direction.UP) {
				spc = -scrollPixelCount;
			} else {
				throw new Exception("Scroll left/right is not yet supported.");
			}
			for (int i = 0; i < count; i++) {
				jse.executeScript(String.format("window.scrollBy(0, %d)", spc), "");
			}
			this.takeConditionalSnapShot();
		} catch (Exception e) {
			throwActionException(String.format("scroll%s", direction), e);
		}
	}

	@Override
	public void scrollDown() throws Exception {
		scroll(Direction.DOWN, 1);
	}

	@Override
	public void scrollDown(int count) throws Exception {
		scroll(Direction.DOWN, count);
	}

	@Override
	public void scrollUp() throws Exception {
		scroll(Direction.UP, 1);
	}

	@Override
	public void scrollUp(int count) throws Exception {
		scroll(Direction.UP, count);
	}

	protected boolean isWebView() {
		// Irrelevant for web agent. Should be overriden by MobileAgent, so that
		// assertPageLoad
		// logic can be in one place for all agents.
		return true;
	}

	@Override
	public void assertPageLoad() throws Exception {
		if (Platform.isWebPlatform(this.getPlatform()) || isWebView()) {
			ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					return ((String) ((JavascriptExecutor) driver).executeScript("return document.readyState"))
							.equals("complete");
				}
			};
			this.getWaiter().until(pageLoadCondition);
		}
	}

	@Override
	public AppiumDriver<MobileElement> getMobileDriver() throws Exception {
		throwUnsupportedActionException();
		return null;
	}

	public void quit() {
		this.driver.quit();
	}

	@Override
	public void swipeUp() throws Exception {
		throwUnsupportedActionException();
	}

	@Override
	public void swipeUp(int count) throws Exception {
		throwUnsupportedActionException();
	}

	@Override
	public void swipeDown() throws Exception {
		throwUnsupportedActionException();
	}

	@Override
	public void swipeDown(int count) throws Exception {
		throwUnsupportedActionException();
	}

	public void switchToNativeView() throws Exception {
		throwUnsupportedActionException();
	}

	public void switchToWebView() throws Exception {
		throwUnsupportedActionException();
	}
	
	@Override
	public void swipeDownTillElement(String elementName) throws Exception {
		throwUnsupportedActionException();
	}

	@Override
	public void switchToIFrame() throws Exception {
			logger.info("Switching to iFrame");
			List<WebElement> listFrames = driver.findElements(By.tagName("iFrame"));
			//logger.info("iframe "+ listFrames);
			int frameIndex=listFrames.size();
			logger.info("frameIndex "+frameIndex);
			driver.switchTo().frame(listFrames.get(frameIndex-1));
	}

	@Override
	public void switchToDefaultFrame() throws Exception {
		logger.info("Switching to Default Frame");
		driver.switchTo().defaultContent();
	}
}
