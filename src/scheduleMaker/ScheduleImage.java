package scheduleMaker;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class ScheduleImage extends JFrame {
	private static final long serialVersionUID = 1L;
	static int width = 2480;
	static int height = 1600;

	public static void main(String args[]) throws Exception {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		
		g.setRenderingHint(
		        RenderingHints.KEY_TEXT_ANTIALIASING,
		        RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		
		drawLayout(g);
		ArrayList<String> strings = getFormattedString(130, "ACCOUNTG 222-01ML (70579)\n4:00PM - 5:15PM", g);
		for(String s: strings) {
			System.out.println(s);
		}
		System.out.println();
	}
	
	public static void openBufferedImage(BufferedImage image) {
		Desktop desktop = Desktop.getDesktop();
		File outputFile = new File("image.jpg");
		
		try {
			ImageIO.write(image, "jpg", outputFile);
		} catch (IOException e) {
		}
		try {
			desktop.open(outputFile);
		} catch (IOException e) {
			try {
				desktop.edit(outputFile);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public static void createSchedule(Schedule s) throws Exception {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		
		drawLayout(g);
		
		ArrayList<StudentClass> studentClasses = s.getStudentClasses();
		ArrayList<Color> colors = new ArrayList<Color>();
		float hueIncrement = 1f/(float) studentClasses.size();
		float hue = 0f;
		float saturation = .25f;
		float brightness = .95f;
		for(int n = 0; n < studentClasses.size(); n++) {
			Color c = Color.getHSBColor(hue, saturation, brightness);
			hue += hueIncrement;
			colors.add(c);
		}
		for(int n = 0; n < studentClasses.size(); n++) {
			drawStudentClass(studentClasses.get(n), g, new Font("Verdana", 0, 15), colors.get(n));
		}
		
		openBufferedImage(image);
	}
	
	static void drawLayout(Graphics2D g) {
		g.setPaint(Color.WHITE);
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Verdana", 0, 24));
		
		g.setPaint(new Color(224, 224, 224));
		g.setStroke(new BasicStroke(1));
		for(int x = 340; x < 2740; x += 300) {
			g.drawLine(x, 80, x, height - 80);
		}
		for(int y = 80; y <= 1520; y += 60) {
			g.drawLine(340, y, 2440, y);
		}
		
		g.setPaint(new Color(16, 16, 16));
		int height = g.getFontMetrics().getAscent();
		
		String s = "12:00 PM";
		int length = g.getFontMetrics().stringWidth(s);
		g.drawString(s, 340 - 20 - length, 800 + height/2);
		s = "12:00 AM";
		g.drawString(s, 340 - 20 - length, 80 + height/2);
		
		for(int n = 1; n <= 11; n++) {
			s = n + ":00 AM";
			length = g.getFontMetrics().stringWidth(s);
			g.drawString(s, 340 - 20 - length, 60 * n + 80 + height/2);
		}
	
		for(int n = 1; n <= 11; n++) {
			s = n + ":00 PM";
			length = g.getFontMetrics().stringWidth(s);
			g.drawString(s, 340 - 20 - length, 60 * n + 800 + height/2);
		}
	}
	
	static int TimeToPixel(Time t) {
		int hour = t.getHour();
		int minute = t.getMinute();
		int pixel = (int) (60 * hour + 80 + ((double) minute / 60 * 60));
		return pixel;
	}
	
	static void drawTimeSlot(TimeSlot t, Graphics2D g, Font f, String s, Color c) throws Exception {
		int day = t.getDay();
		Time startTime = t.getStartTime();
		Time endTime = t.getEndTime();
		
		int height = TimeToPixel(endTime) - TimeToPixel(startTime);
		int width = 260;
		int y = TimeToPixel(startTime);
		int x = 300 * day + 360;
		
		drawTextBox(x, y, width, height, c, s, g, f);
	}
	
	static void drawStudentClass(StudentClass s, Graphics2D g, Font f, Color c) throws Exception {
		ArrayList<TimeSlot> timeSlots = s.getTimeSlots();
		for(int n = 0; n < timeSlots.size(); n++) {
			drawTimeSlot(timeSlots.get(n), g, f, s.toString(), c);
		}
	}
	
	static void drawTextBox(int x, int y, int w, int h, Color c, String s, Graphics2D g, Font f) throws Exception {
		Color savedColor = g.getColor();
		
		g.setFont(f);
		FontMetrics metrics = g.getFontMetrics(f);
		int fontHeight = metrics.getHeight();
		g.setPaint(c);
		g.fillRoundRect(x, y, w, h, 30, 30);
		
		int padding = 10;
		x += padding;
		y += padding;
		w -= 2 * padding;
		h -= 2 * padding;
		
		g.setPaint(new Color(16, 16, 16));
		ArrayList<String> text = getFormattedString(w, s, g);
		
		int startHeight = y + metrics.getAscent();
		for(String st: text) {
			g.drawString(st, x, startHeight);
			startHeight += fontHeight;
		}
		
		g.setColor(savedColor);
	}
	

	
	static ArrayList<String> getFormattedString(int lineWidth, String s, Graphics2D g) throws Exception {
		ArrayList<String> str = new ArrayList<String>();
		s += " ";
		while(g.getFontMetrics().stringWidth(s) > 0) {
			
			String line = getLine(lineWidth, s, g);
			if(line.contains("\n")) {
				line = line.substring(0, line.indexOf("\n"));
				s = s.substring(line.length());
			}
			if(line.indexOf(" ") == -1) {
				str.add(line);
				s = s.substring(line.length());	
			}
			else {
				line = line.substring(0, line.lastIndexOf(" "));
				str.add(line);
				s = s.substring(line.length() + 1);	
			}
			
		}
		return str;
	}
	
	static String getLine(int w, String s, Graphics2D g) throws Exception {
		for(int n = s.length(); n > 0; n--) {
			if(g.getFontMetrics().stringWidth(s) <= w) {
				String str = s.substring(0, n);
				return str;
			}
			s = s.substring(0, n);
		}
		throw new Exception("You're asking for too small of a text box");
	}
	
	
	void setLineWidth(int n, Graphics2D g) {
		g.setStroke(new BasicStroke(n));
	}
}
