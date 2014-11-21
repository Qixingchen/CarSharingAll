package com.hibernate.entity;

/**
 * CarInfo entity. @author MyEclipse Persistence Tools
 */

public class CarInfo implements java.io.Serializable {

	// Fields

	private Integer id;
	private String carNum;
	private String driverId;
	private String carBrand;
	private String carModel;
	private String carColor;
	private Integer carCapacity;
	private Integer drivedYears;
	private String carPohto;
	private String driveLicense;

	// Constructors

	/** default constructor */
	public CarInfo() {
	}

	/** minimal constructor */
	public CarInfo(String carNum, String driverId, String carBrand,
			String carModel, String carColor, Integer carCapacity) {
		this.carNum = carNum;
		this.driverId = driverId;
		this.carBrand = carBrand;
		this.carModel = carModel;
		this.carColor = carColor;
		this.carCapacity = carCapacity;
	}

	/** full constructor */
	public CarInfo(String carNum, String driverId, String carBrand,
			String carModel, String carColor, Integer carCapacity,
			Integer drivedYears, String carPohto, String driveLicense) {
		this.carNum = carNum;
		this.driverId = driverId;
		this.carBrand = carBrand;
		this.carModel = carModel;
		this.carColor = carColor;
		this.carCapacity = carCapacity;
		this.drivedYears = drivedYears;
		this.carPohto = carPohto;
		this.driveLicense = driveLicense;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCarNum() {
		return this.carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

	public String getDriverId() {
		return this.driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getCarBrand() {
		return this.carBrand;
	}

	public void setCarBrand(String carBrand) {
		this.carBrand = carBrand;
	}

	public String getCarModel() {
		return this.carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}

	public String getCarColor() {
		return this.carColor;
	}

	public void setCarColor(String carColor) {
		this.carColor = carColor;
	}

	public Integer getCarCapacity() {
		return this.carCapacity;
	}

	public void setCarCapacity(Integer carCapacity) {
		this.carCapacity = carCapacity;
	}

	public Integer getDrivedYears() {
		return this.drivedYears;
	}

	public void setDrivedYears(Integer drivedYears) {
		this.drivedYears = drivedYears;
	}

	public String getCarPohto() {
		return this.carPohto;
	}

	public void setCarPohto(String carPohto) {
		this.carPohto = carPohto;
	}

	public String getDriveLicense() {
		return this.driveLicense;
	}

	public void setDriveLicense(String driveLicense) {
		this.driveLicense = driveLicense;
	}

}