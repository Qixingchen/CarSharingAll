package com.json.object;

/**
 * CartakeDeal entity. @author MyEclipse Persistence Tools
 */

public class CartakeDealJSON {

	// Fields

	private String id;
	private String dealId;
	private String driverId;
	private String passengerId;
	private String passengerOrder;
	private String posionX;
	private String posionY;

	// Constructors

	/** default constructor */
	public CartakeDealJSON() {
	}

	/** full constructor */
	public CartakeDealJSON(String dealId, String driverId, String passengerId,
			String passengerOrder, String posionX, String posionY) {
		this.dealId = dealId;
		this.driverId = driverId;
		this.passengerId = passengerId;
		this.passengerOrder = passengerOrder;
		this.posionX = posionX;
		this.posionY = posionY;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDealId() {
		return this.dealId;
	}

	public void setDealId(String dealId) {
		this.dealId = dealId;
	}

	public String getDriverId() {
		return this.driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getPassengerId() {
		return this.passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public String getPassengerOrder() {
		return this.passengerOrder;
	}

	public void setPassengerOrder(String passengerOrder) {
		this.passengerOrder = passengerOrder;
	}

	public String getPosionX() {
		return this.posionX;
	}

	public void setPosionX(String posionX) {
		this.posionX = posionX;
	}

	public String getPosionY() {
		return this.posionY;
	}

	public void setPosionY(String posionY) {
		this.posionY = posionY;
	}
}