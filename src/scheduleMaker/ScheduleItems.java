package scheduleMaker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class ScheduleItems {
	public ArrayList<Schedule> getSchedules() {
		return schedules;
	}
	public void setSchedules(ArrayList<Schedule> schedules) {
		this.schedules = schedules;
	}
	public ArrayList<StudentClass> getProblematicClasses() {
		return problematicClasses;
	}
	public void setProblematicClasses(ArrayList<StudentClass> problematicClasses) {
		this.problematicClasses = problematicClasses;
	}
	public ArrayList<Integer> getOccurances() {
		return occurances;
	}
	public void setOccurances(ArrayList<Integer> occurances) {
		this.occurances = occurances;
	}
	public ScheduleItems(ArrayList<Schedule> s, ArrayList<StudentClass> pc, ArrayList<Integer> o) {
		schedules = s;
		ProblematicItem[] problematicItems = new ProblematicItem[pc.size()];
		for(int n = 0; n < pc.size(); n++) {
			problematicItems[n] = new ProblematicItem(pc.get(n), o.get(n));
		}
		Arrays.sort(problematicItems);
		ProblematicItem[] temp = new ProblematicItem[problematicItems.length];
		for(int n = 0; n < problematicItems.length; n++) {
			temp[n] = problematicItems[problematicItems.length - 1 - n];
		}
		problematicItems = temp;
		
		ArrayList<StudentClass> prob = new ArrayList<StudentClass>();
		ArrayList<Integer> occu = new ArrayList<Integer>();
		for(int n = 0; n < pc.size(); n++) {
			prob.add(problematicItems[n].getProblematicClass());
			occu.add(problematicItems[n].getOccurances());
		}
		problematicClasses = prob;
		occurances = occu;
		scheduleNumber = new ArrayList<Integer>();
		for(int n = 0; n < s.size(); n++) {
			scheduleNumber.add(n);
		}
	}
	private ArrayList<Schedule> schedules;
	private ArrayList<StudentClass> problematicClasses;
	private ArrayList<Integer> occurances;
	private ArrayList<Integer> scheduleNumber;
	
	public String toString() {
		String st = "";
		for(Schedule s: schedules) 
			st += s + " ";
		for(StudentClass s: problematicClasses)
			st += s + " ";
		for(Integer i: occurances)
			st += i + " ";
		return st;
	}
	
	@SuppressWarnings("rawtypes")
	class ProblematicItem implements Comparable {
		public ProblematicItem(StudentClass problematicClass, Integer occurances) {
			super();
			this.problematicClass = problematicClass;
			this.occurances = occurances;
		}
		private StudentClass problematicClass;
		private Integer occurances;
		public StudentClass getProblematicClass() {
			return problematicClass;
		}
		public void setProblematicClasses(StudentClass problematicClass) {
			this.problematicClass = problematicClass;
		}
		public Integer getOccurances() {
			return occurances;
		}
		public void setOccurances(Integer occurances) {
			this.occurances = occurances;
		}

		@Override
		public int compareTo(Object o) {
			ProblematicItem item = (ProblematicItem) o;
			return occurances.compareTo(item.getOccurances());
		}
	}
}
