package com.json.object;

/**
 * CarInfo entity. @author MyEclipse Persistence Tools
 */

public class CarInfoJSON {

	// Fields

	private String id;
	private String carNum;
	private String driverId;
	private String carBrand;
	private String carModel;
	private String carColor;
	private String carCapacity;
	private String drivedYears;
	private String carPohto;
	private String driveLicense;

	// Constructors

	/** default constructor */
	public CarInfoJSON() {
	}

	/** minimal constructor */
	public CarInfoJSON(String carNum, String driverId, String carBrand,
			String carModel, String carColor, String carCapacity) {
		this.carNum = carNum;
		this.driverId = driverId;
		this.carBrand = carBrand;
		this.carModel = carModel;
		this.carColor = carColor;
		this.carCapacity = carCapacity;
	}

	/** full constructor */
	public CarInfoJSON(String carNum, String driverId, String carBrand,
			String carModel, String carColor, String carCapacity,
			String drivedYears, String carPohto, String driveLicense) {
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

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
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

	public String getCarCapacity() {
		return this.carCapacity;
	}

	public void setCarCapacity(String carCapacity) {
		this.carCapacity = carCapacity;
	}

	public String getDrivedYears() {
		return this.drivedYears;
	}

	public void setDrivedYears(String drivedYears) {
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