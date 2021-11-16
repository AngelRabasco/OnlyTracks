package org.AngelRabasco.OnlyTracks;

import java.io.IOException;

import org.AngelRabasco.OnlyTracks.Model.Album;
import org.AngelRabasco.OnlyTracks.Model.Genre;
import org.AngelRabasco.OnlyTracks.Model.Playlist;
import org.AngelRabasco.OnlyTracks.Model.Track;
import org.AngelRabasco.OnlyTracks.Model.User;
import org.AngelRabasco.OnlyTracks.Model.DAO.PlaylistDAO;
import org.AngelRabasco.OnlyTracks.Model.DAO.TrackDAO;
import org.AngelRabasco.OnlyTracks.Model.DAO.UserDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainMenuController {
	UserDAO user = new UserDAO();
	ObservableList<Track> selectedTrack=FXCollections.observableArrayList();

	@FXML
	private HBox userBox;
	@FXML
	private ImageView userImage;
	@FXML
	private Text userField;
	@FXML
	private ListView<Playlist> playlistList;
	@FXML
	private Button removeButton;
	@FXML
	private Button addButton;
	@FXML
	private TabPane tabPane;
	@FXML
	private TableView<Track> tracksTable;
	@FXML
	private TableColumn<Track, String> tracksName;
	@FXML
	private TableColumn<Track, Album> tracksAlbum;
	@FXML
	private TableColumn<Track, Genre> tracksGenre;
	@FXML
	private TableColumn<Track, Integer> tracksLength;
	@FXML
	private TableColumn<Track, Integer> tracksReproductions;
	@FXML
	private TableView<Playlist> userPlaylistTable;
	@FXML
	private TableColumn<Playlist, User> userPlaylistOwner;
	@FXML
	private TableColumn<Playlist, String> userPlaylistName;
	@FXML
	private TableColumn<Playlist, String> userPlaylistDescription;
	@FXML
	private Button subscribeButton;
	@FXML
	private Tab playlistTracks;
	@FXML
	private TableView<Track> playlistTracksTable;
	@FXML
	private TableColumn<Track, String> playlistTracksName;
	@FXML
	private TableColumn<Track, Album> playlistTracksAlbum;
	@FXML
	private TableColumn<Track, Genre> playlistTracksGenre;
	@FXML
	private TableColumn<Track, Integer> playlistTracksLength;
	@FXML
	private TableColumn<Track, Integer> playlistTracksReproductions;
	@FXML
	private Button addTrackButton;
	@FXML
	private Button removeTrackButton;
	@FXML
	private Text trackName;

	public void initialize() {
		tracksName.setCellValueFactory(new PropertyValueFactory<Track, String>("name"));
		tracksAlbum.setCellValueFactory(new PropertyValueFactory<Track, Album>("album"));
		tracksGenre.setCellValueFactory(new PropertyValueFactory<Track, Genre>("genre"));
		tracksLength.setCellValueFactory(new PropertyValueFactory<Track, Integer>("length"));
		tracksReproductions.setCellValueFactory(new PropertyValueFactory<Track, Integer>("reproductions"));
		
		userPlaylistOwner.setCellValueFactory(new PropertyValueFactory<Playlist, User>("owner"));
		userPlaylistName.setCellValueFactory(new PropertyValueFactory<Playlist, String>("name"));
		userPlaylistDescription.setCellValueFactory(new PropertyValueFactory<Playlist, String>("description"));
		
		playlistTracksName.setCellValueFactory(new PropertyValueFactory<Track, String>("name"));
		playlistTracksAlbum.setCellValueFactory(new PropertyValueFactory<Track, Album>("album"));
		playlistTracksGenre.setCellValueFactory(new PropertyValueFactory<Track, Genre>("genre"));
		playlistTracksLength.setCellValueFactory(new PropertyValueFactory<Track, Integer>("length"));
		playlistTracksReproductions.setCellValueFactory(new PropertyValueFactory<Track, Integer>("reproductions"));
	}

	protected void loadUserInfo(UserDAO userLogin) {
//		Carga el usuario que fué introducido en el login, muestra su nombre en su campo de texto y carga campos
		this.user = userLogin;
		this.userField.setText(user.getUsername());
		loadTables();
	}

	protected void loadTables() {
//		Carga las playlists, las canciones y las tablas del resto de usuarios
		loadPlaylistList();
		loadTracks();
		this.userPlaylistTable.setItems(FXCollections.observableArrayList(PlaylistDAO.searchByNotOwned(user.getId())));
	}
	
	private void loadPlaylistList() {
//		Carga las playlists del usuario y sus subscripciones
		this.playlistList.setItems(FXCollections.observableArrayList(PlaylistDAO.searchByOwner(user.getId())));
		ObservableList<Playlist> subscribedList = FXCollections.observableArrayList(UserDAO.searchSubscriptionsById(user.getId()));
		for (Playlist i : subscribedList) {
			this.playlistList.getItems().add(i);
		}
	}
	
	private void loadTracks() {
//		Carga todas las canciones
		this.tracksTable.setItems(FXCollections.observableArrayList(TrackDAO.searchAllTracks()));
	}
	
	@FXML
	private void loadPlaylistTracks() {
//		Carga las canciones de una playlist en su pestaña, en caso de que esta sea del usuario le permitirá modificar las canciones de esta
		if (playlistList.getSelectionModel().getSelectedItem() != null) {
			if (playlistTracks.isDisable()) {
				playlistTracks.setDisable(false);
			}
			if (playlistList.getSelectionModel().getSelectedItem().getOwner().getId() == user.getId()) {
				addTrackButton.setDisable(false);
				removeTrackButton.setDisable(false);
			} else {
				addTrackButton.setDisable(true);
				removeTrackButton.setDisable(true);
			}
			playlistTracks.setText(playlistList.getSelectionModel().getSelectedItem().getName());
			playlistTracksTable.setItems(FXCollections.observableArrayList(PlaylistDAO.searchTracksById(playlistList.getSelectionModel().getSelectedItem().getId())));
			tabPane.getSelectionModel().select(playlistTracks);
		}
	}
	
	@FXML
	private void loadPlayer() {
//		Carga la canción en selectedTrack y muestra su nombre en el reproductor
		switch (tabPane.getSelectionModel().getSelectedIndex()) {
		case 0:
			if (tracksTable.getSelectionModel().getSelectedItem() != null) {
				this.selectedTrack.clear();
				this.selectedTrack.add(tracksTable.getSelectionModel().getSelectedItem());
				trackName.setText(selectedTrack.get(0).getName());
			}
			break;
		case 2:
			if (playlistTracksTable.getSelectionModel().getSelectedItem() != null) {
				this.selectedTrack.clear();
				this.selectedTrack.add(playlistTracksTable.getSelectionModel().getSelectedItem());
				trackName.setText(selectedTrack.get(0).getName());
			}
			break;
		}
	}
	
	@FXML
	private void subscribe() {
//		Suscribe al usuario a una lista de reproducción en caso de no estarlo ya
		if (userPlaylistTable.getSelectionModel().getSelectedItem() != null) {
			PlaylistDAO selectedPlaylist=new PlaylistDAO(userPlaylistTable.getSelectionModel().getSelectedItem());
			PlaylistDAO.subscribe(selectedPlaylist.getId(), user.getId());
			loadPlaylistList();
		}
	}
	
	@FXML
	private void removePlaylist() {
//		Si la lista es propiedad del usuario esta es eliminada, si no se desuscribe
		if (playlistList.getSelectionModel().getSelectedItem() != null) {
			PlaylistDAO selectedPlaylist = new PlaylistDAO(playlistList.getSelectionModel().getSelectedItem());
			if (selectedPlaylist.getOwner().getId() == user.getId()) {
				selectedPlaylist.removePlaylist();
			} else {
				PlaylistDAO.removePlaylistUserRelation(selectedPlaylist.getId(), user.getId());
			}
			loadPlaylistList();
		}
	}

	@FXML
	private void removeFromPlaylist() {
//		Elimina de una playlist la canción seleccionada
		if (playlistList.getSelectionModel().getSelectedItem() != null) {
			if (playlistTracksTable.getSelectionModel().getSelectedItem() != null) {
				PlaylistDAO.removePlaylistTrackRelation(playlistList.getSelectionModel().getSelectedItem().getId(),playlistTracksTable.getSelectionModel().getSelectedItem().getId());
				playlistTracksTable.getItems().remove(playlistTracksTable.getSelectionModel().getSelectedItem());
			}
		}
	}
	
	@FXML
	private void play() {
//		Incrementa el número de reproducciones de la canción seleccionada
		if(!this.selectedTrack.isEmpty()) {
			TrackDAO.increaseReproductions(this.selectedTrack.get(0).getId());
			loadTracks();
			playlistTracksTable.setItems(FXCollections.observableArrayList(PlaylistDAO.searchTracksById(playlistList.getSelectionModel().getSelectedItem().getId())));
		}
	}
	
	@FXML
	private void addToPlaylist() {
//		Carga la pantalla y te muestra todas las canciones que no forman parte de la playlist para poder añadirlas
		if (playlistList.getSelectionModel().getSelectedItem() != null) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("PlaylistEditor.fxml"));
				Parent parent = loader.load();
				PlaylistEditorController playlistEditorController = loader.getController();
				playlistEditorController.loadTracks(user, new PlaylistDAO(playlistList.getSelectionModel().getSelectedItem()));
				Stage stage = new Stage();
				stage.setScene(new Scene(parent));
				stage.setTitle("Add to playlist");
				stage.setResizable(false);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.showAndWait();
				playlistTracksTable.setItems(FXCollections.observableArrayList(PlaylistDAO.searchTracksById(playlistList.getSelectionModel().getSelectedItem().getId())));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@FXML
	private void loadPlaylistCreator() {
//		Carga una pantalla donde poder crear una nueva playlist
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("PlaylistCreator.fxml"));
			Parent parent = loader.load();
			PlaylistCreatorController playlistCreatorController = loader.getController();
			playlistCreatorController.setFields(user);
			Stage stage = new Stage();
			stage.setScene(new Scene(parent));
			stage.setTitle("New Playlist");
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
			loadTables();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void loadUserEditor() {
//		Permite modificar los parametros del usuario actual
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("UserEditor.fxml"));
			Parent parent = loader.load();
			UserEditorController userditorController = loader.getController();
			userditorController.loadUser(user);
			Stage stage = new Stage();
			stage.setScene(new Scene(parent));
			stage.setTitle("New Playlist");
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
			loadTables();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
