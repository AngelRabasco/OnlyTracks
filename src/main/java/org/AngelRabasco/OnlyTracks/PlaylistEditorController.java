package org.AngelRabasco.OnlyTracks;

import org.AngelRabasco.OnlyTracks.Model.Album;
import org.AngelRabasco.OnlyTracks.Model.Genre;
import org.AngelRabasco.OnlyTracks.Model.Track;
import org.AngelRabasco.OnlyTracks.Model.DAO.PlaylistDAO;
import org.AngelRabasco.OnlyTracks.Model.DAO.UserDAO;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class PlaylistEditorController {
	UserDAO user = new UserDAO();
	PlaylistDAO playlist = new PlaylistDAO();

	@FXML
	private TableView<Track> trackTable;
	@FXML
	private TableColumn<Track, String> trackName;
	@FXML
	private TableColumn<Track, Album> trackAlbum;
	@FXML
	private TableColumn<Track, Genre> trackGenre;
	@FXML
	private TableColumn<Track, Integer> trackLength;
	@FXML
	private TableColumn<Track, Integer> trackReproductions;
	@FXML
	private Button exitButton;
	@FXML
	private Button addButton;
	
	public void initialize() {
		trackName.setCellValueFactory(new PropertyValueFactory<Track, String>("name"));
		trackAlbum.setCellValueFactory(new PropertyValueFactory<Track, Album>("album"));
		trackGenre.setCellValueFactory(new PropertyValueFactory<Track, Genre>("genre"));
		trackLength.setCellValueFactory(new PropertyValueFactory<Track, Integer>("length"));
		trackReproductions.setCellValueFactory(new PropertyValueFactory<Track, Integer>("reproductions"));
	}

	public void loadTracks(UserDAO user, PlaylistDAO playlist) {
//		Carga el usuario, la playlist y las canciones que no sean parte de la playlist
		this.user = user;
		this.playlist = playlist;
		trackTable.setItems(FXCollections.observableArrayList(PlaylistDAO.searchTracksNotInPlaylist(this.playlist.getId())));
	}
	
	@FXML
	private void addTrack() {
//		Añade la canción elegida a la playlist
		if(trackTable.getSelectionModel().getSelectedItem()!=null) {
			PlaylistDAO.createPlaylistTrackRelation(playlist.getId(), trackTable.getSelectionModel().getSelectedItem().getId());
			trackTable.getItems().remove(trackTable.getSelectionModel().getSelectedItem());
		}
	}
	
	@FXML
	private void closeWindow() {
//		Cierra la escena
		Stage currentStage = (Stage) addButton.getScene().getWindow();
		currentStage.close();
	}
}
