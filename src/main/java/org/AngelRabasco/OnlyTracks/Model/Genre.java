package org.AngelRabasco.OnlyTracks.Model;

public class Genre {
	protected Integer id;
	protected String name;

	public Genre() {

	}
	public Genre(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
	public Genre(String name) {
		this.name = name;
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
}
