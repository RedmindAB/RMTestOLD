package se.redmind.rmtest.selenium.framework;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import se.redmind.rmtest.selenium.grid.DriverNamingWrapper;
import se.redmind.rmtest.selenium.grid.TestHome;

public class RMReportScreenshot {

	private static final int MAX_LONG_SIDE = 1280;
	private static final String FILE_EXTENTION = "png";
	private DriverNamingWrapper namingWrapper;
	private WebDriver driver;

	public RMReportScreenshot(DriverNamingWrapper namingWrapper) {
		this.namingWrapper = namingWrapper;
		this.driver = namingWrapper.getDriver();
	}
	
	/**
	 *
	 * this method should be called directly from a test-method, the filename will have the name of the invoked class and method inside it.
	 * if more than one screenshot is taken in the same method make sure that the screenshot is unique for each screenshot.
	 * @param prefix - optional, description of the screenshot can be null or empty.
	 */
	public void takeScreenshot(String prefix){
		String className = StackTraceInfo.getInvokingClassName();
		String methodName = StackTraceInfo.getInvokingMethodName();
		takeScreenshot(className, methodName, prefix);
	}
	
	/** 
	 * USE WITH CAUTION!
	 * 
	 * This method should be an alternative to the takeScreenshot method if needed, examples of use is Navigation classes.
	 * its important that the class and method name is the same as they are stored in RMReport. 
	 * @param className - name of the testclass.
	 * @param methodName - name of the test method that was invoked.
	 */
	public void takeScreenshot(String className, String methodName, String prefix){
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		BufferedImage image = fileToImage(scrFile);
		if (isResizeNecessary(image)) {
			image = resizeImage(image);
		}
		String filename = getFileName(className, methodName, prefix);
		if (filename == null) {
			System.err.println("No screenshot taken, run with 'mvn test'");
			return;
		}
		SaveImage(image, filename);
	}

	private void SaveImage(BufferedImage image, String filename) {
		try {
			System.out.println(filename);
			ImageIO.write(image, FILE_EXTENTION, new File(filename));
		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private String getFileName(String className, String methodName, String prefix) {
		String timestamp = System.getProperty("rmt.timestamp");
		if (timestamp == null) {
			return null;
		}
		timestamp = timestamp.replace("-", "");
		String description = namingWrapper.getDescription();
		String filename = className+"."+methodName+"-"+timestamp+"["+description+"]."+FILE_EXTENTION;
		if (prefix != null && prefix.length() > 0) {
			filename = prefix + "-_-" + filename;
		}
		filename = getSavePath(timestamp)+filename;
		return filename;
	}

	private BufferedImage resizeImage(BufferedImage originalImage) {
		BufferedImage resizedImage = null;
		try {
			int type = getType(originalImage);
			resizedImage = resizeImageWithHint(originalImage, type);
		} catch (Exception e) {
			System.err.println("Could not resize screenshot image!");
			e.printStackTrace();
		}
		return resizedImage;
	}

	private BufferedImage fileToImage(File scrFile) {
		try {
			return ImageIO.read(scrFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private BufferedImage resizeImageWithHint(BufferedImage originalImage, int type){
		
		BufferedImage resizedImage = getResizedBufferedImage(originalImage, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, resizedImage.getWidth(), resizedImage.getHeight(), null);
		g.dispose();	
		g.setComposite(AlphaComposite.Src);
	 
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	 
		return resizedImage;
	}
	
	private BufferedImage getResizedBufferedImage(BufferedImage originalImage, int type) {
		int height = originalImage.getHeight();
		int width = originalImage.getWidth();
		float factor = 0; 
		if (height>width) {
			factor = (float) MAX_LONG_SIDE / height;
			height = MAX_LONG_SIDE;
			width = (int) (width * factor);
		}
		else {
			factor = (float) MAX_LONG_SIDE / width;
			width = MAX_LONG_SIDE;
			height = (int) (height * factor);
		}
		return new BufferedImage(width, height, type);
	}

	private boolean isResizeNecessary(BufferedImage originalImage){
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();
		if (width>height) {
			return width>MAX_LONG_SIDE;
		}
		else return height>MAX_LONG_SIDE;
	}
	
	private int getType(BufferedImage originalImage){
		return originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

	}
	
	private String getSavePath(String timestamp){
		String path = TestHome.main() + "/RMR-Screenshots/"+timestamp+"/";
		File file = new File(path);
		file.mkdirs();
		return path;
	}
	
}
