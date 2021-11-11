package org.AngelRabasco.OnlyTracks.Model;

import java.time.LocalDate;

public class Album {
	protected Integer id;
	protected String name;
	protected Author author;
	protected LocalDate releaseDate;
	protected String photo;
	protected Integer reproductions;

	public Album() {

	}
	public Album(Integer id, String name, Author author, LocalDate releaseDate, String photo, Integer reproductions) {
		this.id = id;
		this.name = name;
		this.author = author;
		this.releaseDate = releaseDate;
		this.photo = photo;
		this.reproductions = reproductions;
	}
	public Album(String name, Author author, LocalDate releaseDate, String photo, Integer reproductions) {
		this.name = name;
		this.author = author;
		this.releaseDate = releaseDate;
		this.photo = photo;
		this.reproductions = reproductions;
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

	public Author getAuthor() {
		return author;
	}
	public void setAuthor(Author author) {
		this.author = author;
	}

	public LocalDate getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public Integer getReproductions() {
		return reproductions;
	}
	public void setReproductions(Integer reproductions) {
		this.reproductions = reproductions;
	}
}
