package org.AngelRabasco.OnlyTracks.Model;

import java.util.List;

public class Track {
	protected Integer id;
	protected String name;
	protected Integer length;
	protected Album album;
	protected Genre genre;
	protected Integer reproductions;
	protected List<Playlist> playlistList;

	public Track() {

	}

	public Track(Integer id, String name, Integer length, Album album, Genre genre, Integer reproductions, List<Playlist> playlistList) {
		this.id = id;
		this.name = name;
		this.length = length;
		this.album = album;
		this.genre = genre;
		this.reproductions = reproductions;
		this.playlistList = playlistList;
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

	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}

	public Album getAlbum() {
		return album;
	}
	public void setAlbum(Album album) {
		this.album = album;
	}

	public Genre getGenre() {
		return genre;
	}
	public void setGenre(Genre genre) {
		this.genre = genre;
	}

	public Integer getReproductions() {
		return reproductions;
	}
	public void setReproductions(Integer reproductions) {
		this.reproductions = reproductions;
	}

	public List<Playlist> getPlaylistList() {
		return playlistList;
	}
	public void setPlaylistList(List<Playlist> playlistList) {
		this.playlistList = playlistList;
	}
}
