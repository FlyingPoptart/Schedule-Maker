package scheduleMaker;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;

public class SpireScraper {
	private WebDriver driver;
	public WebDriver getDriver() {
		return driver;
	}

	private String username;
	private String password;
	public SpireScraper(String u, String p) {
		username = u;
		password = p;
		
		//ChromeDriver setup and initialization
		String projectLocation = System.getProperty("user.dir");
		System.setProperty("webdriver.chrome.driver", projectLocation + "/lib/chromeDriver/chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
        //options.addArguments("headless");
        options.addArguments("window-size=1200x600");
        options.addArguments("load-extension=" + projectLocation + "/lib/chromeDriver/uBlock Origin/1.16.16_0");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	//returns true if login is successful, false otherwise
	public boolean login() {
		String errorPageURL = "https://www.spire.umass.edu/psp/heproda/?&cmd=login&errorCode=105&languageCd=ENG";
		String loginURL = "https://www.spire.umass.edu/psp/heproda/?cmd=login&languageCd=ENG";
		
		//go to Spire
		driver.get("https://www.spire.umass.edu/");
		
		//Login to spire
		enterText("userid", username, driver);
		enterText("pwd", password, driver);
		driver.findElement(By.name("Submit")).click();
		
		//check if the login succeeded or failed
		boolean isLoggedIn;
		try {
			isLoggedIn = loginAdmitStatus(errorPageURL, loginURL, driver);
			if(isLoggedIn == false) {
				driver.close();
				return false;
			}
			else {
				//makes sure the driver is on the correct frame
				driver.switchTo().frame("TargetContent"); 
				
				//waits for button to load
				@SuppressWarnings("unused")
				WebElement button = driver.findElement(By.id("DERIVED_SSS_SCL_SSS_GO_4$83$"));
				return true;
			}
		} catch (Exception e) {
			driver.close();
			return false;
		}
	}
	
	//true = login success, false = login fail
	public boolean loginAdmitStatus(String errorPageURL, String loginPageURL, WebDriver driver) throws Exception {
		for(int n = 0; n < 20; n++) {
			if(!driver.getCurrentUrl().equals(loginPageURL) && !driver.getCurrentUrl().equals(errorPageURL))
				return true;
			else if(driver.getCurrentUrl().equals(errorPageURL))
				return false;
			else
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
		throw new Exception("Login timeout. Try checking your internet connection.");
	}
	
	public ArrayList<StudentClass> getStudentClasses(String codeName, String courseNumber) throws Exception {
		ArrayList<StudentClass> classes = new ArrayList<StudentClass>();
		String searchURL = "https://www.spire.umass.edu/psc/heproda/EMPLOYEE/HRMS/c/SA_LEARNER_SERVICES.CLASS_SEARCH.GBL?Page=SSR_CLSRCH_ENTRY&Action=U";
		driver.get(searchURL);

		@SuppressWarnings("unused")
		WebElement button = driver.findElement(By.id("CLASS_SRCH_WRK2_SSR_PB_CLASS_SRCH"));
		Select dropdown = new Select(driver.findElement(By.id("CLASS_SRCH_WRK2_SUBJECT$108$")));
		dropdown.selectByValue(codeName);
		enterText("CLASS_SRCH_WRK2_CATALOG_NBR$8$", courseNumber, driver);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		WebElement checkBox = driver.findElement(By.id("CLASS_SRCH_WRK2_SSR_OPEN_ONLY"));
		checkBox.click();
		
		WebElement searchButton = (new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("CLASS_SRCH_WRK2_SSR_PB_CLASS_SRCH")))); 
		WebElement table;
		do {
			searchButton.click();
			table = driver.findElement(By.xpath("//table[@id='ACE_$ICField106$0'] | //table[@id='ACE_DERIVED_CLSMSG_GROUP2']"));
			Thread.sleep(100);
		} while(table == null);

		if(table.getAttribute("id").equals("ACE_DERIVED_CLSMSG_GROUP2")) {
			throw new Exception("Class not found");
		}
		
		int size = (driver.findElements(By.xpath("//table[@id='ACE_$ICField106$0']/tbody/tr")).size() + 1)/9;
		
		@SuppressWarnings("unused")
		int infoTableIndex = -1; //the first row of value starts at index 8, so the 0th row should be -1
		@SuppressWarnings("unused")
		int nameIndex = -5; 
		for(int currentRow = 0; currentRow < size; currentRow++) {
			infoTableIndex += 9;
			nameIndex += 9;
			String section = driver.findElement(By.id("DERIVED_CLSRCH_SSR_CLASSNAME_LONG$" + currentRow)).getText();
			String className = codeName + " " + courseNumber + "-" + translateName(section);
			String courseName = codeName + " " + courseNumber;
			
			boolean isLecture = false;
			if(section.contains("LEC"))
				isLecture = true;
			
			String time = driver.findElement(By.id("MTG_DAYTIME$" + currentRow)).getText();
			if(time.equals("TBA")) {
				continue;
			}
			ArrayList<TimeSlot> timeSlots = translateTimeSlots(time);
			
			String location = driver.findElement(By.id("MTG_ROOM$" + currentRow)).getText();
			String mapsLocation = translateLocation(location);
			
			String professor = driver.findElement(By.id("MTG_INSTR$" + currentRow)).getText();
			professor = translateProfessor(professor);
			classes.add(generateStudentClass(isLecture, mapsLocation, courseName, location, className, timeSlots, professor));
		}
		ArrayList<StudentClass> classesFinal = new ArrayList<StudentClass>();
		for(int n = 0; n < classes.size(); n++) {
			if(classes.get(n).getClass().getName().equals("scheduleMaker.Addon"))
				classesFinal.add(new Addon(classes.get(n).getMapsLocation(), classes.get(n).getCourse(), classes.get(n).getLocation(), classes.get(n).getName(), classes.get(n).getTimeSlots(), classes.get(n).getProfessor().getName(), driver));
			else
				classesFinal.add(new Lecture(classes.get(n).getMapsLocation(), classes.get(n).getCourse(), classes.get(n).getLocation(), classes.get(n).getName(), classes.get(n).getTimeSlots(), classes.get(n).getProfessor().getName(), driver));
		}
		return classesFinal;
	}
	public StudentClass generateStudentClass(boolean isLecture, String mapsLocation, String course, String location, String name, ArrayList<TimeSlot> timeSlots,
			String professor) {
		StudentClass sClass;
		if(isLecture) {
			sClass = new Lecture(mapsLocation, course, location, name, timeSlots, professor);
		}
		else {
			sClass = new Addon(mapsLocation, course, location, name, timeSlots, professor);
		}
		return sClass;
	}
	
	public static void enterText(String id, String text, WebDriver driver) {
		@SuppressWarnings("unused")
		WebElement wb = driver.findElement(By.id(id));
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("document.getElementById('" + id + "').value='" + text + "';");
	}

	private static ArrayList<TimeSlot> translateTimeSlots(String time) {
		if(time.equals("TBA"))
			return null;
		ArrayList<Integer> days = new ArrayList<Integer>();
		while(!time.substring(0, 1).equals(" ")) {
			days.add(dayToNumber(time.substring(0, 2)));
			time = time.substring(2);
		}
		time = time.substring(1);
		String sHour = time.substring(0, time.indexOf(":"));
		String sMinute = time.substring(time.indexOf(":") + 1, time.indexOf(" ") - 2);
		String eHour = time.substring(time.lastIndexOf(" ") + 1, time.lastIndexOf(":"));
		String eMinute = time.substring(time.lastIndexOf(":") + 1, time.length() - 2);
		int startHour = Integer.valueOf(sHour);
		int startMinute = Integer.valueOf(sMinute);
		int endHour = Integer.valueOf(eHour);
		int endMinute = Integer.valueOf(eMinute);
		if(time.substring(0, time.indexOf("-")).contains("PM")&& startHour != 12)
			startHour += 12;
		if(time.substring(time.indexOf("-")).contains("PM") && endHour != 12)
			endHour += 12;
		ArrayList<TimeSlot> timeSlots = new ArrayList<TimeSlot>();
		for(Integer k : days) {
			TimeSlot ts = new TimeSlot(new Time(startHour, startMinute), new Time(endHour, endMinute), k);
			timeSlots.add(ts);
		}
		return timeSlots;
	}
	private static String translateLocation(String location) {
		String mapsLocation = location;
		if(mapsLocation.contains("rm"))
			mapsLocation = mapsLocation.substring(0, mapsLocation.indexOf("rm"));
		if(mapsLocation.contains("room"))
			mapsLocation = mapsLocation.substring(0, mapsLocation.indexOf("room"));
		return mapsLocation;
	}
	private static String translateProfessor(String professor) {
		if(professor.equals("Staff")) {
			professor = null;
		}
		return professor;
	}
	public static String translateName(String name) {
		String newName = name.substring(0, name.indexOf("-")) + " " + name.substring(name.indexOf("("));
		return newName;
	}
	
	private static int dayToNumber(String s) {
		if(s.equals("Su")) 
			return 0;
		else if(s.equals("Mo")) 
			return 1;
		else if(s.equals("Tu")) 
			return 2;
		else if(s.equals("We")) 
			return 3;
		else if(s.equals("Th")) 
			return 4;
		else if(s.equals("Fr")) 
			return 5;
		else if(s.equals("Sa")) 
			return 6;
		else throw new NullPointerException("You screwed up the Days of the Week");
	}
}
