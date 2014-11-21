package com.hibernate.entity;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

/**
 * ShortwayRequest entity. @author MyEclipse Persistence Tools
 */

public class ShortwayRequest implements java.io.Serializable {

	// Fields

	private Integer id;
	private String userId;
	private Timestamp requestTime;
	private String userRole;
	private Float startPlaceX;
	private Float startPlaceY;
	private String startPlace;
	private Float destinationX;
	private Float destinationY;
	private String destination;
	private Date startDate;
	private Time startTime;
	private Time endTime;
	private Integer dealStatus;

	// Constructors

	/** default constructor */
	public ShortwayRequest() {
	}

	/** full constructor */
	public ShortwayRequest(String userId, Timestamp requestTime,
			String userRole, Float startPlaceX, Float startPlaceY,
			String startPlace, Float destinationX, Float destinationY,
			String destination, Date startDate, Time startTime, Time endTime,
			Integer dealStatus) {
		this.userId = userId;
		this.requestTime = requestTime;
		this.userRole = userRole;
		this.startPlaceX = startPlaceX;
		this.startPlaceY = startPlaceY;
		this.startPlace = startPlace;
		this.destinationX = destinationX;
		this.destinationY = destinationY;
		this.destination = destination;
		this.startDate = startDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.dealStatus = dealStatus;
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

	public Timestamp getRequestTime() {
		return this.requestTime;
	}

	public void setRequestTime(Timestamp requestTime) {
		this.requestTime = requestTime;
	}

	public String getUserRole() {
		return this.userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public Float getStartPlaceX() {
		return this.startPlaceX;
	}

	public void setStartPlaceX(Float startPlaceX) {
		this.startPlaceX = startPlaceX;
	}

	public Float getStartPlaceY() {
		return this.startPlaceY;
	}

	public void setStartPlaceY(Float startPlaceY) {
		this.startPlaceY = startPlaceY;
	}

	public String getStartPlace() {
		return this.startPlace;
	}

	public void setStartPlace(String startPlace) {
		this.startPlace = startPlace;
	}

	public Float getDestinationX() {
		return this.destinationX;
	}

	public void setDestinationX(Float destinationX) {
		this.destinationX = destinationX;
	}

	public Float getDestinationY() {
		return this.destinationY;
	}

	public void setDestinationY(Float destinationY) {
		this.destinationY = destinationY;
	}

	public String getDestination() {
		return this.destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Time getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public Integer getDealStatus() {
		return this.dealStatus;
	}

	public void setDealStatus(Integer dealStatus) {
		this.dealStatus = dealStatus;
	}

}