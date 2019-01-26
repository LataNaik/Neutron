package control;

import java.util.List;

import org.openqa.selenium.Keys;

public interface IControl {
	void click() throws Exception;

	void enterText(String text) throws Exception;

	void enterText(Keys text) throws Exception;
	
	void uploadFile(String text) throws Exception;

	String getText() throws Exception;

	void assertText(String text) throws Exception;

	boolean isVisible() throws Exception;

	void assertVisible() throws Exception;

	boolean isEnabled() throws Exception;

	void assertEnabled() throws Exception;

	void scrollTo() throws Exception;

	void selectDropDownByValue(String value) throws Exception;

	void waitUntilVisible() throws Exception;

	void waitUntilClickable() throws Exception;

	IControl getControl(String name) throws Exception;

	List<IControl> getControls(String name) throws Exception;
	
	void hover() throws Exception;

	void clear() throws Exception;
}
