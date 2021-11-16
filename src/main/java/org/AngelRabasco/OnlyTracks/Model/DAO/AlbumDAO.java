package org.AngelRabasco.OnlyTracks.Model.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.AngelRabasco.OnlyTracks.Model.Album;
import org.AngelRabasco.OnlyTracks.Model.Author;
import org.AngelRabasco.OnlyTracks.Util.Connect;

public class AlbumDAO extends Album {
	private final static String getById = "SELECT id,name,authorId,releaseDate,photo,reproductions FROM albums WHERE id=?";
	private final static String insertUpdate = "INSERT INTO albums (id,name,authorId,releaseDate,photo,reproductions) VALUES (?,?,?,?,?,?) ON DUPLICATE KEY UPDATE name=?,authorId=?,releaseDate=?,photo=?,reproductions=?";
	private final static String delete="DELETE FROM albums WHERE id=?";
	
	public AlbumDAO() {
		super();
	}
	public AlbumDAO(Album album) {
		super(album.getId(),
				album.getName(),
				album.getAuthor(),
				album.getReleaseDate(),
				album.getPhoto(),
				album.getReproductions());
	}
	public AlbumDAO(Integer id, String name, Author author, LocalDate releaseDate, String photo, Integer reproductions) {
		super(id, name, author, releaseDate, photo, reproductions);
	}
	public AlbumDAO(String name, Author author, LocalDate releaseDate, String photo, Integer reproductions) {
		super(name, author, releaseDate, photo, reproductions);
	}
	
	public static Album searchByID(Integer ID) {
//		Devuelve el usuario cuya ID coincida con la ID itroducida
		Album queryResult = new Album();
		Connection con = Connect.getConnection();
		if (con != null) {
			try {
				PreparedStatement query = con.prepareStatement(getById);
				query.setInt(1, ID);
				ResultSet rs = query.executeQuery();
				if (rs.next()) {
					queryResult = (new Album(rs.getInt("id"),
							rs.getString("name"),
							new AuthorDAO(/*AuthorDAO.searchByID(rs.getInt("id"))*/),
							rs.getDate("releaseDate").toLocalDate(),
							rs.getString("photo"),
							rs.getInt("reproductions")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return queryResult;
	}

	public Integer saveAlbum() {
//		Guarda el album en la base de datos, y en caso de estar repetida actualiza los campos
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
				query.setInt(3, this.author.getId());
				query.setDate(4, Date.valueOf(this.releaseDate));
				query.setString(5, this.photo);
				query.setInt(6, this.reproductions);
				query.setString(7, this.name);
				query.setInt(8, this.author.getId());
				query.setDate(9, Date.valueOf(this.releaseDate));
				query.setString(10, this.photo);
				query.setInt(11, this.reproductions);
				result = query.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public int removeAlbum() {
//		Eliminal el album de la base de datos
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
			this.author = new Author();
			this.releaseDate = LocalDate.MIN;
			this.photo = "";
			this.reproductions = Integer.MIN_VALUE;
		}
		return result;
	}
}
