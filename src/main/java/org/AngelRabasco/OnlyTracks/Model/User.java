package org.AngelRabasco.OnlyTracks.Model;

import java.util.List;

public class User {
	protected Integer id;
	protected String username;
	protected String email;
	protected String password;
	protected String profilePicture;
	protected List<Playlist> playlists;

	public User() {

	}
	public User(Integer id, String username, String email, String password, String profilePicture, List<Playlist> playlists) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.profilePicture = profilePicture;
		this.playlists = playlists;
	}
	public User(Integer id, String username, String email, String password, String profilePicture) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.profilePicture = profilePicture;
	}
	public User(String username, String email, String password, String profilePicture, List<Playlist> playlists) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.profilePicture = profilePicture;
		this.playlists = playlists;
	}
	public User(String username, String email, String password, String profilePicture) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.profilePicture = profilePicture;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getProfilePicture() {
		return profilePicture;
	}
	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	public List<Playlist> getPlaylists() {
		return playlists;
	}
	public void setPlaylists(List<Playlist> playlists) {
		this.playlists = playlists;
	}
}
