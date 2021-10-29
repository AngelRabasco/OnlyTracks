package org.AngelRabasco.OnlyTracks.Model;

import java.util.List;

public class Playlist {
	protected Integer id;
	protected String name;
	protected String description;
	protected User owner;
	protected List<Track> trackList;
	protected List<User> subscribers;

	public Playlist() {

	}

	public Playlist(Integer id, String name, String description, User owner, List<Track> trackList, List<User> subscribers) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.owner = owner;
		this.trackList = trackList;
		this.subscribers = subscribers;
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

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}

	public List<Track> getTrackList() {
		return trackList;
	}
	public void setTrackList(List<Track> trackList) {
		this.trackList = trackList;
	}

	public List<User> getSubscribers() {
		return subscribers;
	}
	public void setSubscribers(List<User> subscribers) {
		this.subscribers = subscribers;
	}
}
