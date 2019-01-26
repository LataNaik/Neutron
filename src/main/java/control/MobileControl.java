package control;

import io.appium.java_client.MobileElement;
import page.IPage;

public class MobileControl extends WebControl {
	private MobileElement mobileElement = null;

	public MobileControl(String name, IPage page, MobileElement element) {
		super(name, page, element);
		this.mobileElement = element;
	}

}
