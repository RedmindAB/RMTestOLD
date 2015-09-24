package se.redmind.rmtest.aftonbladet.webviewtest;

import io.appium.java_client.android.AndroidDriver;

import java.util.Set;

import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AftonbladetWebViewNav {

    private static final Logger LOG = LoggerFactory.getLogger(AftonbladetWebViewNav.class);
    final AndroidDriver driver;

    public AftonbladetWebViewNav(AndroidDriver driver) {
        this.driver = driver;
    }

    public void getWebView() {
        final WebElement webView = driver.findElementByClassName("android.webkit.WebView");
        Set<String> contextHandles = driver.getContextHandles();
        driver.switchTo().window("WEBVIEW");
        for(String string : contextHandles) {
            LOG.debug(string);
        }
    }
}
