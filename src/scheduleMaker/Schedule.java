package scheduleMaker;
import java.util.ArrayList;

public class Schedule {
	private ArrayList<StudentClass> studentClasses;
	public Schedule(ArrayList<StudentClass> sc) {
		studentClasses = sc;
	}
	public ArrayList<StudentClass> getStudentClasses() {
		return studentClasses;
	}
	public void setStudentClasses(ArrayList<StudentClass> studentClasses) {
		this.studentClasses = studentClasses;
	}
	public String toString() {
		String s = "";
		for(StudentClass c : studentClasses) {
			s += c + "\n";
		}
		return s;
	}
	public void addStudentClass(StudentClass sc) {
		studentClasses.add(sc);
	}
	public boolean existConflict(StudentClass sc) {
		if(this.getStudentClasses().size() == 0)
			return false;
		boolean existConflict = false;
		ArrayList<TimeSlot> totalTS = new ArrayList<TimeSlot>();
		ArrayList<StudentClass> tempStudentClasses = this.getStudentClasses();
		for(StudentClass c : tempStudentClasses) {
			ArrayList<TimeSlot> cl = c.getTimeSlots();
			for(TimeSlot ts : cl) {
				totalTS.add(ts);
			}
		}
		for(int n = 0; n < totalTS.size(); n++) {
			for(int k = 0; k < sc.getTimeSlots().size(); k++) {
				if(sc.getTimeSlots().get(k).existConflict(totalTS.get(n)))
					existConflict = true;
			}
		}
		return existConflict;
	}
	public static String[] getNames(Schedule s) {
		String[] array = new String[s.getStudentClasses().size()];
		for(int n = 0; n < array.length; n++) {
			array[n] = s.getStudentClasses().get(n).getName();
		}
		return array;
		
	}
}


