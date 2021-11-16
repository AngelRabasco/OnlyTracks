package org.AngelRabasco.OnlyTracks;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.AngelRabasco.OnlyTracks.Model.DAO.UserDAO;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

	@FXML
	private TextField userField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private Button loginButton;
	@FXML
	private Button signupButton;

	public void initialize() {

	}

	@FXML
	protected void logIn() throws IOException {
//		Comprueba la existencia del usuario inntroducido, si este exite carga el menú principal, si no existe muestra un aviso
		UserDAO user = new UserDAO(userField.getText(), passwordField.getText());
		if (user.logIn()) {
			loadMainMenu(user);
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setTitle("Login error");
			alert.setContentText("Parameters dont match");
			alert.showAndWait();
		}
	}

	@FXML
	protected void loadSignUp() throws IOException {
//		Carga le pantalla de creación de cuenta
		App.setRoot("Signup");
	}

	@FXML
	private void loadMainMenu(UserDAO user) throws IOException {
//		Carga la escena del menú principal y envía los datos del usuario introducido
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
			Parent parent = loader.load();
			MainMenuController mainMenuController=loader.getController();
			mainMenuController.loadUserInfo(user);
			Stage stage = new Stage();
			stage.setScene(new Scene(parent));
			stage.setTitle("OnlyTracks");
			stage.setResizable(false);
			Stage currentStage = (Stage) loginButton.getScene().getWindow();
			currentStage.close();
			stage.show();
		} catch (IOException ex) {
			Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
