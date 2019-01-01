package scheduleMaker;

public class TimeSlot implements Comparable<TimeSlot> {
	private Time startTime;
	private Time endTime;
	private int day; // 0 = Sunday, 1 = Monday, 2 = Tuesday etc...
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public TimeSlot(Time st, Time et) {
		startTime = st;
		endTime = et;
	}
	public TimeSlot(Time st, Time et, int d) {
		startTime = st;
		endTime = et;
		day = d;
	}
	public Time getStartTime() {
		return startTime;
	}
	public Time getEndTime() {
		return endTime;
	}
	public boolean existConflict(TimeSlot a) {
		if(this.compareTo(a) == 2)
			return false;
		else if(Time.existTimeInBetween(a.getStartTime(), a.getEndTime(), this.getStartTime()) == true || Time.existTimeInBetween(a.getStartTime(), a.getEndTime(), this.getEndTime()) == true) {
			return true;
		}
		else if(Time.existTimeInBetween(this.getStartTime(), this.getEndTime(), a.getStartTime()) == true || Time.existTimeInBetween(this.getStartTime(), this.getEndTime(), a.getEndTime()) == true) {
			return true;
		}
		else if(this.getStartTime().compareTo(a.getStartTime()) == 0 && this.getEndTime().compareTo(a.getEndTime()) == 0) 
			return true;
		else
			return false;
	}
	public String toString() {
		return startTime + " - " + endTime;
	}
	public int compareTo(TimeSlot t) {
		if(this.day != t.day)
			return 2;
		else 
			return this.getEndTime().compareTo(t.getEndTime());
	}
	public TimeSlot clone() {
		return new TimeSlot(startTime.clone(), endTime.clone(), day);
	}
}
