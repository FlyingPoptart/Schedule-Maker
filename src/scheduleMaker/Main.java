package scheduleMaker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	public static void main(String args[]) {
		launch(args);
	}

	public void start(Stage window) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginScreen.fxml"));
		Parent root = (Parent) loader.load();
		window.setTitle("Login Screen");
		window.setResizable(false);
		window.setScene(new Scene(root));
		window.show();
	}
}
