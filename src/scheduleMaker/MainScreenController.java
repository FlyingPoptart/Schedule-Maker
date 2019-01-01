package scheduleMaker;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.controlsfx.control.ToggleSwitch;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MainScreenController implements Initializable {
	private SpireScraper spireScraper;
	private int teacherRow;
	private int classRow;
	private ArrayList<ProfessorItem> professorItems;
	private ArrayList<StudentClass> avoidClasses;
	private ArrayList<StudentClassItem> studentClassItems;
	
	
	@FXML private TextField numberBox;
	@FXML private ComboBox<CourseName> courseMenu;
	@FXML private Button addButton;
	@FXML private ListView<Course> listView;
	@FXML private Button removeButton;
	@FXML private Button generateButton;
	@FXML private GridPane teacherGrid;
	@FXML private GridPane classGrid;
	@FXML private ScrollPane teacherPane;
	@FXML private ScrollPane classPane;

	public SpireScraper getSpireScraper() {
		return spireScraper;
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		populateComboBox();
		teacherRow = 0;
		classRow = 0;
		professorItems = new ArrayList<ProfessorItem>();
		avoidClasses = new ArrayList<StudentClass>();
		studentClassItems = new ArrayList<StudentClassItem>();
	}
	
    
	public void initializeProfessorCookies() {
		WebDriver driver = spireScraper.getDriver();
		driver.get("http://www.ratemyprofessors.com/");
		driver.findElement(By.xpath("//a[@class='btn close-this']")).click();
	}
	
	public void addCourse() throws Exception {
		String name = courseMenu.getValue().getCodeName();
		String number = numberBox.getText();
		Course course = new Course(spireScraper.getStudentClasses(name, number));
		listView.getItems().add(course);
		addProfessors(course);
		addStudentClasses(course);
	}
	
	public void addStudentClasses(Course course) {
		ArrayList<StudentClass> classes = new ArrayList<StudentClass>();
		for(StudentClass c: course.getAddons()) {
			classes.add(c);
		}
		for(StudentClass c: course.getLectures()) {
			classes.add(c);
		}
		for(int n = 0; n < classes.size(); n++) {
			addStudentClass(classes.get(n), course);
		}
	}
	
	public void addStudentClass(StudentClass sc, Course course) {
		String name = sc.getName();
		String time = sc.getTimeSlots().get(0).toString();
		String location = sc.getLocation();
		ToggleSwitch toggleSwitch = new ToggleSwitch();
		toggleSwitch.fire();
		Label nameLabel = new Label(name);
		Label timeLabel = new Label(time);
		Label locationLabel = new Label(location);
		Insets padding = new Insets(4, 4, 0, 4);
		nameLabel.setPadding(padding);
		timeLabel.setPadding(padding);
		locationLabel.setPadding(padding);
		toggleSwitch.setPadding(new Insets(4, 0, 0, 0));
		classGrid.add(nameLabel, 0, classRow);
		classGrid.add(timeLabel, 1, classRow);
		classGrid.add(locationLabel, 2, classRow);
		classGrid.add(toggleSwitch, 3, classRow);
		studentClassItems.add(new StudentClassItem(course, sc, toggleSwitch));
		classRow++;
	}
	
	public boolean professorPresent(Professor professor, ArrayList<Professor> gridProfessors) {
		for(int n = 0; n < gridProfessors.size(); n++) {
			System.out.println(professor);
			System.out.println(gridProfessors);
			if(professor.getName().equals(gridProfessors.get(n).getName()))
				return true;
		}
		return false;
	}
	
	public void addTeacherCourseLabel(Course c) {
		Label courseLabel = new Label(c.getName() +":");
		courseLabel.setPadding(new Insets(4, 4, 0, 4));
		teacherGrid.add(courseLabel, 0, teacherRow);
		professorItems.add(new ProfessorItem(c));
		teacherRow++;
	}
	
	public void addProfessors(Course c) {
		addTeacherCourseLabel(c);
		ArrayList<Professor> gridProfessors = new ArrayList<Professor>();
		for(int n = 0; n < c.getAddons().size(); n++) {
			Professor professor = c.getAddons().get(n).getProfessor();
			if(!professorPresent(professor, gridProfessors) && !(professor == null || professor.getName() == null || professor.getRating() == 0 || professor.getNumRatings() == 0))
				gridProfessors = addProfessor(professor, c, gridProfessors, c.getAddons().get(n));
		}
		for(int n = 0; n < c.getLectures().size(); n++) {
			Professor professor = c.getLectures().get(n).getProfessor();
			if(!(professor == null || professor.getName() == null || professor.getRating() == 0 || professor.getNumRatings() == 0) && !professorPresent(professor, gridProfessors))
				gridProfessors = addProfessor(professor, c, gridProfessors, c.getLectures().get(n));
		}
	}
	
	class ProfessorItem {
		public Course getCourse() {
			return course;
		}
		public void setCourse(Course course) {
			this.course = course;
		}
		public StudentClass getStudentClass() {
			return studentClass;
		}
		public void setStudentClass(StudentClass studentClass) {
			this.studentClass = studentClass;
		}
		public ProfessorItem(Course course, StudentClass studentClass, ToggleSwitch toggleSwitch) {
			super();
			this.course = course;
			this.studentClass = studentClass;
			this.toggleSwitch = toggleSwitch;
		}
		public ProfessorItem(Course course) {
			super();
			this.course = course;
		}
		private Course course;
		private StudentClass studentClass;
		public ToggleSwitch getToggleSwitch() {
			return toggleSwitch;
		}
		private ToggleSwitch toggleSwitch;
	}
	
	class StudentClassItem {
		public Course getCourse() {
			return course;
		}
		public void setCourse(Course course) {
			this.course = course;
		}
		public StudentClass getStudentClass() {
			return studentClass;
		}
		public void setStudentClass(StudentClass studentClass) {
			this.studentClass = studentClass;
		}
		public StudentClassItem(Course course, StudentClass studentClass, ToggleSwitch toggleSwitch) {
			super();
			this.course = course;
			this.studentClass = studentClass;
			this.toggleSwitch = toggleSwitch;
		}
		public StudentClassItem(Course course) {
			super();
			this.course = course;
		}
		private Course course;
		private StudentClass studentClass;
		public ToggleSwitch getToggleSwitch() {
			return toggleSwitch;
		}
		private ToggleSwitch toggleSwitch;
	}
	
	public ArrayList<Professor> addProfessor(Professor p, Course course, ArrayList<Professor> gridProfessors, StudentClass sc) {
		gridProfessors.add(p);
		String name = p.getName();
		String rating = "Rating: " + p.getRating() + " / 5.0";
		String numRatings = p.getNumRatings() + " Ratings";
		ToggleSwitch toggleSwitch = new ToggleSwitch();
		toggleSwitch.fire();
		Label nameLabel = new Label(name);
		Label ratingLabel = new Label(rating);
		Label numRatingsLabel = new Label(numRatings);
		Insets padding = new Insets(4, 4, 0, 4);
		nameLabel.setPadding(padding);
		ratingLabel.setPadding(padding);
		numRatingsLabel.setPadding(padding);
		toggleSwitch.setPadding(new Insets(4, 0, 0, 0));
		teacherGrid.add(nameLabel, 0, teacherRow);
		teacherGrid.add(ratingLabel, 1, teacherRow);
		teacherGrid.add(numRatingsLabel, 2, teacherRow);
		teacherGrid.add(toggleSwitch, 3, teacherRow);
		professorItems.add(new ProfessorItem(course, sc, toggleSwitch));
		teacherRow++;
		return gridProfessors;
	}
	
	static void deleteRow(GridPane grid, int row) {
	    Set<Node> deleteNodes = new HashSet<>();
	    for (Node child : grid.getChildren()) {
	        Integer rowIndex = GridPane.getRowIndex(child);
	        int r = rowIndex == null ? 0 : rowIndex;
	        if (r > row) {
	            GridPane.setRowIndex(child, r-1);
	        } else if (r == row) {
	            deleteNodes.add(child);
	        }
	    }
	    grid.getChildren().removeAll(deleteNodes);
	}
	
	void removeTeachers(Course c) {
		for(int n = 0; n < professorItems.size(); n++) {
			if(professorItems.get(n).getCourse().equals(c)) {
				deleteRow(teacherGrid, n);
				professorItems.remove(n);
				n--;
			}
		}
	}
	
	void removeStudentClasses(Course c) {
		for(int n = 0; n < studentClassItems.size(); n++) {
			if(studentClassItems.get(n).getCourse().equals(c)) {
				deleteRow(classGrid, n);
				studentClassItems.remove(n);
				n--;
			}
		}
	}
	
	public boolean validateCodeName(String s) {
		if(s.equals("") || s.equals(null))
			return false;
		return true;
	}

	public void removeCourse() {
        int selectedIdx = listView.getSelectionModel().getSelectedIndex();
        if (selectedIdx != -1) {
 
        	final int newSelectedIdx =
        	(selectedIdx == listView.getItems().size() - 1)
        		? selectedIdx - 1
        		: selectedIdx;
        	
        	Course course = listView.getItems().get(selectedIdx);
        	removeTeachers(course);
        	removeStudentClasses(course);
        	
        	
          	listView.getItems().remove(selectedIdx);
          	listView.getSelectionModel().select(newSelectedIdx);
        }
	}
	public void generateSchedules() throws IOException {
		ObservableList<Course> courses = listView.getItems();
		ArrayList<Course> c = new ArrayList<Course>();
		for(int n = 0; n < courses.size(); n++) {
			c.add(courses.get(n));
		}
		populateAvoidClasses();
		ScheduleItems items = ScheduleMaker.createSchedules(c, avoidClasses);
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ScheduleList.fxml"));
		Parent root = (Parent) loader.load();
			
		ScheduleListController scheduleListController = loader.getController(); 
		scheduleListController.setScheduleItems(items);
		scheduleListController.populateConflicts();
		scheduleListController.populateScheduleItems();

		Stage scheduleList = new Stage();
		scheduleList.setScene(new Scene(root));
		scheduleList.setTitle("Results");
		scheduleList.show();
	}
	
	public void populateAvoidClasses() {
		avoidClasses = new ArrayList<StudentClass>();
		for(int n = 0; n < professorItems.size(); n++) {
			if(professorItems.get(n).getToggleSwitch() != null && professorItems.get(n).getToggleSwitch().selectedProperty().get() == false) {
				avoidClasses.add(professorItems.get(n).getStudentClass());
			}
		}
		for(int n = 0; n < studentClassItems.size(); n++) {
			if(studentClassItems.get(n).getToggleSwitch() != null && studentClassItems.get(n).getToggleSwitch().selectedProperty().get() == false) {
				avoidClasses.add(studentClassItems.get(n).getStudentClass());
			}
		}
	}
	
    public void populateComboBox() {
    	List<CourseName> courseList = new ArrayList<CourseName>();
		courseList.add(new CourseName("ACCOUNTG", "Accounting"));
		courseList.add(new CourseName("AEROSPAC", "Aerospace Studies"));
		courseList.add(new CourseName("AFROAM", "Afro-American Studies"));
		courseList.add(new CourseName("ANIMLSCI", "Animal Science"));
		courseList.add(new CourseName("ANTHRO", "Anthropology"));
		courseList.add(new CourseName("ARABIC", "Arabic"));
		courseList.add(new CourseName("ARCH", "Architecture"));
		courseList.add(new CourseName("ARCH-DES", "Architecture and Design"));
		courseList.add(new CourseName("ART", "Art"));
		courseList.add(new CourseName("ART-ED", "Art - Student Teaching"));
		courseList.add(new CourseName("ART-HIST", "Art History"));
		courseList.add(new CourseName("ARTS-EXT", "Arts Extension"));
		courseList.add(new CourseName("ASIAN-ST", "Asian Studies"));
		courseList.add(new CourseName("ASTRON", "Astronomy"));
		courseList.add(new CourseName("BDIC", "Bachelor's Deg. W/Indiv Conc."));
		courseList.add(new CourseName("BIOCHEM", "Biochemistry & Molecular Bio."));
		courseList.add(new CourseName("BIOLOGY", "Biology"));
		courseList.add(new CourseName("BI@MIC", "Biology @ MIC"));
		courseList.add(new CourseName("BMED-ENG", "Biomedical Engineering"));
		courseList.add(new CourseName("BIOSTATS", "Biostatistics"));
		courseList.add(new CourseName("BIOST&EP", "Biostats & Epidemiology"));
		courseList.add(new CourseName("BIOTECH", "Biotechnology"));
		courseList.add(new CourseName("BCT", "Building & Construction Tech"));
		courseList.add(new CourseName("CATALAN", "Catalan"));
		courseList.add(new CourseName("CHEM-ENG", "Chemical Engineering"));
		courseList.add(new CourseName("CHEM", "Chemistry"));
		courseList.add(new CourseName("CHINESE", "Chinese"));
		courseList.add(new CourseName("CE-ENGIN", "Civil & Environmental Engrg"));
		courseList.add(new CourseName("CLASSICS", "Classics"));
		courseList.add(new CourseName("CC@MIC", "College Curriculum @ MCI"));
		courseList.add(new CourseName("CICS", "College of Inform & Comp Sci"));
		courseList.add(new CourseName("COMM", "Communication"));
		courseList.add(new CourseName("COMM-DIS", "Communication Disorders"));
		courseList.add(new CourseName("COM-HLTH", "Community Health (see PUBHLTH));"));
		courseList.add(new CourseName("COMP-LIT", "Comparative Literature"));
		courseList.add(new CourseName("COMPSCI", "Computer Science"));
		courseList.add(new CourseName("DANCE", "Dance"));
		courseList.add(new CourseName("DANISH", "Danish"));
		courseList.add(new CourseName("DUTCH", "Dutch"));
		courseList.add(new CourseName("ECON", "Economics"));
		courseList.add(new CourseName("EDUC", "Education"));
		courseList.add(new CourseName("E&C-ENG", "Electrical & Computer Engin"));
		courseList.add(new CourseName("ENGIN", "Engineering"));
		courseList.add(new CourseName("ENGLISH", "English"));
		courseList.add(new CourseName("ENGLWRIT", "English Writing Program"));
		courseList.add(new CourseName("ESL", "English as a Second Language"));
		courseList.add(new CourseName("ENTOMOL", "Entomology"));
		courseList.add(new CourseName("ECO", "Environmental Conservation"));
		courseList.add(new CourseName("ENVIRDES", "Environmental Design"));
		courseList.add(new CourseName("EHS", "Environmental Health Sciences"));
		courseList.add(new CourseName("ENVIRSCI", "Environmental Science"));
		courseList.add(new CourseName("EPI", "Epidemiology"));
		courseList.add(new CourseName("EDIPEMIO", "Epidemiology"));
		courseList.add(new CourseName("EXCHANGE", "Exchange"));
		courseList.add(new CourseName("FFYS", "Faculty First Year Seminars"));
		courseList.add(new CourseName("FILM-ST", "Film Studies"));
		courseList.add(new CourseName("FINANCE", "Finance"));
		courseList.add(new CourseName("FINOPMGT", "Finance and Operations Mgt"));
		courseList.add(new CourseName("FINNISH", "Finnish"));
		courseList.add(new CourseName("FORLANGC", "Five Coll Ctr: World Languages"));
		courseList.add(new CourseName("FOOD-SCI", "Food Science"));
		courseList.add(new CourseName("FRENCHST", "French Studies"));
		courseList.add(new CourseName("FRENCHED", "French-Student Teaching"));
		courseList.add(new CourseName("GEOGRAPH", "Geography"));
		courseList.add(new CourseName("GEOLOGY", "Geology"));
		courseList.add(new CourseName("GEO-SCI", "Geosciences"));
		courseList.add(new CourseName("GERMAN", "German"));
		courseList.add(new CourseName("GRADSCH", "Graduate School"));
		courseList.add(new CourseName("GREEK", "Greek"));
		courseList.add(new CourseName("HAITCREO", "Haitian Creole"));
		courseList.add(new CourseName("HPP", "Health Promotion & Policy"));
		courseList.add(new CourseName("HEBREW", "Hebrew"));
		courseList.add(new CourseName("HERIT", "Heritage Studies"));
		courseList.add(new CourseName("HISPAN", "Hispanic Lit. & Linguistics"));
		courseList.add(new CourseName("HISTORY", "History"));
		courseList.add(new CourseName("HI@MIC", "History @ MIC"));
		courseList.add(new CourseName("HONORS", "Honors College"));
		courseList.add(new CourseName("HT-MGT", "Hospitality & Tourism Managmnt"));
		courseList.add(new CourseName("HUMANDEV", "Human Development"));
		courseList.add(new CourseName("HU@MIC", "Humanities @ MIC"));
		courseList.add(new CourseName("HM&FNART", "Humanities and Fine Arts"));
		courseList.add(new CourseName("INFO", "Informatics"));
		courseList.add(new CourseName("INFOSEC", "Information Security"));
		courseList.add(new CourseName("INTERPRT", "Interpreter&#039s Studies"));
		courseList.add(new CourseName("SCH-MGMT", "Isenberg School of Management"));
		courseList.add(new CourseName("ITALIAN", "Italian Studies"));
		courseList.add(new CourseName("ITALIAED", "Italian-Student Teaching"));
		courseList.add(new CourseName("JAPANESE", "Japanese"));
		courseList.add(new CourseName("JOURNAL", "Journalism"));
		courseList.add(new CourseName("JUDAIC", "Judaic Studies"));
		courseList.add(new CourseName("KIN", "Kinesiology"));
		courseList.add(new CourseName("KOREAN", "Korean"));
		courseList.add(new CourseName("LLACTING", "LL: Acting"));
		courseList.add(new CourseName("LLART", "LL: Art & Photography"));
		courseList.add(new CourseName("LLAMS", "LL: Arts Management"));
		courseList.add(new CourseName("LLBUS", "LL: Business & Financial Plan"));
		courseList.add(new CourseName("LLCAR", "LL: Career & Personal Develpmt"));
		courseList.add(new CourseName("LLCOM", "LL: Computers"));
		courseList.add(new CourseName("LLLEAD", "LL: Donahue Leadership Prog"));
		courseList.add(new CourseName("LLESL", "LL: English as a 2nd Language"));
		courseList.add(new CourseName("LLFOOD", "LL: Food and Drink"));
		courseList.add(new CourseName("LLINGARM", "LL: Global Asset & Risk Mgmt"));
		courseList.add(new CourseName("LLHEA", "LL: Health & Fitness"));
		courseList.add(new CourseName("LLLAN", "LL: Languages"));
		courseList.add(new CourseName("LLLCR", "LL: License Renewal"));
		courseList.add(new CourseName("LLMUS", "LL: Music"));
		courseList.add(new CourseName("LLPARKS", "LL: Parks & Conservation Law"));
		courseList.add(new CourseName("LLPER", "LL: Personal Awareness"));
		courseList.add(new CourseName("LLREA", "LL: Real Estate"));
		courseList.add(new CourseName("LLPLSOIL", "LL: Soils/Plants/Insects"));
		courseList.add(new CourseName("LLSPEC", "LL: Special Interest"));
		courseList.add(new CourseName("LLSR&O", "LL: Sports,Recrtn & Outdoors"));
		courseList.add(new CourseName("LLSTU", "LL: Study Skills & Test Taking"));
		courseList.add(new CourseName("LLGREEN", "LL: Sustainability/Green"));
		courseList.add(new CourseName("LLWIND", "LL: Wind Energy"));
		courseList.add(new CourseName("LLWOOD", "LL: Wood Identification"));
		courseList.add(new CourseName("LLWRI", "LL: Writing, Literature &Drama"));
		courseList.add(new CourseName("LABOR", "Labor Studies"));
		courseList.add(new CourseName("LANDARCH", "Landscape Architecture"));
		courseList.add(new CourseName("LANDCONT", "Landscape Contracting"));
		courseList.add(new CourseName("LLC", "Languages, Literature&Culture"));
		courseList.add(new CourseName("LATIN", "Latin"));
		courseList.add(new CourseName("LATIN-AM", "Latin American Studies"));
		courseList.add(new CourseName("LATIN-ED", "Latin-Student Teaching"));
		courseList.add(new CourseName("LEGAL", "Legal Studies"));
		courseList.add(new CourseName("LINGUIST", "Linguistics"));
		courseList.add(new CourseName("LI@MIC", "Literature Elective @ MIC"));
		courseList.add(new CourseName("MANAGMNT", "Management"));
		courseList.add(new CourseName("MARKETNG", "Marketing"));
		courseList.add(new CourseName("MATH", "Mathematics"));
		courseList.add(new CourseName("MA@MIC", "Mathematics @ MIC"));
		courseList.add(new CourseName("M&I-ENG", "Mechanical & Industrial Engrg"));
		courseList.add(new CourseName("MICROBIO", "Microbiology"));
		courseList.add(new CourseName("MIDEAST", "Middle Eastern Studies"));
		courseList.add(new CourseName("MILITARY", "Military Leadership"));
		courseList.add(new CourseName("EURO", "Modern European Studies"));
		courseList.add(new CourseName("MOLCLBIO", "Molecular & Cellular Biology"));
		courseList.add(new CourseName("MUSIC", "Music"));
		courseList.add(new CourseName("MUSIC-ED", "Music Education"));
		courseList.add(new CourseName("MUSICAPP", "Music, Applied"));
		courseList.add(new CourseName("NEXCHNG", "National Student Exchange"));
		courseList.add(new CourseName("NRC", "Natural Resources Conservation"));
		courseList.add(new CourseName("NATSCI", "Natural Sciences"));
		courseList.add(new CourseName("NEUROS&B", "Neuroscience & Behavior"));
		courseList.add(new CourseName("NURSING", "Nursing"));
		courseList.add(new CourseName("NUTRITN", "Nutrition"));
		courseList.add(new CourseName("OIM", "Operations & Info Management"));
		courseList.add(new CourseName("ORG&EVBI", "Organismic & Evolutionary Biol"));
		courseList.add(new CourseName("PHIL", "Philosophy"));
		courseList.add(new CourseName("PHYSICS", "Physics"));
		courseList.add(new CourseName("PLANTBIO", "Plant Biology"));
		courseList.add(new CourseName("POLISH", "Polish"));
		courseList.add(new CourseName("POLISCI", "Political Science"));
		courseList.add(new CourseName("POLYMER", "Polymer Science & Engineering"));
		courseList.add(new CourseName("PORTUG", "Portuguese"));
		courseList.add(new CourseName("PORTUGED", "Portuguese-Student Teaching"));
		courseList.add(new CourseName("PSYCH", "Psychology & Brain Sciences"));
		courseList.add(new CourseName("PUBHLTH", "Public Health"));
		courseList.add(new CourseName("PUBP&ADM", "Public Policy & Administration"));
		courseList.add(new CourseName("REGIONPL", "Regional Planning"));
		courseList.add(new CourseName("RES-ECON", "Resource Economics"));
		courseList.add(new CourseName("ROMANIA", "Romanian"));
		courseList.add(new CourseName("RUSSIAN", "Russian"));
		courseList.add(new CourseName("SCANDIN", "Scandinavian"));
		courseList.add(new CourseName("SCHPSYCH", "School Psychology"));
		courseList.add(new CourseName("SPP", "School of Public Policy"));
		courseList.add(new CourseName("SC@MIC", "Science @ MIC"));
		courseList.add(new CourseName("SRVCLRNG", "Service Learning"));
		courseList.add(new CourseName("SLAVIC", "Slavic"));
		courseList.add(new CourseName("SEESTU", "Slavic & E European Studies"));
		courseList.add(new CourseName("SOCBEHAV", "Social & Behavioral Science"));
		courseList.add(new CourseName("STPEC", "Social Thought & Polic. Econ"));
		courseList.add(new CourseName("SOCIOL", "Sociology"));
		courseList.add(new CourseName("SPANISH", "Spanish"));
		courseList.add(new CourseName("SPANI-ED", "Spanish - Student Teaching"));
		courseList.add(new CourseName("SPORTMGT", "Sport Management"));
		courseList.add(new CourseName("STATISTC", "Statistics"));
		courseList.add(new CourseName("STOCKSCH", "Stockbridge Sch of Agriculture"));
		courseList.add(new CourseName("SUSTCOMM", "Sustainable Community"));
		courseList.add(new CourseName("SWEDISH", "Swedish"));
		courseList.add(new CourseName("THEATER", "Theater"));
		courseList.add(new CourseName("UMA-XCHG", "UMass Graduate Course Exchange"));
		courseList.add(new CourseName("UMASS", "UMass Practicum"));
		courseList.add(new CourseName("UNIVRSTY", "Univ Interdepartmental Course"));
		courseList.add(new CourseName("UWW", "University Without Walls"));
		courseList.add(new CourseName("VT@MIC", "Vet Tech @ Mt. Ida College"));
		courseList.add(new CourseName("WGSS", "Women,Gender,Sexuality Studies"));
		courseList.add(new CourseName("WSTNURSE", "Worcester Nursing Program"));
		courseList.add(new CourseName("YIDDISH", "Yiddish"));
		ObservableList<CourseName> obList = FXCollections.observableList(courseList);
		courseMenu.setItems(obList);
    }
    public void setSpireScraper(SpireScraper s) {
    	this.spireScraper = s;
    }
    class CourseName {
    	public String codeName;
    	public String publicName;
    	public String getCodeName() {
    		return codeName;
    	}
    	public void setCodeName(String codeName) {
    		this.codeName = codeName;
    	}
    	public String getPublicName() {
    		return publicName;
    	}
    	public void setPublicName(String publicName) {
    		this.publicName = publicName;
    	}
    	public CourseName(String c, String p) {
    		codeName = c;
    		publicName = p;
    	}
    	public String toString() {
    		return publicName;
    	}
    }

}
