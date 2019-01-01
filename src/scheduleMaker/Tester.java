package scheduleMaker;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Tester {
	static WebDriver driver;
	public static void main(String args[]) throws InterruptedException {
		String projectLocation = System.getProperty("user.dir");
		ChromeOptions options = new ChromeOptions();
	    options.addArguments("load-extension=" + projectLocation + "/lib/chromeDriver/uBlock Origin/1.16.16_0");
		System.setProperty("webdriver.chrome.driver", projectLocation + "/lib/chromeDriver/chromedriver.exe");
		driver = new ChromeDriver(options);
	
		
		
		driver.get("http://www.ratemyprofessors.com/search.jsp?queryoption=HEADER&queryBy=teacherName&schoolName=University+of+Massachusetts&schoolID=1362&query=Mario+DeFranco");
		
		WebElement element = driver.findElement(By.xpath("//div[@class='result-count']"));
		System.out.println(element.getAttribute("class"));
		System.out.println(element.getText());
		List<WebElement> elements = driver.findElements(By.xpath("//div[@class='result-count'] | //span[@class='listing-cat']"));	
		System.out.println(elements.size());
		System.out.println(elements.get(0).getAttribute("class"));
		System.out.println(elements.get(1).getAttribute("class"));
		System.out.println(elements.get(2).getAttribute("class"));

		System.out.println(elements.get(0).getText());
		
	}
	
}
