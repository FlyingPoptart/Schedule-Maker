package scheduleMaker;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Professor {
	public static void main(String args[]) {
		
	}
	
	public Professor(String name, double rating, int numRatings) {
		this.name = name;
		this.rating = rating;
		this.numRatings = numRatings;
	}

	public Professor(String name) {
		super();
		this.name = name;
	}

	private String name;
	private double rating;
	private int numRatings;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}
	public int getNumRatings() {
		return numRatings;
	}
	public void setNumRatings(int numRatings) {
		this.numRatings = numRatings;
	}
	public String toString() {
		return name + " " + rating + " " + numRatings;
	}

	
	public Professor(String name, WebDriver driver) {
		try {
		String firstName = name.substring(0, name.indexOf(" "));
		String lastName = name.substring(name.indexOf(" ") + 1);
		String URL = "http://www.ratemyprofessors.com/search.jsp?queryoption=HEADER&queryBy=teacherName&schoolName=University+of+Massachusetts&schoolID=1362&query=" + firstName + "+" + lastName;
		driver.get(URL);
		List<WebElement> elements = driver.findElements(By.xpath("//span[@class='listing-cat'] | //div[@class='result-count'] | //div[@class='header error']"));	
		ArrayList<WebElement> buttons = new ArrayList<WebElement>();
		for(int n = 0; n < elements.size(); n++) {
			if(elements.get(n).getAttribute("class").equals("listing-cat")) {
				buttons.add(elements.get(n));
			}
		}
		if(buttons.size() == 1) {
			buttons.get(0).click();
			this.name = name;
			rating = Double.parseDouble(driver.findElement(By.xpath("//div[@class='grade']")).getText());
			String ratingCount = driver.findElement(By.xpath("//div[@class='table-toggle rating-count active']")).getText();
			numRatings = Integer.parseInt(ratingCount.substring(0, ratingCount.indexOf(" ")));	
		}
		else {
			this.name = null;
		}
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}
		
		
	}
}
