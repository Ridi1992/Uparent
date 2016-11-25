package com.teacher.data;

import android.R.bool;

public class StudentData_feedback {

	private String id;//学生id
	private Boolean hasFeedback;//是否已经反馈
	private String name;//学生姓名
	private String gender;//学生性别
	private String photo;//学生照片
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the hasFeedback
	 */
	public Boolean getHasFeedback() {
		return hasFeedback;
	}
	/**
	 * @param hasFeedback the hasFeedback to set
	 */
	public void setHasFeedback(Boolean hasFeedback) {
		this.hasFeedback = hasFeedback;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * @return the photo
	 */
	public String getPhoto() {
		return photo;
	}
	/**
	 * @param photo the photo to set
	 */
	public void setPhoto(String photo) {
		this.photo = photo;
	}
    
}
