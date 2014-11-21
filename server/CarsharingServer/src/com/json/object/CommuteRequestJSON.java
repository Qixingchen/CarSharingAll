package com.json.object;

/**
 * CommuteRequest entity. @author MyEclipse Persistence Tools
 */

public class CommuteRequestJSON{

	// Fields

	private String id;
	private String userId;
	private String requestTime;
	private String startPlaceX;
	private String startPlaceY;
	private String startPlace;
	private String destinationX;
	private String destinationY;
	private String destination;
	private String startDate;
	private String endDate;
	private String startTime;
	private String endTime;
	private String weekRepeat;
	private String supplyCar;
	private String dealStatus;

	// Constructors

	/** default constructor */
	public CommuteRequestJSON() {
	}

	/** full constructor */
	public CommuteRequestJSON(String userId, String requestTime,
			String startPlaceX, String startPlaceY, String startPlace,
			String destinationX, String destinationY, String destination,
			String startDate, String endDate, String startTime, String endTime,
			String weekRepeat, String supplyCar, String dealStatus) {
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

	public String getRequestTime() {
		return this.requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public String getStartPlaceX() {
		return this.startPlaceX;
	}

	public void setStartPlaceX(String startPlaceX) {
		this.startPlaceX = startPlaceX;
	}

	public String getStartPlaceY() {
		return this.startPlaceY;
	}

	public void setStartPlaceY(String startPlaceY) {
		this.startPlaceY = startPlaceY;
	}

	public String getStartPlace() {
		return this.startPlace;
	}

	public void setStartPlace(String startPlace) {
		this.startPlace = startPlace;
	}

	public String getDestinationX() {
		return this.destinationX;
	}

	public void setDestinationX(String destinationX) {
		this.destinationX = destinationX;
	}

	public String getDestinationY() {
		return this.destinationY;
	}

	public void setDestinationY(String destinationY) {
		this.destinationY = destinationY;
	}

	public String getDestination() {
		return this.destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getStartDate() {
		return this.startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return this.endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartTime() {
		return this.startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return this.endTime;
	}

	public void setEndTime(String endTime) {
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

	public String getDealStatus() {
		return this.dealStatus;
	}

	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
	}

}