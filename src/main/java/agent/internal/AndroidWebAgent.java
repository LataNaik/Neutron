package agent.internal;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import central.Configuration;

public class AndroidWebAgent extends AndroidAgent {

	public AndroidWebAgent(Configuration config, AppiumDriver<MobileElement> driver) throws Exception {
		super(config, driver);
	}

}
