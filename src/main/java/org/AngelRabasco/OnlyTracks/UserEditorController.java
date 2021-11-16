package org.AngelRabasco.OnlyTracks;

import java.io.IOException;

import org.AngelRabasco.OnlyTracks.Model.DAO.UserDAO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserEditorController {
	UserDAO user = new UserDAO();

	@FXML
	private TextField nameField;
	@FXML
	private TextField emailField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private Button confirmButton;
	@FXML
	private Button cancelButton;

	public void initialize() {
	}

	@FXML
	private void editUser() throws IOException {
//		comprueba que los campos no están vacios y actualiaza al usuario
		if (!nameField.getText().trim().equals("")) {
			if (!emailField.getText().trim().equals("")) {
				if (!passwordField.getText().trim().equals("")) {
					new UserDAO(this.user.getId(), nameField.getText(), emailField.getText(), new BCryptPasswordEncoder().encode(passwordField.getText())).saveUser();
					closeWindow();
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

	public void loadUser(UserDAO user) {
//		Carga los datos del usuario y los muestra en los campos
		this.user = user;
		this.nameField.setText(this.user.getUsername());
		this.emailField.setText(this.user.getEmail());
	}

	@FXML
	private void closeWindow() {
//		Cierra la escena
		Stage currentStage = (Stage) cancelButton.getScene().getWindow();
		currentStage.close();
	}
}
