package com.hibernate.entity;

/**
 * CartakeDeal entity. @author MyEclipse Persistence Tools
 */

public class CartakeDeal implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer dealId;
	private String driverId;
	private String passengerId;
	private Integer passengerOrder;
	private Double posionX;
	private Double posionY;
	private String posion;

	// Constructors

	/** default constructor */
	public CartakeDeal() {
	}

	/** full constructor */
	public CartakeDeal(Integer dealId, String driverId, String passengerId,
			Integer passengerOrder, Double posionX, Double posionY,
			String posion) {
		this.dealId = dealId;
		this.driverId = driverId;
		this.passengerId = passengerId;
		this.passengerOrder = passengerOrder;
		this.posionX = posionX;
		this.posionY = posionY;
		this.posion = posion;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDealId() {
		return this.dealId;
	}

	public void setDealId(Integer dealId) {
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

	public Integer getPassengerOrder() {
		return this.passengerOrder;
	}

	public void setPassengerOrder(Integer passengerOrder) {
		this.passengerOrder = passengerOrder;
	}

	public Double getPosionX() {
		return this.posionX;
	}

	public void setPosionX(Double posionX) {
		this.posionX = posionX;
	}

	public Double getPosionY() {
		return this.posionY;
	}

	public void setPosionY(Double posionY) {
		this.posionY = posionY;
	}

	public String getPosion() {
		return this.posion;
	}

	public void setPosion(String posion) {
		this.posion = posion;
	}

}