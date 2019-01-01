package scheduleMaker;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class ScheduleListController implements Initializable {
	ScheduleItems scheduleItems;
	
	@FXML private ListView<String> conflictListView;
	@FXML private ListView<Schedule> scheduleListView;
	
	public void setScheduleItems(ScheduleItems s) {
		scheduleItems = s;
	}
	
	public void populateScheduleItems() {
		ArrayList<Schedule> scheduleArray = scheduleItems.getSchedules();
		ObservableList<Schedule> obList = FXCollections.observableArrayList(scheduleArray);
		scheduleListView.setItems(obList);
	}
	
	public void populateConflicts() {
		List<String> list = new ArrayList<String>();
		ArrayList<StudentClass> classArray = scheduleItems.getProblematicClasses();
		ArrayList<Integer> occuranceArray = scheduleItems.getOccurances();
		for(int n = 0; n < classArray.size(); n++) {
			String s = classArray.get(n).toString() + "\nConflicts: " + occuranceArray.get(n).toString();
			list.add(s);
		}
		ObservableList<String> obList = FXCollections.observableList(list);
		conflictListView.setItems(obList);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Label placeHolderLabel = new Label("No schedules generated.\nTry removing a course or loosening the \nteacher/class restrictions.");
		placeHolderLabel.setPadding(new Insets(5, 5, 5, 5));
		placeHolderLabel.setAlignment(Pos.TOP_LEFT);
		scheduleListView.setPlaceholder(placeHolderLabel);
		scheduleListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent click) {

		        if (click.getClickCount() == 2) {
		           //Use ListView's getSelected Item
		           Schedule currentItemSelected = scheduleListView.getSelectionModel().getSelectedItem();
		           try {
					ScheduleImage.createSchedule(currentItemSelected);
				} catch (Exception e) {
					e.printStackTrace();
				}
		        }
		    }
		});
	}
}
