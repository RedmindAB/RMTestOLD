package se.redmind.rmtest.selenium.grid;

import java.net.URL;

import org.openqa.selenium.remote.DesiredCapabilities;

class UrlCapContainer {

	private URL url;
	private DesiredCapabilities capability;
	String description;
	
	
	public UrlCapContainer(URL url, DesiredCapabilities capability, String description) {
		super();
		this.url = url;
		this.capability = capability;
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public URL getUrl() {
		return url;
	}
	
	public DesiredCapabilities getCapability() {
		return capability;
	}
	
}