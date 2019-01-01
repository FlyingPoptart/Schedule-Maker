package scheduleMaker;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginScreenController {
	public Button loginButton;
	public TextField netID;
	public PasswordField password;
	public SpireScraper scraper;
	public SpireScraper getScraper() {
		return scraper;
	}

	
	public void handleButtonClick() throws IOException {
		Stage stage = (Stage) loginButton.getScene().getWindow();
		String username = netID.getText();
		String pword = password.getText();
		scraper = new SpireScraper(username, pword);
		stage.setOnCloseRequest(e -> scraper.getDriver().quit());
		boolean isLoggedIn = scraper.login();
		if(isLoggedIn == true) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
			Parent root = (Parent) loader.load();
			
			MainScreenController mainScreenController = loader.getController();
			mainScreenController.setSpireScraper(scraper);
			mainScreenController.initializeProfessorCookies();
			
			
			Stage newStage = new Stage();
			newStage.setTitle("Schedule Maker");
			newStage.setScene(new Scene(root));
			newStage.show();
			newStage.setOnCloseRequest(e -> mainScreenController.getSpireScraper().getDriver().quit());
			
			stage.close();
		}
		else if(isLoggedIn == false) {
			System.out.println("Wrong password");
		}
		else {
			System.out.println("Check your internet connection");
		}
	}
}
