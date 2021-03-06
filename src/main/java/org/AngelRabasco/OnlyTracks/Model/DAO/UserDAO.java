package org.AngelRabasco.OnlyTracks.Model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.AngelRabasco.OnlyTracks.Model.Playlist;
import org.AngelRabasco.OnlyTracks.Model.User;
import org.AngelRabasco.OnlyTracks.Util.Connect;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserDAO extends User {
	private final static String getById = "SELECT * FROM users WHERE id=?";
	private final static String getSubscriptionsById = "SELECT playlists.id,playlists.name,playlists.description,playlists.owner FROM playlists JOIN users_playlists ON users_playlists.idPlaylist=playlists.id WHERE users_playlists.idUser=?";
	private final static String getLoginCredentials="SELECT id,username,email,password,profilePicture FROM users WHERE username LIKE ?";
	private final static String insertUpdate = "INSERT INTO users (id,username,email,password,profilePicture) VALUES (?,?,?,?,?) ON DUPLICATE KEY UPDATE username=?,email=?,password=?";
	private final static String delete = "DELETE FROM users WHERE id=?";
	
	public UserDAO() {
		super();
	}
	public UserDAO(User user) {
		super(user.getId(),
				user.getUsername(),
				user.getEmail(),
				user.getPassword(),
				user.getProfilePicture(),
				user.getPlaylists());
	}
	public UserDAO(Integer id, String username, String email, String password, String profilePicture, List<Playlist> playlists) {
		super(id, username, email, password, profilePicture, playlists);
	}
	public UserDAO(Integer id, String username, String email, String password, String profilePicture) {
		super(id, username, email, password, profilePicture);
	}
	public UserDAO(Integer id, String username, String email, String password) {
		super(id, username, email, password);
	}
	public UserDAO(String username, String email, String password, String profilePicture, List<Playlist> playlists) {
		super(username, email, password, profilePicture, playlists);
	}
	public UserDAO(String username, String email, String password, String profilePicture) {
		super(username, email, password, profilePicture);
	}
	public UserDAO(String username, String email, String password) {
		super(username, email, password);
	}
	public UserDAO(String username, String password) {
		super(username, password);
	}

	public static User searchByID(Integer ID) {
//		Devuelve el usuario cuya ID coincida con la ID itroducida
		User queryResult = new User();
		Connection con = Connect.getConnection();
		if (con != null) {
			try {
				PreparedStatement query = con.prepareStatement(getById);
				query.setInt(1, ID);
				ResultSet rs = query.executeQuery();
				if (rs.next()) {
					queryResult = (new User(rs.getInt("id"),
							rs.getString("username"),
							rs.getString("email"),
							null,
							rs.getString("profilePicture")/*,
							new ArrayList<Playlist>()*/));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return queryResult;
	}
	
	public Boolean logIn() {
//		Comprueba que haya un usuario con el mismo nombre y contrase??a
		Boolean result = false;
		Connection con = Connect.getConnection();
		if (con != null) {
			try {
				PreparedStatement query = con.prepareStatement(getLoginCredentials);
				query.setString(1, this.username);
				ResultSet rs = query.executeQuery();
				if (rs.next()) {
					if (new BCryptPasswordEncoder().matches(this.password, rs.getString("password"))) {
						this.id = rs.getInt("id");
						this.username = rs.getString("username");
						this.email = rs.getString("email");
						this.password = null;
						this.profilePicture = rs.getString("profilePicture");
						this.playlists = searchSubscriptionsById(this.id);
						result = true;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public Boolean signUp() {
//		Comprueba que no exista un usuario y lo registra
		Boolean result = false;
		Connection con = Connect.getConnection();
		if (con != null) {
			try {
				PreparedStatement query = con.prepareStatement(getLoginCredentials);
				query.setString(1, this.username);
				ResultSet rs = query.executeQuery();
				if (!rs.next()) {
					this.id = new UserDAO(this.username, this.email, new BCryptPasswordEncoder().encode(this.password)).saveUser();
					result = true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static List<Playlist> searchSubscriptionsById(Integer id){
//		Devuelve las playlists a las que est?? suscrito el usuario
		List<Playlist> queryResult= new ArrayList<Playlist>();
		Connection con = Connect.getConnection();
		if (con != null) {
			try {
				PreparedStatement query = con.prepareStatement(getSubscriptionsById);
				query.setInt(1, id);
				ResultSet rs = query.executeQuery();
				while (rs.next()) {
					queryResult.add(new Playlist(rs.getInt("id"),
							rs.getString("name"),
							rs.getString("description"),
							UserDAO.searchByID(rs.getInt("owner"))));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return queryResult;
	}
	
	public Integer saveUser() {
//		Guarda el usuario o actualiza su informaci??n
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
				query.setString(2, this.username);
				query.setString(3, this.email);
				query.setString(4, this.password);
				query.setString(5, this.profilePicture);
				query.setString(6, this.username);
				query.setString(7, this.email);
				query.setString(8, this.password);
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
	
	public Integer removeUser() {
//		Elimina al usuario de la base de datos
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
			this.username = "";
			this.email = "";
			this.password = "";
			this.profilePicture = "";
			this.playlists.clear();
			;
		}
		return result;
	}
}
