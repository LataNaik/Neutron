/**
 * 
 */
package agent.internal;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import central.Configuration;

public abstract class IOSAgent extends MobileAgent {

	public IOSAgent(Configuration config, AppiumDriver<MobileElement> driver) throws Exception {
		super(config, driver);
	}

}
