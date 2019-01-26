package agent.internal;

public interface IWindowActions {

	void takeSnapShot() throws Exception;

	void swipeUp() throws Exception;

	void swipeUp(int count) throws Exception;

	void swipeDown() throws Exception;

	void swipeDown(int count) throws Exception;

	void goTo(String url) throws Exception;

	void goBack() throws Exception;

	void switchToNativeView() throws Exception;

	void switchToWebView() throws Exception;

	void switchToNewWindow() throws Exception;

	void switchToMainWindow() throws Exception;

	void acceptAlert() throws Exception;

	void scrollUp() throws Exception;

	void scrollUp(int count) throws Exception;

	void scrollDown() throws Exception;

	void scrollDown(int count) throws Exception;
	
	void swipeDownTillElement(String elementName) throws Exception;

	void assertPageLoad() throws Exception;

	void switchToIFrame() throws Exception;

	void switchToDefaultFrame() throws Exception;
}
