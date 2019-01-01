package scheduleMaker;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;

public class Addon extends StudentClass {
	public Addon(String mapsLocation, String course, String location, String name, ArrayList<TimeSlot> timeSlots,
			String professor, WebDriver driver) {
		super(mapsLocation, course, location, name, timeSlots, professor, driver);
	}
	public Addon(String mapsLocation, String course, String location, String name, ArrayList<TimeSlot> timeSlots,
			String professor) {
		super(mapsLocation, course, location, name, timeSlots, professor);
	}
	public Addon(String mapsLocation, String course, String location, String name, ArrayList<TimeSlot> timeSlots,
			Professor professor) {
		super(mapsLocation, course, location, name, timeSlots, professor);
	}
	public Addon clone() {
		ArrayList<TimeSlot> timeSlotsb = new ArrayList<TimeSlot>();
		for(TimeSlot ts : timeSlots) {
			timeSlotsb.add(ts.clone());
		}
		return new Addon(mapsLocation, course, location, name, timeSlots, professor);
	}
}
