package org.AngelRabasco.OnlyTracks;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.AngelRabasco.OnlyTracks.Model.User;
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

public class SignupController {

	@FXML
	private TextField userField;
	@FXML
	private TextField emailField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private Button loginButton;
	@FXML
	private Button signupButton;

	public void initialize() {
	}

	@FXML
	protected void signUp() throws IOException {
//		Comprueba que los campos no estén vacios, que no exista un usuario con esos parámetros y lo registra
		if (!userField.getText().trim().equals("")) {
			if (!emailField.getText().trim().equals("")) {
				if (!passwordField.getText().trim().equals("")) {
					UserDAO user = new UserDAO(userField.getText(), emailField.getText(), passwordField.getText());
					if (user.signUp()) {
						loadMainMenu(UserDAO.searchByID(user.getId()));
					} else {
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setHeaderText(null);
						alert.setTitle("Registration error");
						alert.setContentText("User already exists");
						alert.showAndWait();
					}
				} else {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setHeaderText(null);
					alert.setTitle("Field error");
					alert.setContentText("Password cannot be empty");
					alert.showAndWait();
				}
			} else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setTitle("Field error");
				alert.setContentText("Email cannot be empty");
				alert.showAndWait();
			}
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setTitle("Field error");
			alert.setContentText("Name cannot be empty");
			alert.showAndWait();
		}
	}

	@FXML
	public void loadLogIn() throws IOException {
//		Carga la pantalla de inicio de sesión
		App.setRoot("Login");
	}
	
	@FXML
	private void loadMainMenu(User user) throws IOException {
//		Carga el menú principal con los datos del nuevo usuario
		try {	
			FXMLLoader loader=new FXMLLoader(getClass().getResource("MainMenu.fxml"));
			Parent parent=loader.load();
			MainMenuController mainMenuController=loader.getController();
			mainMenuController.loadUserInfo(new UserDAO(user));;
			Stage stage = new Stage();
			stage.setScene(new Scene(parent));
			stage.setTitle("Main window");
			stage.setResizable(false);
			Stage currentStage=(Stage) loginButton.getScene().getWindow();
			currentStage.close();
			stage.show();
		}catch (IOException ex){
			Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
