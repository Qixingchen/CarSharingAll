package com.json.object;

/**
 * CarshareDeal entity. @author MyEclipse Persistence Tools
 */

public class CarshareDealJSON {

	// Fields

	private String dealId;
	private String dealTime;
	private String sharingType;
	private String startPlaceX;
	private String startPlaceY;
	private String destinationX;
	private String destinationY;
	private String finshStatus;

	// Constructors

	/** default constructor */
	public CarshareDealJSON() {
	}

	/** full constructor */
	public CarshareDealJSON(String dealTime, String sharingType,
			String startPlaceX, String startPlaceY, String destinationX,
			String destinationY, String finshStatus) {
		this.dealTime = dealTime;
		this.sharingType = sharingType;
		this.startPlaceX = startPlaceX;
		this.startPlaceY = startPlaceY;
		this.destinationX = destinationX;
		this.destinationY = destinationY;
		this.finshStatus = finshStatus;
	}

	// Property accessors

	public String getDealId() {
		return this.dealId;
	}

	public void setDealId(String dealId) {
		this.dealId = dealId;
	}

	public String getDealTime() {
		return this.dealTime;
	}

	public void setDealTime(String dealTime) {
		this.dealTime = dealTime;
	}

	public String getSharingType() {
		return this.sharingType;
	}

	public void setSharingType(String sharingType) {
		this.sharingType = sharingType;
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

	public String getFinshStatus() {
		return this.finshStatus;
	}

	public void setFinshStatus(String finshStatus) {
		this.finshStatus = finshStatus;
	}

}