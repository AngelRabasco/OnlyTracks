package org.AngelRabasco.OnlyTracks.Model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.AngelRabasco.OnlyTracks.Model.Playlist;
import org.AngelRabasco.OnlyTracks.Model.Track;
import org.AngelRabasco.OnlyTracks.Model.User;
import org.AngelRabasco.OnlyTracks.Util.Connect;

public class PlaylistDAO extends Playlist {
	private final static String getById = "SELECT id,name,description,owner FROM playlists WHERE id=?";
	private final static String insertUpdate = "INSERT INTO playlists (id,name,description,owner) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE name=?,description=?,owner=?";
	private final static String delete="DELETE FROM playlists WHERE id=?";

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

	public static Playlist searchByID(Integer ID) {
//		Devuelve la playlist cuya ID coincida con la ID itroducida
		Playlist queryResult = new Playlist();
		Connection con = Connect.getConnection();
		if (con != null) {
			try {
				PreparedStatement query = con.prepareStatement(getById);
				query.setInt(1, ID);
				ResultSet rs = query.executeQuery();
				if (rs.next()) {
					queryResult = (new Playlist(rs.getInt("id"),
							rs.getString("name"),
							rs.getString("description"),
							new UserDAO(/*UserDAO.searchByID(rs.getInt("id"))*/)/*,
							new ArrayList<Track>(),
							new ArrayList<User>()*/));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return queryResult;
	}
	
	public Integer savePlaylist() {
		Integer result = 0;
		Connection con = Connect.getConnection();
		if (con != null) {
			try {
				PreparedStatement query = con.prepareStatement(insertUpdate);
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
				result = query.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public int removePlaylist() {
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
			this.trackList.clear();
			this.subscribers.clear();
		}
		return result;
	}
}