package se.aftonbladet.abtest.navigation.app;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LeftMenu {
	private WebElement leftMenu;
	private WebDriver driver;
	public LeftMenu(By pLeftMenuBox, WebDriver pDriver) {
		driver = pDriver;
		leftMenu = driver.findElement(pLeftMenuBox);
	}

	public List<WebElement> getMainButtonBoxes(){
		List<WebElement> boxes = new ArrayList<WebElement>();
		List<WebElement> allmenus = leftMenu.findElements(By.className("LinearLayout"));
		WebElement currentLinLay;
		WebElement currentButton;
		
		for (int i = 0; i < allmenus.size(); i++) {
			currentButton = allmenus.get(i);
			try {
				currentLinLay = currentButton.findElement(By.xpath("//LinearLayout[1]"));
				boxes.add(currentButton);
				System.out.println("ButtonText: " + currentLinLay.findElement(By.id("se.aftonbladet.sportbladet.fotboll:id/menuButton")).getText());
			} catch (NoSuchElementException e) {
				System.out.println("ButtonNotAdded: " + currentButton.findElement(By.id("se.aftonbladet.sportbladet.fotboll:id/menuButton")).getText());
				System.out.println("FoundNoLinLay: " + e);
			}
			
			
//			currentLinLays.clear();
//			currentLinLays = currentButton.findElements(By.className("LinearLayout"));
////			System.out.println("NumLinLayouts: " + currentLinLays.size());
//			if (currentLinLays.size() > 0) {
//				System.out.println("ButtonText: " + currentButton.findElement(By.id("se.aftonbladet.sportbladet.fotboll:id/menuButton")).getText());
//				for (int j = 0; j < currentLinLays.size(); j++) {
//					System.out.println(currentLinLays.get(j).getTagName());
//				}
//				boxes.add(currentButton);
//			} else {
//				System.out.println("ShouldNotBePrinted: " + currentButton.findElement(By.id("se.aftonbladet.sportbladet.fotboll:id/menuButton")).getText());
//			}
		}
		return boxes;
	}
	
}
