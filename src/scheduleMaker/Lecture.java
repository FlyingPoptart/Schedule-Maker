package scheduleMaker;
import java.util.ArrayList;

import org.openqa.selenium.WebDriver;

public class Lecture extends StudentClass {
	public Lecture(String mapsLocation, String course, String location, String name, ArrayList<TimeSlot> timeSlots,
			String professor, WebDriver driver) {
		super(mapsLocation, course, location, name, timeSlots, professor, driver);
	}
	public Lecture(String mapsLocation, String course, String location, String name, ArrayList<TimeSlot> timeSlots,
			String professor) {
		super(mapsLocation, course, location, name, timeSlots, professor);
	}
	public Lecture(String mapsLocation, String course, String location, String name, ArrayList<TimeSlot> timeSlots,
			Professor professor) {
		super(mapsLocation, course, location, name, timeSlots, professor);
	}
	public Lecture clone() {
		ArrayList<TimeSlot> timeSlotsb = new ArrayList<TimeSlot>();
		for(TimeSlot ts : timeSlots) {
			timeSlotsb.add(ts.clone());
		}
		return new Lecture(mapsLocation, course, location, name, timeSlots,
				professor);
	}
}
