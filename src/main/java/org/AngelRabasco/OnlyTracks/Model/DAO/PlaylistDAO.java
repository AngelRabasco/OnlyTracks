package org.AngelRabasco.OnlyTracks.Model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.AngelRabasco.OnlyTracks.Model.Playlist;
import org.AngelRabasco.OnlyTracks.Model.Track;
import org.AngelRabasco.OnlyTracks.Model.User;
import org.AngelRabasco.OnlyTracks.Util.Connect;

public class PlaylistDAO extends Playlist {
	private final static String getById = "SELECT id,name,description,owner FROM playlists WHERE id=?";
	private final static String getByOwner = "SELECT id,name,description FROM playlists WHERE owner=?";
	private final static String getOtherPlaylists = "SELECT id,name,description,owner FROM playlists WHERE owner!=?";
	private final static String getSubscribersById = "SELECT users.id,users.username,users.email,users.profilePicture FROM users JOIN users_playlists ON users.id=users_playlists.idUser WHERE users_playlists.idPlaylist=?";
	private final static String getTracksById = "SELECT tracks.id,tracks.name,tracks.length,tracks.albumId,tracks.genreId,tracks.reproductions FROM tracks JOIN tracks_playlists ON tracks_playlists.idTrack=tracks.id WHERE tracks_playlists.idPlaylist=?";
	private final static String getTracksNotInPlaylist="SELECT tracks.id,tracks.name,tracks.length,tracks.albumId,tracks.genreId,tracks.reproductions FROM tracks WHERE tracks.id NOT IN (SELECT tracks.id FROM tracks JOIN tracks_playlists ON tracks_playlists.idTrack=tracks.id WHERE tracks_playlists.idPlaylist=?)";
	private final static String insertUpdate = "INSERT INTO playlists (id,name,description,owner) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE name=?,description=?,owner=?";
	private final static String insertPlaylistTrackRelation="INSERT INTO tracks_playlists (idTrack,idPlaylist) VALUES (?,?)";
	private final static String insertSubscription="INSERT INTO users_playlists (idPlaylist,idUser) VALUES (?,?) ON DUPLICATE KEY UPDATE idPlaylist=idPlaylist";
	private final static String delete = "DELETE FROM playlists WHERE id=?";
	private final static String deleteSubscription="DELETE FROM users_playlists WHERE idPlaylist=? AND idUser=?";
	private final static String deleteTrackFromPlaylist="DELETE FROM tracks_playlists WHERE idPlaylist=? AND idTrack=?";

	public PlaylistDAO() {
		super();
	}
	public PlaylistDAO(Playlist playlist) {
		super(playlist.getId(),
				playlist.getName(),
				playlist.getDescription(),
				playlist.getOwner(),
				playlist.getTrackList(),
				playlist.getSubscribers());
	}
	public PlaylistDAO(Integer id, String name, String description, User owner, List<Track> trackList, List<User> subscribers) {
		super(id, name, description, owner, trackList, subscribers);
	}
	public PlaylistDAO(Integer id, String name, String description, User owner) {
		super(id, name, description, owner);
	}
	public PlaylistDAO(String name, String description, User owner, List<Track> trackList, List<User> subscribers) {
		super(name, description, owner, trackList, subscribers);
	}
	public PlaylistDAO(String name, String description, User owner) {
		super(name, description, owner);
	}

	public static Playlist searchById(Integer id) {
//		Devuelve la playlist cuya ID coincida con la ID itroducida
		Playlist queryResult = new Playlist();
		Connection con = Connect.getConnection();
		if (con != null) {
			try {
				PreparedStatement query = con.prepareStatement(getById);
				query.setInt(1, id);
				ResultSet rs = query.executeQuery();
				if (rs.next()) {
					queryResult = (new Playlist(rs.getInt("id"),
							rs.getString("name"),
							rs.getString("description"),
							new UserDAO()));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return queryResult;
	}
	
	public static List<Playlist> searchByOwner(Integer id){
//		Devuelve una lista con las playlists del usuario introducido
		List<Playlist> queryResult= new ArrayList<Playlist>();
		Connection con = Connect.getConnection();
		if (con != null) {
			try {
				PreparedStatement query = con.prepareStatement(getByOwner);
				query.setInt(1, id);
				ResultSet rs = query.executeQuery();
				while (rs.next()) {
					queryResult.add(new Playlist(rs.getInt("id"),
							rs.getString("name"),
							rs.getString("description"),
							new UserDAO(UserDAO.searchByID(id))));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return queryResult;
	}
	
	public static List<Playlist> searchByNotOwned(Integer id){
//		Devuelve las playlists que no pertenezcan al usuario
		List<Playlist> queryResult= new ArrayList<Playlist>();
		Connection con = Connect.getConnection();
		if (con != null) {
			try {
				PreparedStatement query = con.prepareStatement(getOtherPlaylists);
				query.setInt(1, id);
				ResultSet rs = query.executeQuery();
				while (rs.next()) {
					queryResult.add(new Playlist(rs.getInt("id"),
							rs.getString("name"),
							rs.getString("description"),
							new UserDAO(UserDAO.searchByID(rs.getInt("owner")))/*,
							new ArrayList<Track>(),
							new ArrayList<User>()*/));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return queryResult;
	}
	
	public static List<User> searchSubscribersById(Integer id){
//		Devuelve los suscriptore de una playlist
		List<User> queryResult= new ArrayList<User>();
		Connection con = Connect.getConnection();
		if (con != null) {
			try {
				PreparedStatement query = con.prepareStatement(getSubscribersById);
				query.setInt(1, id);
				ResultSet rs = query.executeQuery();
				while (rs.next()) {
					queryResult.add(new User(rs.getInt("id"),
							rs.getString("username"),
							rs.getString("email"),
							rs.getString("profilePicture")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return queryResult;
	}
	
	public static List<Track> searchTracksById(Integer id){
//		Devuelve las canciones de una playlist
		List<Track> queryResult= new ArrayList<Track>();
		Connection con = Connect.getConnection();
		if (con != null) {
			try {
				PreparedStatement query = con.prepareStatement(getTracksById);
				query.setInt(1, id);
				ResultSet rs = query.executeQuery();
				while (rs.next()) {
					queryResult.add(new Track(rs.getInt("id"),
							rs.getString("name"),
							rs.getInt("length"),
							new AlbumDAO(AlbumDAO.searchByID(rs.getInt("albumId"))),
							new GenreDAO(GenreDAO.searchByID(rs.getInt("genreId"))),
							rs.getInt("reproductions")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return queryResult;
	}
	
	public static List<Track> searchTracksNotInPlaylist(Integer id){
//		Devuelve todas las canciones que no formen parte de la playlist
		List<Track> queryResult= new ArrayList<Track>();
		Connection con = Connect.getConnection();
		if (con != null) {
			try {
				PreparedStatement query = con.prepareStatement(getTracksNotInPlaylist);
				query.setInt(1, id);
				ResultSet rs = query.executeQuery();
				while (rs.next()) {
					queryResult.add(new Track(rs.getInt("id"),
							rs.getString("name"),
							rs.getInt("length"),
							new AlbumDAO(AlbumDAO.searchByID(rs.getInt("albumId"))),
							new GenreDAO(GenreDAO.searchByID(rs.getInt("genreId"))),
							rs.getInt("reproductions")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return queryResult;
	}
	
	public Integer savePlaylist() {
//		Guarda la playlist en la base de datos o la actualiza
		Integer result = 0;
		Connection con = Connect.getConnection();
		if (con != null) {
			try {
				PreparedStatement query = con.prepareStatement(insertUpdate, Statement.RETURN_GENERATED_KEYS);
				try {
					query.setInt(1, this.id);
				} catch (NullPointerException e) {
					query.setNull(1, 0);
				}
				query.setString(2, this.name);
				query.setString(3, this.description);
				query.setInt(4, this.owner.getId());
				query.setString(5, this.name);
				query.setString(6, this.description);
				query.setInt(7, this.owner.getId());
				query.executeUpdate();
				ResultSet generatedKeys = query.getGeneratedKeys();
				if (generatedKeys.next()) {
					result = generatedKeys.getInt(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static Integer createPlaylistTrackRelation(Integer idPlaylist, Integer idTrack) {
//		Añade una canción a la playlist
		Integer result = 0;
		Connection con = Connect.getConnection();
		if (con != null) {
			try {
				PreparedStatement query = con.prepareStatement(insertPlaylistTrackRelation);
				query.setInt(1, idTrack);
				query.setInt(2, idPlaylist);
				result = query.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static Integer subscribe(Integer idPlaylist, Integer idUser) {
//		Suscribe a un usuario a una playlist
		Integer result = 0;
		Connection con = Connect.getConnection();
		if (con != null) {
			try {
				PreparedStatement query = con.prepareStatement(insertSubscription);
				query.setInt(1, idPlaylist);
				query.setInt(2, idUser);
				result = query.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public int removePlaylist() {
//		Elimina una playlist de la base de datos
		int result = 0;
		Connection con = Connect.getConnection();
		if (con != null) {
			try {
				PreparedStatement query = con.prepareStatement(delete);
				query.setInt(1, this.id);
				result = query.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			this.id = Integer.MIN_VALUE;
			this.name = "";
			this.description = "";
			this.owner = new User();
//			this.trackList.clear();
//			this.subscribers.clear();
		}
		return result;
	}
	
	public static Integer removePlaylistUserRelation(Integer idPlaylist, Integer idUser) {
//		Desuscribe a un usuario de una playlist
		Integer result = 0;
		Connection con = Connect.getConnection();
		if (con != null) {
			try {
				PreparedStatement query = con.prepareStatement(deleteSubscription);
				query.setInt(1, idPlaylist);
				query.setInt(2, idUser);
				result = query.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static Integer removePlaylistTrackRelation(Integer idPlaylist, Integer idTrack) {
//		Elimina una cacnión de una playlist
		Integer result = 0;
		Connection con = Connect.getConnection();
		if (con != null) {
			try {
				PreparedStatement query = con.prepareStatement(deleteTrackFromPlaylist);
				query.setInt(1, idPlaylist);
				query.setInt(2, idTrack);
				result = query.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
