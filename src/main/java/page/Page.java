package page;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import io.appium.java_client.MobileDriver;
import io.appium.java_client.PressesKeyCode;
import io.appium.java_client.android.AndroidKeyCode;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import agent.IAgent;
import central.Configuration;
import central.AppachhiCentral;
import control.Control;
import control.IControl;
import enums.Platform;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import pagedef.Identifier;
import pagedef.PageDef;

public abstract class Page implements IPage {
	protected static Logger logger = AppachhiCentral.getLogger();
	protected Configuration config = null;
	private IAgent agent = null;
	private PageDef pageDef = null;
	private Platform platform = null;
	private Map<String, String> testData = null;

	public Page(Configuration config, IAgent agent, Map<String, String> testData) throws Exception {
		this.testData = testData;
		this.config = config;
		this.platform = config.getPlatform();
		String currentPage = pageName();
		pageDef = AppachhiCentral.INSTANCE.getPageDef(currentPage);
		this.agent = agent;
	}

	protected Configuration getConfig() {
		return this.config;
	}

	public IAgent getAgent() {
		return this.agent;
	}

	protected Map<String, String> getTestData() {
		return this.testData;
	}

	public IControl getControl(String name) throws Exception {
		IControl control = Control.createControl(this, name, getIdentifier(name));
		//logger.info(name +"value is "+control);
		return control;
	}

	public boolean getControl(String name, String controlFunction) throws Exception {

		boolean result = false;
		IControl control = Control.createControl(this, name, getIdentifier(name));
		logger.info(name +"value is "+control);
		if ( control != null ) {
			if ( controlFunction.equalsIgnoreCase("isVisible") )
				result = control.isVisible();
			else if ( controlFunction.equalsIgnoreCase("isEnabled") ) 
				result = control.isEnabled();
		}
		return result;
	}

	public List<IControl> getControls(String name) throws Exception {
		return Control.createControls(this, name, getIdentifier(name));
	}

	@Override
	public void takeSnapShot() throws Exception {
		this.agent.takeSnapShot();
	}

	@Override
	public void swipeUp() throws Exception {
		this.agent.swipeUp();
	}

	@Override
	public void swipeUp(int count) throws Exception {
		this.agent.swipeUp(count);
	}

	@Override
	public void swipeDown() throws Exception {
		this.agent.swipeDown();
	}

	@Override
	public void swipeDown(int count) throws Exception {
		this.agent.swipeDown(count);
	}

	@Override
	public void goTo(String url) throws Exception {
		this.agent.goTo(url);
	}

	@Override
	public void goBack() throws Exception {
		this.agent.goBack();
	}

	@Override
	public void switchToNewWindow() throws Exception {
		this.agent.switchToNewWindow();
	}

	@Override
	public void switchToMainWindow() throws Exception {
		this.agent.switchToMainWindow();
	}

	@Override
	public void acceptAlert() throws Exception {
		this.agent.acceptAlert();
	}

	@Override
	public void scrollUp() throws Exception {
		this.agent.scrollUp();
	}

	@Override
	public void scrollUp(int count) throws Exception {
		this.agent.scrollUp(count);
	}

	@Override
	public void scrollDown() throws Exception {
		this.agent.scrollDown();
	}

	@Override
	public void scrollDown(int count) throws Exception {
		this.agent.scrollDown(count);
	}

	@Override
	public Platform getPlatform() {
		return this.platform;
	}

	@Override
	public Identifier getIdentifier(String name) {
		return this.pageDef.getIdentifier(this.platform, name);
	}

	@Override
	public void switchToNativeView() throws Exception {
		this.agent.switchToNativeView();
	}

	@Override
	public void switchToWebView() throws Exception {
		this.agent.switchToWebView();
	}

	@Override
	public void assertPageLoad() throws Exception {
		this.agent.assertPageLoad();
	}

	@Override
	public void swipeDownTillElement(String elementName) throws Exception {
		this.agent.swipeDownTillElement(elementName);
		String msg = String.format("Scrolling till element - ", elementName);
		try {
			logger.debug(msg);
			for (int i = 0; i < 50; i++) {
				try {
					getControl(elementName);
					break;
				} catch (Exception e) {
					this.getAgent().swipeDown();
				}

			}
			this.getAgent().takeConditionalSnapShot();
		} catch (Exception e) {
			logger.debug(String.format("Failure in %s", msg));
		}
	}

	public void androidWebView() throws Exception {
		if (this.getPlatform() == Platform.ANDROID) {
			this.getAgent().switchToWebView();
		}
	}

	public void androidNativeView() throws Exception {
		if (this.getPlatform() == Platform.ANDROID) {
			this.getAgent().switchToNativeView();
		}
	}

	public void switchToIFrame() throws Exception{
		this.agent.switchToIFrame();
	}

	public void switchToDefaultFrame() throws Exception{
		this.agent.switchToDefaultFrame();
	}

	public void selectOptionFromDropDownByEnter(String xpathValue) throws Exception{
		String xpath="//*[contains(text(),'" + xpathValue + "')]";
		getAgent().getWebDriver().findElement(By.xpath(xpath)).click();
	}

	public void selectOptionFromDropDownList(String cssValue) throws Exception{
		String css="li[data-value='"+cssValue+"']";
		getAgent().getWebDriver().findElement(By.cssSelector(css)).click();
	}

	public void scrollDownTillElement(String ele) throws Exception{
		do{
			scrollDown();
		}
		while (!getControl(ele).isVisible());

	}
}
