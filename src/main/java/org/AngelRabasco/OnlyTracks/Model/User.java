package org.AngelRabasco.OnlyTracks.Model;

public class User {
	private Integer id;
	private String username;
	private String email;
	private String profilePicture;

	public User() {

	}

	public User(Integer id, String username, String email, String profilePicture) {
		this.id = id;
		this.username = username;
		this.email = email;
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

	public String getProfilePicture() {
		return profilePicture;
	}
	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}
}
