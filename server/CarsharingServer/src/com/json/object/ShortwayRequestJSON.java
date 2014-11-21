package com.json.object;

/**
 * ShortwayRequest entity. @author MyEclipse Persistence Tools
 */

public class ShortwayRequestJSON {

	// Fields

	private String id;
	private String userId;
	private String requestTime;
	private String userRole;
	private String startPlaceX;
	private String startPlaceY;
	private String startPlace;
	private String destinationX;
	private String destinationY;
	private String destination;
	private String startDate;
	private String startTime;
	private String endTime;
	private String dealStatus;

	// Constructors

	/** default constructor */
	public ShortwayRequestJSON() {
	}

	/** full constructor */
	public ShortwayRequestJSON(String userId, String requestTime,
			String userRole, String startPlaceX, String startPlaceY,
			String startPlace, String destinationX, String destinationY,
			String destination, String startDate, String startTime,
			String endTime, String dealStatus) {
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

	public String getUserRole() {
		return this.userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
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

	public String getDealStatus() {
		return this.dealStatus;
	}

	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
	}
}