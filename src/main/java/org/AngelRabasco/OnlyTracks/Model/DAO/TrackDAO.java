package org.AngelRabasco.OnlyTracks.Model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.AngelRabasco.OnlyTracks.Model.Album;
import org.AngelRabasco.OnlyTracks.Model.Genre;
import org.AngelRabasco.OnlyTracks.Model.Playlist;
import org.AngelRabasco.OnlyTracks.Model.Track;
import org.AngelRabasco.OnlyTracks.Model.User;
import org.AngelRabasco.OnlyTracks.Util.Connect;

public class TrackDAO extends Track {
	private final static String getById = "SELECT id,name,length,albumId,genreId,reproductions FROM tracks WHERE id=?";
	private final static String getAllTracks = "SELECT id,name,length,albumId,genreId,reproductions FROM tracks";
	private final static String getPlaylistsById = "SELECT playlists.id,playlists.name,playlists.description,playlists.owner FROM playlists JOIN tracks_playlists ON tracks_playlists.idPlaylist=playlists.id WHERE tracks_playlists.idTrack=?";
	private final static String increaseReproductions = "UPDATE tracks SET reproductions=reproductions+1 WHERE id=?";
	private final static String insertUpdate = "INSERT INTO tracks (id,name,length,albumId,genreId,reproductions) VALUES (?,?,?,?,?,?) ON DUPLICATE KEY UPDATE name=?,length=?,albumId=?,genreId=?,reproductions=?";
	private final static String delete = "DELETE FROM tracks WHERE id=?";

	public TrackDAO() {
		super();
	}

	public TrackDAO(Integer id, String name, Integer length, Album album, Genre genre, Integer reproductions,
			List<Playlist> playlistList) {
		super(id, name, length, album, genre, reproductions, playlistList);
	}

	public TrackDAO(Integer id, String name, Integer length, Album album, Genre genre, Integer reproductions) {
		super(id, name, length, album, genre, reproductions);
	}

	public TrackDAO(String name, Integer length, Album album, Genre genre, Integer reproductions,
			List<Playlist> playlistList) {
		super(name, length, album, genre, reproductions, playlistList);
	}

	public TrackDAO(String name, Integer length, Album album, Genre genre, Integer reproductions) {
		super(name, length, album, genre, reproductions);
	}

	public static Track searchById(Integer id) {
//		Devuelve el autor cuya ID coincida con la ID itroducida
		Track queryResult = new Track();
		Connection con = Connect.getConnection();
		if (con != null) {
			try {
				PreparedStatement query = con.prepareStatement(getById);
				query.setInt(1, id);
				ResultSet rs = query.executeQuery();
				if (rs.next()) {
					queryResult = (new Track(rs.getInt("id"), rs.getString("name"), rs.getInt("length"),
							new AlbumDAO(/* AlbumDAO.searchByID(rs.getInt("id")) */),
							new GenreDAO(/* GenreDAO.searchByID(rs.getInt("id")) */),
							rs.getInt("reproductions")/*
																 * , new ArrayList<Playlist>()
																 */));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return queryResult;
	}

	public static List<Track> searchAllTracks() {
//		Devuelve todas las canciones
		List<Track> queryResult = new ArrayList<Track>();
		Connection con = Connect.getConnection();
		if (con != null) {
			try {
				ResultSet rs = con.prepareStatement(getAllTracks).executeQuery();
				while (rs.next()) {
					queryResult.add(new Track(rs.getInt("id"), rs.getString("name"), rs.getInt("length"),
							new AlbumDAO(AlbumDAO.searchByID(rs.getInt("id"))),
							new GenreDAO(GenreDAO.searchByID(rs.getInt("id"))),
							rs.getInt("reproductions")/*
																 * , new ArrayList<Playlist>()
																 */));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return queryResult;
	}

	public static List<Playlist> searchPlaylistsById(Integer id) {
//		Devulve las playlists en las que está una canción
		List<Playlist> queryResult = new ArrayList<Playlist>();
		Connection con = Connect.getConnection();
		if (con != null) {
			try {
				PreparedStatement query = con.prepareStatement(getPlaylistsById);
				query.setInt(1, id);
				ResultSet rs = query.executeQuery();
				while (rs.next()) {
					queryResult
							.add(new Playlist(rs.getInt("id"), rs.getString("name"), rs.getString("description"), new User()));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return queryResult;
	}

	public static Integer increaseReproductions(Integer id) {
//		Aumenta en 1 las reproducciones de una canción
		Integer result = 0;
		Connection con = Connect.getConnection();
		if (con != null) {
			try {
				PreparedStatement query = con.prepareStatement(increaseReproductions);
				query.setInt(1, id);
				result = query.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public Integer saveTrack() {
//		Almacena una cancíon en la base de datos o actualiza su información
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
				query.setInt(3, this.length);
				query.setInt(4, this.album.getId());
				query.setInt(5, this.genre.getId());
				query.setInt(6, this.reproductions);
				query.setString(7, this.name);
				query.setInt(8, this.length);
				query.setInt(9, this.album.getId());
				query.setInt(10, this.genre.getId());
				query.setInt(11, this.reproductions);
				result = query.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public int removeTrack() {
//		Elimina una canción de la base de datos
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
			this.length = Integer.MIN_VALUE;
			this.album = new Album();
			this.genre = new Genre();
			this.reproductions = Integer.MIN_VALUE;
			this.playlistList.clear();
		}
		return result;
	}
}
