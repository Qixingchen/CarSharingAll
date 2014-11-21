package com.hibernate.entity;

import java.sql.Timestamp;

/**
 * CarshareDeal entity. @author MyEclipse Persistence Tools
 */

public class CarshareDeal implements java.io.Serializable {

	// Fields

	private Integer dealId;
	private Timestamp dealTime;
	private String sharingType;
	private Double startPlaceX;
	private Double startPlaceY;
	private String startPlace;
	private Double destinationX;
	private Double destinationY;
	private String destination;
	private Integer finshStatus;

	// Constructors

	/** default constructor */
	public CarshareDeal() {
	}

	/** full constructor */
	public CarshareDeal(Timestamp dealTime, String sharingType,
			Double startPlaceX, Double startPlaceY, String startPlace,
			Double destinationX, Double destinationY, String destination,
			Integer finshStatus) {
		this.dealTime = dealTime;
		this.sharingType = sharingType;
		this.startPlaceX = startPlaceX;
		this.startPlaceY = startPlaceY;
		this.startPlace = startPlace;
		this.destinationX = destinationX;
		this.destinationY = destinationY;
		this.destination = destination;
		this.finshStatus = finshStatus;
	}

	// Property accessors

	public Integer getDealId() {
		return this.dealId;
	}

	public void setDealId(Integer dealId) {
		this.dealId = dealId;
	}

	public Timestamp getDealTime() {
		return this.dealTime;
	}

	public void setDealTime(Timestamp dealTime) {
		this.dealTime = dealTime;
	}

	public String getSharingType() {
		return this.sharingType;
	}

	public void setSharingType(String sharingType) {
		this.sharingType = sharingType;
	}

	public Double getStartPlaceX() {
		return this.startPlaceX;
	}

	public void setStartPlaceX(Double startPlaceX) {
		this.startPlaceX = startPlaceX;
	}

	public Double getStartPlaceY() {
		return this.startPlaceY;
	}

	public void setStartPlaceY(Double startPlaceY) {
		this.startPlaceY = startPlaceY;
	}

	public String getStartPlace() {
		return this.startPlace;
	}

	public void setStartPlace(String startPlace) {
		this.startPlace = startPlace;
	}

	public Double getDestinationX() {
		return this.destinationX;
	}

	public void setDestinationX(Double destinationX) {
		this.destinationX = destinationX;
	}

	public Double getDestinationY() {
		return this.destinationY;
	}

	public void setDestinationY(Double destinationY) {
		this.destinationY = destinationY;
	}

	public String getDestination() {
		return this.destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Integer getFinshStatus() {
		return this.finshStatus;
	}

	public void setFinshStatus(Integer finshStatus) {
		this.finshStatus = finshStatus;
	}

}