package agent.internal;

import java.time.Duration;
import java.util.Set;

import org.openqa.selenium.Dimension;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import central.Configuration;
import enums.ConfigType;
import enums.Direction;
import enums.MobileView;
import enums.Platform;

public abstract class MobileAgent extends WebAgent {
	private AppiumDriver<MobileElement> driver;
	private Long swipeWait;
	private Float swipeTopFraction;
	private Float swipeDownFraction;

	public MobileAgent(Configuration config, AppiumDriver<MobileElement> driver) throws Exception {
		super(config, driver);
		this.driver = driver;
		swipeWait = Long.parseLong(config.getValue(ConfigType.SWIPE_WAIT));
		swipeTopFraction = Float.parseFloat(config.getValue(ConfigType.SWIPE_TOP_FRACTION));
		swipeDownFraction = Float.parseFloat(config.getValue(ConfigType.SWIPE_DOWN_FRACTION));
	}

	public AppiumDriver<MobileElement> getMobileDriver() {
		return this.driver;
	}

	@Override
    public void takeSnapShot() throws Exception {
        try {
            if (getPlatform().equals(Platform.ANDROID) || getPlatform().equals(Platform.IOS) ) {
                String context = driver.getContext();
                if (context.contains(MobileView.NATIVE_APP.toString())) {
                    super.takeSnapShot();
                } else {
                    switchToNativeView();
                    super.takeSnapShot();
                    switchToWebView();
                }
            } else {
                super.takeSnapShot();                
            }
        } catch (Exception e) {
            logger.error("Issue with takeSnapShot : " + e.getMessage());
        }
    }

	protected boolean isWebView() {
		return driver.getContext().contains(MobileView.WEBVIEW.toString());
	}

	protected boolean isNativeView() {
		return driver.getContext().contains(MobileView.NATIVE_APP.toString());
	}

	private void validateSwipeSupport() throws Exception {
		if (Platform.isMobileWebPlatform(this.getPlatform()) || isWebView()) {
			throwAgentException("Swipe actions are not supported for Web View.");
		}
	}

	protected void validateScrollSupport() throws Exception {
		if (Platform.isMobileNativePlatform(this.getPlatform()) && isNativeView()) {
			throwAgentException("Scroll actions are not supported for Native View.");
		}
	}

	private void switchToView(String view) throws Exception {
		try {
			logger.debug(String.format("Attempt to switch to %s view", view));
			Set<String> contextNames = driver.getContextHandles();
			logger.debug("Context Found : " + contextNames);
			driver.context(view);
			this.takeConditionalSnapShot();
		} catch (Exception e) {
			this.throwAgentException(e, String.format("Issue in switchToView for %s view", view));
		}
	}

	public void switchToNativeView() throws Exception {
		switchToView(MobileView.NATIVE_APP.toString());
	}

	public void switchToWebView() throws Exception {
		switchToView(String.format("%s_%s", MobileView.WEBVIEW.toString(),
				this.getConfig().getValue(ConfigType.APP_PACKAGE)));
	}

	private void swipe(Direction direction, int count, float startFraction, float endFraction) throws Exception {
		try {
			validateSwipeSupport();
			logger.debug(String.format("Trying to Swipe %s for %d times", direction, count));
			Dimension size = driver.manage().window().getSize();
			int starty = (int) (size.height * startFraction);
			int endy = (int) (size.height * endFraction);
			int width = size.width / 2;
			for (int i = 0; i < count; i++) {
				new TouchAction(driver).press(width, starty).waitAction(Duration.ofSeconds(swipeWait))
						.moveTo(width, endy).release().perform();
			}
			this.takeConditionalSnapShot();
		} catch (Exception e) {
			throwActionException(String.format("swipe%s", direction.toString()), e);
		}
	}

	@Override
	public void swipeUp(int count) throws Exception {
		swipe(Direction.UP, count, swipeTopFraction, swipeDownFraction);
	}

	@Override
	public void swipeUp() throws Exception {
		swipeUp(1);
	}

	@Override
	public void swipeDown(int count) throws Exception {
		swipe(Direction.DOWN, count, swipeDownFraction, swipeTopFraction);
	}

	@Override
	public void swipeDown() throws Exception {
		swipeDown(1);
	}

	@Override
	public void scrollUp() throws Exception {
		validateScrollSupport();
		try {
			super.scrollUp();
		} catch (Exception e) {
			throwActionException(e);
		}
	}

	@Override
	public void scrollDown() throws Exception {
		validateScrollSupport();
		try {
			super.scrollDown();
		} catch (Exception e) {
			throwActionException(e);
		}
	}
	
	@Override
	public void swipeDownTillElement(String elementName) throws Exception {
		//method is implemented in page class
	}

}
