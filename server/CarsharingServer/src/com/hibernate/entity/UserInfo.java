package com.hibernate.entity;

/**
 * UserInfo entity. @author MyEclipse Persistence Tools
 */

public class UserInfo implements java.io.Serializable {

	// Fields

	private Integer userId;
	private String phoneNum;
	private String passWord;
	private String name;
	private String sex;
	private Integer age;
	private String photoAddress;
	private String identifyNum;
	private Integer creditScore;

	// Constructors

	/** default constructor */
	public UserInfo() {
	}

	/** minimal constructor */
	public UserInfo(String phoneNum, String passWord) {
		this.phoneNum = phoneNum;
		this.passWord = passWord;
	}

	/** full constructor */
	public UserInfo(String phoneNum, String passWord, String name, String sex,
			Integer age, String photoAddress, String identifyNum,
			Integer creditScore) {
		this.phoneNum = phoneNum;
		this.passWord = passWord;
		this.name = name;
		this.sex = sex;
		this.age = age;
		this.photoAddress = photoAddress;
		this.identifyNum = identifyNum;
		this.creditScore = creditScore;
	}

	// Property accessors

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getPhoneNum() {
		return this.phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getPassWord() {
		return this.passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Integer getAge() {
		return this.age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getPhotoAddress() {
		return this.photoAddress;
	}

	public void setPhotoAddress(String photoAddress) {
		this.photoAddress = photoAddress;
	}

	public String getIdentifyNum() {
		return this.identifyNum;
	}

	public void setIdentifyNum(String identifyNum) {
		this.identifyNum = identifyNum;
	}

	public Integer getCreditScore() {
		return this.creditScore;
	}

	public void setCreditScore(Integer creditScore) {
		this.creditScore = creditScore;
	}

}