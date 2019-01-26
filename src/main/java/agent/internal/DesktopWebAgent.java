package agent.internal;

import org.openqa.selenium.WebDriver;

import central.Configuration;

public class DesktopWebAgent extends WebAgent {

	public DesktopWebAgent(Configuration config, WebDriver driver) throws Exception {
		super(config, driver);
	}

}
