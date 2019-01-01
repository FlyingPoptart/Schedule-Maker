package scheduleMaker;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;

public abstract class StudentClass {
	public StudentClass(String mapsLocation, String course, String location, String name, ArrayList<TimeSlot> timeSlots,
			Professor professor) {
		super();
		this.mapsLocation = mapsLocation;
		this.course = course;
		this.location = location;
		this.name = name;
		this.timeSlots = timeSlots;
		this.professor = professor;
	}
	public StudentClass(String mapsLocation, String course, String location, String name, ArrayList<TimeSlot> timeSlots,
			String professor, WebDriver driver) {
		super();
		this.mapsLocation = mapsLocation;
		this.course = course;
		this.location = location;
		this.name = name;
		this.timeSlots = timeSlots;
		if(professor == null) {
			this.professor = null;
		}
		else {
			this.professor = new Professor(professor, driver);	
		}
	}
	public StudentClass(String mapsLocation, String course, String location, String name, ArrayList<TimeSlot> timeSlots,
			String professor) {
		super();
		this.mapsLocation = mapsLocation;
		this.course = course;
		this.location = location;
		this.name = name;
		this.timeSlots = timeSlots;
		this.professor = new Professor(professor);
	}
	public String getMapsLocation() {
		return mapsLocation;
	}
	public void setMapsLocation(String mapsLocation) {
		this.mapsLocation = mapsLocation;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<TimeSlot> getTimeSlots() {
		return timeSlots;
	}
	public void setTimeSlots(ArrayList<TimeSlot> timeSlots) {
		this.timeSlots = timeSlots;
	}
	public Professor getProfessor() {
		return professor;
	}
	public void setProfessor(Professor professor) {
		this.professor = professor;
	}
	protected String mapsLocation;
	protected String course;
	protected String location;
	protected String name;
	protected ArrayList<TimeSlot> timeSlots;
	protected Professor professor;
	public String toString() {
		String tString = "";
		tString += name;
		return tString;
	}
	public ArrayList<String> getDetails() {
		ArrayList<String> details = new ArrayList<String>();
		for(int n = 0; n < timeSlots.size(); n++) {
			details.add(this.toString() + "\n" + timeSlots.get(n));
		}
		return details;
	}
}
