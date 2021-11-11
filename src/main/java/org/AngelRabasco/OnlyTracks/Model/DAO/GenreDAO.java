package org.AngelRabasco.OnlyTracks.Model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.AngelRabasco.OnlyTracks.Model.Genre;
import org.AngelRabasco.OnlyTracks.Util.Connect;

public class GenreDAO extends Genre {
	private final static String getById = "SELECT id,name FROM genres WHERE id=?";
	private final static String insertUpdate = "INSERT INTO genres (id,name) VALUES (?,?) ON DUPLICATE KEY UPDATE name=?";
	private final static String delete="DELETE FROM genres WHERE id=?";
	
	public GenreDAO() {
		super();
	}
	public GenreDAO(Genre genre) {
		super(genre.getId(),
				genre.getName());
	}
	public GenreDAO(Integer id, String name) {
		super(id, name);
	}
	public GenreDAO(String name) {
		super(name);
	}

	public static Genre searchByID(Integer ID) {
//		Devuelve el genero cuya ID coincida con la ID itroducida
		Genre queryResult = new Genre();
		Connection con = Connect.getConnection();
		if (con != null) {
			try {
				PreparedStatement query = con.prepareStatement(getById);
				query.setInt(1, ID);
				ResultSet rs = query.executeQuery();
				if (rs.next()) {
					queryResult = (new Genre(rs.getInt("id"),
							rs.getString("name")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return queryResult;
	}

	public Integer saveGenre() {
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
				query.setString(3, this.name);
				result = query.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public int removeGenre() {
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
		}
		return result;
	}
}
