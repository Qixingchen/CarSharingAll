package com.json.object;

/**
 * UserInfo entity. @author MyEclipse Persistence Tools
 */

public class UserInfoJSON {

	// Fields

	private String userId;
	private String phoneNum;
	private String passWord;
	private String name;
	private String sex;
	private String age;
	private String photoAddress;
	private String identifyNum;
	private String creditScore;

	// Constructors

	/** default constructor */
	public UserInfoJSON() {
	}

	/** minimal constructor */
	public UserInfoJSON(String phoneNum, String passWord) {
		this.phoneNum = phoneNum;
		this.passWord = passWord;
	}

	/** full constructor */
	public UserInfoJSON(String phoneNum, String passWord, String name,
			String sex, String age, String photoAddress, String identifyNum,
			String creditScore) {
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

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
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

	public String getAge() {
		return this.age;
	}

	public void setAge(String age) {
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

	public String getCreditScore() {
		return this.creditScore;
	}

	public void setCreditScore(String creditScore) {
		this.creditScore = creditScore;
	}

}