package org.AngelRabasco.OnlyTracks.Model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.AngelRabasco.OnlyTracks.Model.Author;
import org.AngelRabasco.OnlyTracks.Util.Connect;

public class AuthorDAO extends Author {
	private final static String getById = "SELECT id,name,nationality,photo FROM authors WHERE id=?";
	private final static String insertUpdate = "INSERT INTO authors (id,name,nationality,photo) VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE name=?,nationality=?,photo=?";
	private final static String delete="DELETE FROM authors WHERE id=?";
	
	public AuthorDAO() {
		super();
	}
	public AuthorDAO(Author author) {
		super(author.getId(),
				author.getName(),
				author.getNationality(),
				author.getPhoto());
	}
	public AuthorDAO(Integer id, String name, String nationality, String photo) {
		super(id, name, nationality, photo);
	}
	public AuthorDAO(String name, String nationality, String photo) {
		super(name, nationality, photo);
	}

	public static Author searchByID(Integer ID) {
//		Devuelve el autor cuya ID coincida con la ID itroducida
		Author queryResult = new Author();
		Connection con = Connect.getConnection();
		if (con != null) {
			try {
				PreparedStatement query = con.prepareStatement(getById);
				query.setInt(1, ID);
				ResultSet rs = query.executeQuery();
				if (rs.next()) {
					queryResult = (new Author(rs.getInt("id"),
							rs.getString("name"),
							rs.getString("nationality"),
							rs.getString("photo")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return queryResult;
	}
	
	public Integer saveAuthor() {
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
				query.setString(3, this.nationality);
				query.setString(4, this.photo);
				query.setString(5, this.name);
				query.setString(6, this.nationality);
				query.setString(7, this.photo);
				result = query.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public int removeAuthor() {
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
			this.nationality = "";
			this.photo = "";
		}
		return result;
	}
}
