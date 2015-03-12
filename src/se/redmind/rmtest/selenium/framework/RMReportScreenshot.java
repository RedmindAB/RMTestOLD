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

public class RMReportScreenshot {

	private static final int MAX_LONG_SIDE = 1280;
	private static final int MAX_SHORT_SIDE = 720;
	
	private DriverNamingWrapper namingWrapper;
	private WebDriver driver;

	public RMReportScreenshot(DriverNamingWrapper namingWrapper) {
		this.namingWrapper = namingWrapper;
		this.driver = namingWrapper.getDriver();
	}
	
	public void takeScreenshot(){
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		BufferedImage image = fileToImage(scrFile);
		if (isResizeNecessary(image)) {
			image = resizeImage(image);
		}
		SaveImage(image);
	}

	private void SaveImage(BufferedImage image) {
		try {
			ImageIO.write(image, ".png", new File(""));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			factor = MAX_LONG_SIDE / height;
			height = MAX_LONG_SIDE;
			width = (int) (width * factor);
		}
		else {
			factor = MAX_LONG_SIDE / width;
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
	
}
