package com.hibernate.entity;

import java.sql.Timestamp;
import java.util.Date;

/**
 * LongwayPublish entity. @author MyEclipse Persistence Tools
 */

public class LongwayPublish implements java.io.Serializable {

	// Fields

	private Integer id;
	private String userId;
	private Timestamp publishTime;
	private String userRole;
	private Date startDate;
	private String startPlace;
	private String destination;
	private String noteInfo;

	// Constructors

	/** default constructor */
	public LongwayPublish() {
	}

	/** minimal constructor */
	public LongwayPublish(String userId, Timestamp publishTime,
			String userRole, Date startDate, String startPlace,
			String destination) {
		this.userId = userId;
		this.publishTime = publishTime;
		this.userRole = userRole;
		this.startDate = startDate;
		this.startPlace = startPlace;
		this.destination = destination;
	}

	/** full constructor */
	public LongwayPublish(String userId, Timestamp publishTime,
			String userRole, Date startDate, String startPlace,
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

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Timestamp getPublishTime() {
		return this.publishTime;
	}

	public void setPublishTime(Timestamp publishTime) {
		this.publishTime = publishTime;
	}

	public String getUserRole() {
		return this.userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
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