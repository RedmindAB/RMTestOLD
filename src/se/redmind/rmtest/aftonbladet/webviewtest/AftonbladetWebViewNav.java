package se.redmind.rmtest.aftonbladet.webviewtest;

import java.util.Set;

import org.openqa.selenium.WebElement;

import io.appium.java_client.android.AndroidDriver;

public class AftonbladetWebViewNav {

	AndroidDriver driver;
	
	public AftonbladetWebViewNav(AndroidDriver driver) {
		this.driver = driver;
	}
	
	public void getWebView(){
		WebElement webView = driver.findElementByClassName("android.webkit.WebView");
		Set<String> contextHandles = driver.getContextHandles();
		driver.switchTo().window("WEBVIEW");
		for (String string : contextHandles) {
			System.out.println(string);
		}
	}
	
}
