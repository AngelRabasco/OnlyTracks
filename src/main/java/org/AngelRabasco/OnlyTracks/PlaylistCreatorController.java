package org.AngelRabasco.OnlyTracks;

import org.AngelRabasco.OnlyTracks.Model.Track;
import org.AngelRabasco.OnlyTracks.Model.DAO.PlaylistDAO;
import org.AngelRabasco.OnlyTracks.Model.DAO.TrackDAO;
import org.AngelRabasco.OnlyTracks.Model.DAO.UserDAO;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PlaylistCreatorController {
	UserDAO user = new UserDAO();

	@FXML
	private TextField nameField;
	@FXML
	private TextArea descriptionField;
	@FXML
	private Button cancelButton;
	@FXML
	private Button createButton;
	@FXML
	private ListView<Track> addedTracks;
	@FXML
	private Button addButton;
	@FXML
	private Button removeButton;
	@FXML
	private ListView<Track> allTracks;

	public void initialize() {

	}

	@FXML
	private void createPlaylist() {
//		Comprueba que los campos no estén vacios y que la playlist vaya a tener como mínimo una canción, en tal caso creará una nueva playlist
		if (!nameField.getText().trim().equals("") || !descriptionField.getText().trim().equals("")) {
			if (!addedTracks.getItems().isEmpty()) {
				Integer playlistId = new PlaylistDAO(nameField.getText(), descriptionField.getText(), user).savePlaylist();
				for (Track track : addedTracks.getItems()) {
					PlaylistDAO.createPlaylistTrackRelation(playlistId, track.getId());
				}
				closeWindow();
			} else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setTitle("Field error");
				alert.setContentText("A playlist needs tracks");
				alert.showAndWait();
			}
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setTitle("Field error");
			alert.setContentText("Name and description must be filled");
			alert.showAndWait();
		}
	}

	@FXML
	private void moveLeft() {
//		Mueve una canción de la lista de canciones a la lista de la nueva playlist
		if (allTracks.getSelectionModel().getSelectedItem() != null) {
			addedTracks.getItems().add(allTracks.getSelectionModel().getSelectedItem());
			allTracks.getItems().remove(allTracks.getSelectionModel().getSelectedItem());
		}
	}
	@FXML
	private void moveRight() {
//		Quita una canción de la lista de la nueva playlist y la devuelve a la lista de canciones
		if (addedTracks.getSelectionModel().getSelectedItem() != null) {
			allTracks.getItems().add(addedTracks.getSelectionModel().getSelectedItem());
			addedTracks.getItems().remove(addedTracks.getSelectionModel().getSelectedItem());
		}
	}

	public void setFields(UserDAO user) {
//		Carga el usuario y todas las canciones
		this.user = user;
		this.allTracks.setItems(FXCollections.observableArrayList(TrackDAO.searchAllTracks()));
	}

	@FXML
	private void closeWindow() {
//		Cierra la escena
		Stage currentStage = (Stage) cancelButton.getScene().getWindow();
		currentStage.close();
	}
}
