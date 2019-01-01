package scheduleMaker;
import java.util.ArrayList;

public class Course {
	public String getName() {
		return name;
	}
	private String name;
	private ArrayList<Lecture> lectures;
	public void setName(String name) {
		this.name = name;
	}
	public void setLectures(ArrayList<Lecture> lectures) {
		this.lectures = lectures;
	}
	public void setAddons(ArrayList<Addon> addons) {
		this.addons = addons;
	}
	private ArrayList<Addon> addons;
	public Course(String n, ArrayList<Lecture> c, ArrayList<Addon> a) {
		name = n;
		lectures = c;
		addons = a;
	}
	public Course(ArrayList<StudentClass> sc) {
		name = sc.get(0).getCourse();
		addons = new ArrayList<Addon>();
		lectures = new ArrayList<Lecture>();
		
		for(int n = 0; n < sc.size(); n++) {
			if(sc.get(n).getClass().getName().equals("scheduleMaker.Lecture")) {
				lectures.add((Lecture) sc.get(n));
			}
			else if(sc.get(n).getClass().getName().equals("scheduleMaker.Addon")) {
				addons.add((Addon) sc.get(n));
			}
		}
	}
	public String getDetails() {
		String s = "";
		s += name + "\n<Lectures>\n";
		for(Lecture c : lectures) {
			s += c;
		}
		s += "<Addons>\n";
		for(Addon a : addons) {
			s += a;
		}
		return s;
	}
	public Course(String n, ArrayList<Lecture> c) {
		name = n;
		lectures = c;
	}
	public String toString() {
		return name;
	}
	public ArrayList<Lecture> getLectures() {
		return lectures;
	}
	public ArrayList<Addon> getAddons() {
		return addons;
	}
	public void addLecture(Lecture l) {
		lectures.add(l);
	}
	public void addAddon(Addon a) {
		addons.add(a);
	}
	public Lecture randomLecture() {
		if(lectures.size() > 0) {
		int randIndex = (int)(Math.random() * lectures.size());
		return lectures.get(randIndex);
		}
		else
			return null;
	}
	public Addon randomAddon() {
		if(addons.size() > 0) {
		int randIndex = (int)(Math.random() * addons.size());
		return addons.get(randIndex);
		}
		else
			return null;
	}
	public Course clone() {
		ArrayList<Lecture> lecturesB = new ArrayList<Lecture>();
		for(Lecture l : lectures) {
			lecturesB.add(l.clone());
		}
		ArrayList<Addon> addonsB = new ArrayList<Addon>();
		for(Addon a : addons) {
			addonsB.add(a.clone());
		}
		return new Course(name, lecturesB, addonsB);
	}
	public int getNumberClasses() {
		int n = 0;
		if(lectures.size() > 0)
			n++;
		if(addons.size() > 0)
			n++;
		return n;
	}
}
