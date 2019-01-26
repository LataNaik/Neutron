package agent.internal;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import central.Configuration;

public class IOSWebAgent extends IOSAgent {

	AppiumDriver<MobileElement> driver;

	public IOSWebAgent(Configuration config, AppiumDriver<MobileElement> driver) throws Exception {
		super(config, driver);
	}

}
