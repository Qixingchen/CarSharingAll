package com.json.object;

/**
 * LongwayPublish entity. @author MyEclipse Persistence Tools
 */

public class LongwayPublishJSON{

	// Fields

	private String id;
	private String userId;
	private String publishTime;
	private String userRole;
	private String startDate;
	private String startPlace;
	private String destination;
	private String noteInfo;

	// Constructors

	/** default constructor */
	public LongwayPublishJSON() {
	}

	/** minimal constructor */
	public LongwayPublishJSON(String userId, String publishTime,
			String userRole, String startDate, String startPlace,
			String destination) {
		this.userId = userId;
		this.publishTime = publishTime;
		this.userRole = userRole;
		this.startDate = startDate;
		this.startPlace = startPlace;
		this.destination = destination;
	}

	/** full constructor */
	public LongwayPublishJSON(String userId, String publishTime,
			String userRole, String startDate, String startPlace,
			String destination, String noteInfo) {
		this.userId = userId;
		this.publishTime = publishTime;
		this.userRole = userRole;
		this.startDate = startDate;
		this.startPlace = startPlace;
		this.destination = destination;
		this.noteInfo = noteInfo;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPublishTime() {
		return this.publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getUserRole() {
		return this.userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getStartDate() {
		return this.startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartPlace() {
		return this.startPlace;
	}

	public void setStartPlace(String startPlace) {
		this.startPlace = startPlace;
	}

	public String getDestination() {
		return this.destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getNoteInfo() {
		return this.noteInfo;
	}

	public void setNoteInfo(String noteInfo) {
		this.noteInfo = noteInfo;
	}

}