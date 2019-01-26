package agent.internal;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

public interface IDriverActions {

	AppiumDriver<MobileElement> getMobileDriver() throws Exception;

	WebDriverWait getWaiter();

	WebDriver getWebDriver() throws Exception;

	void quit();

}
