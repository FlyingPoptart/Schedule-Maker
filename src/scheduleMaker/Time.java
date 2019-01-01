package scheduleMaker;

import java.text.DecimalFormat;

public class Time implements Comparable<Time> {
	private int hour;
	private int minute;
	public Time(int h, int m) {
		hour = h;
		minute = m;
	}
	public int getHour() {
		return hour;
	}
	public int getMinute() {
		return minute;
	}
	public int compareTo(Time a) {
		if(this.hour > a.hour) {
			return 1;
		}
		else if(this.hour < a.hour) {
			return -1;
		}
		else if(this.minute > a.minute) {
			return 1;
		}
		else if(this.minute < a.minute) {
			return -1;
		}
		else {
			return 0;
		}		
	}
	public String toString() {
		String t;
		int tempHour = hour;
		if(hour > 12) {
			tempHour -= 12;
			t = "PM";
		}
		else if(hour > 11) {
			t = " PM";
		}
		else {
			t = " AM";
		}
		String pattern = "00";
		DecimalFormat minuteFormat = new DecimalFormat(pattern);
		String s = tempHour + ":" + minuteFormat.format(minute) + t;
		return s;
	}
	public static boolean existTimeInBetween(Time start, Time end, Time test) {
		if(test.compareTo(start) == 1 && test.compareTo(end) == -1) {
			return true; //returns true if the time test is equal to start or end
		}
		return false;
	}
	public Time clone() {
		return new Time(hour, minute);
	}
}
