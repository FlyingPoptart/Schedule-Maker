package scheduleMaker;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ScheduleMaker {
	private Schedule schedule;
	private ArrayList<StudentClass> problematicClasses;
	
	public Schedule getSchedule() {
		return schedule;
	}
	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}
	public ArrayList<StudentClass> getProblematicClasses() {
		return problematicClasses;
	}
	public void setProblematicClasses(ArrayList<StudentClass> problematicClasses) {
		this.problematicClasses = problematicClasses;
	}

	public ScheduleMaker(Schedule s, ArrayList<StudentClass> pc) {
		schedule = s;
		problematicClasses = pc;
	}
	
	public static ScheduleItems createSchedules(ArrayList<Course> courses, ArrayList<StudentClass> avoidClasses) {
		ArrayList<Schedule> workingSchedules = new ArrayList<Schedule>();
		int coursesNumberOfStudentClasses = getNumStudentClasses(courses);
		ArrayList<StudentClass> problematicClassesList = new ArrayList<StudentClass>();
		for(int counter = 0; counter < 10000; counter++) {
			ScheduleMaker sm = createSchedule(courses, avoidClasses);
			Schedule s = sm.getSchedule();
			problematicClassesList.addAll(sm.getProblematicClasses());
			if(s != null && s.getStudentClasses().size() == coursesNumberOfStudentClasses) {
				workingSchedules.add(s);
			}
		}
		removeDuplicateSchedules(workingSchedules);
		ArrayList<StudentClass> uniqueStudentClasses = new ArrayList<StudentClass>();
		for(StudentClass sc : problematicClassesList) {
			boolean contains = false;
			for(int n = 0; n < uniqueStudentClasses.size(); n++) {
				if(uniqueStudentClasses.get(n).getName().equals(sc.getName()))
					contains = true;
			}
			if(contains == false)
				uniqueStudentClasses.add(sc);
		}
		ArrayList<Integer> numOccurances = new ArrayList<Integer>();
		for(int n = 0; n < uniqueStudentClasses.size(); n++) {
			numOccurances.add(0);
		}
		for(int n = 0; n < uniqueStudentClasses.size(); n++) {
			for(int k = 0; k < problematicClassesList.size(); k++) {
				if(uniqueStudentClasses.get(n).getName().equals(problematicClassesList.get(k).getName()))
					numOccurances.set(n, numOccurances.get(n) + 1);
			}
		}
		return new ScheduleItems(workingSchedules, uniqueStudentClasses, numOccurances);
	}
	public static int getNumStudentClasses(ArrayList<Course> courses) {
		int coursesNumberOfStudentClasses = 0;
		for(Course c : courses) {
			coursesNumberOfStudentClasses += c.getNumberClasses();
		}
		return coursesNumberOfStudentClasses;
	}
	public static void removeDuplicateSchedules(ArrayList<Schedule> schedules) {
		for(int n = 0; n < schedules.size(); n++) {
			for(int k = n+1; k < schedules.size(); k++) {
				if(isScheduleSame(schedules.get(n), schedules.get(k))) {
					schedules.remove(k);
					k--;
				}
			}
		}
	}
    public static boolean isScheduleSame(Schedule schedule1, Schedule schedule2) {
        String[] names1 = Schedule.getNames(schedule1);
        String[] names2 = Schedule.getNames(schedule2);
        Arrays.sort(names1);
        Arrays.sort(names2);
        if(Arrays.equals(names1, names2)) {
        	return true;

        }
        else {
        	return false;

        }
        
    }

	public static ScheduleMaker createSchedule(ArrayList<Course> courses, ArrayList<StudentClass> avoidClasses) {
		ArrayList<Course> coursesCopy = new ArrayList<Course>();
		ArrayList<StudentClass> problematicClasses = new ArrayList<StudentClass>();
		for(Course c : courses) {
			coursesCopy.add(c.clone());
		}
		Collections.shuffle(courses);
		ArrayList<StudentClass> studentClasses = new ArrayList<StudentClass>();
		Schedule s = new Schedule(studentClasses);
		boolean existConflict;
		for(Course c : coursesCopy) {
			do {
				existConflict = true;
				if(c.getLectures().size() > 0) {
					Lecture randLecture = c.randomLecture();
					if(!(s.existConflict(randLecture))) {
						s.addStudentClass(randLecture);
						existConflict = false;
					}
					else {
						c.getLectures().remove(randLecture);
						problematicClasses.add(randLecture);
						if(c.getLectures().size() == 0) {
							existConflict = false;
						}
					}
				}
				else {
					existConflict = false;
				}
			} while(existConflict == true);
			do {
				existConflict = true;
				if(c.getAddons().size() > 0) {
					Addon randAddon = c.randomAddon();
					if(!(s.existConflict(randAddon))) {
						s.addStudentClass(randAddon);
						existConflict = false;
					}
					else {
						c.getAddons().remove(randAddon);
						problematicClasses.add(randAddon);
						if(c.getAddons().size() == 0) {
							existConflict = false;
						}
					}
				}
				else {
					existConflict = false;
				}
			} while(existConflict == true);
		}
		ArrayList<StudentClass> scheduleClasses = s.getStudentClasses();
		boolean containsAvoidClass = false;
		for(int n = 0; n < scheduleClasses.size(); n++) {
			for(int k = 0; k < avoidClasses.size(); k++) {
				if(avoidClasses.get(k).getName().equals(scheduleClasses.get(n).getName())) {
					containsAvoidClass = true;
					continue;
				}
				if(containsAvoidClass)
					continue;
			}
		}
		if(containsAvoidClass) 
			return new ScheduleMaker(null, problematicClasses);
		else
			return new ScheduleMaker(s, problematicClasses);
	}	
}
