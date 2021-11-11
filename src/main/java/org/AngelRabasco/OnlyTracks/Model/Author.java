package org.AngelRabasco.OnlyTracks.Model;

public class Author {
	protected Integer id;
	protected String name;
	protected String nationality;
	protected String photo;

	public Author() {

	}
	public Author(Integer id, String name, String nationality, String photo) {
		this.id = id;
		this.name = name;
		this.nationality = nationality;
		this.photo = photo;
	}
	public Author(String name, String nationality, String photo) {
		this.name = name;
		this.nationality = nationality;
		this.photo = photo;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
}
