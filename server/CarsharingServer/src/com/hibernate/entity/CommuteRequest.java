package com.hibernate.entity;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

/**
 * CommuteRequest entity. @author MyEclipse Persistence Tools
 */

public class CommuteRequest implements java.io.Serializable {

	// Fields

	private Integer id;
	private String userId;
	private Timestamp requestTime;
	private Float startPlaceX;
	private Float startPlaceY;
	private String startPlace;
	private Float destinationX;
	private Float destinationY;
	private String destination;
	private Date startDate;
	private Date endDate;
	private Time startTime;
	private Time endTime;
	private String weekRepeat;
	private String supplyCar;
	private Integer dealStatus;

	// Constructors

	/** default constructor */
	public CommuteRequest() {
	}

	/** full constructor */
	public CommuteRequest(String userId, Timestamp requestTime,
			Float startPlaceX, Float startPlaceY, String startPlace,
			Float destinationX, Float destinationY, String destination,
			Date startDate, Date endDate, Time startTime, Time endTime,
			String weekRepeat, String supplyCar, Integer dealStatus) {
		this.userId = userId;
		this.requestTime = requestTime;
		this.startPlaceX = startPlaceX;
		this.startPlaceY = startPlaceY;
		this.startPlace = startPlace;
		this.destinationX = destinationX;
		this.destinationY = destinationY;
		this.destination = destination;
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.weekRepeat = weekRepeat;
		this.supplyCar = supplyCar;
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

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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

	public String getWeekRepeat() {
		return this.weekRepeat;
	}

	public void setWeekRepeat(String weekRepeat) {
		this.weekRepeat = weekRepeat;
	}

	public String getSupplyCar() {
		return this.supplyCar;
	}

	public void setSupplyCar(String supplyCar) {
		this.supplyCar = supplyCar;
	}

	public Integer getDealStatus() {
		return this.dealStatus;
	}

	public void setDealStatus(Integer dealStatus) {
		this.dealStatus = dealStatus;
	}

}