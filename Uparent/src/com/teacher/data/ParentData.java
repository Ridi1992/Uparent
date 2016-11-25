package com.teacher.data;

import java.io.Serializable;

public class ParentData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1059377924437450655L;
	private String subParentLoingId;
	private String pId;
	private String subParentName;
	private String studentId;
	private String photo;
	private String idCard;
	private String mobilePhone;
	private Boolean isReceive;
	private int relationship;
	
	
	/**
	 * @return the idCard
	 */
	public String getIdCard() {
		return idCard;
	}
	/**
	 * @param idCard the idCard to set
	 */
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	/**
	 * @return the mobilePhone
	 */
	public String getMobilePhone() {
		return mobilePhone;
	}
	/**
	 * @param mobilePhone the mobilePhone to set
	 */
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	/**
	 * @return the subParentLoingId
	 */
	public String getSubParentLoingId() {
		return subParentLoingId;
	}
	/**
	 * @param subParentLoingId the subParentLoingId to set
	 */
	public void setSubParentLoingId(String subParentLoingId) {
		this.subParentLoingId = subParentLoingId;
	}
	/**
	 * @return the pId
	 */
	public String getpId() {
		return pId;
	}
	/**
	 * @param pId the pId to set
	 */
	public void setpId(String pId) {
		this.pId = pId;
	}
	/**
	 * @return the subParentName
	 */
	public String getSubParentName() {
		return subParentName;
	}
	/**
	 * @param subParentName the subParentName to set
	 */
	public void setSubParentName(String subParentName) {
		this.subParentName = subParentName;
	}
	/**
	 * @return the studentId
	 */
	public String getStudentId() {
		return studentId;
	}
	/**
	 * @param studentId the studentId to set
	 */
	public void setStudentId(String studentId) {
		this.studentId = studentId;
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


	/**
	 * @return the isReceive
	 */
	public Boolean getIsReceive() {
		return isReceive;
	}
	/**
	 * @param isReceive the isReceive to set
	 */
	public void setIsReceive(Boolean isReceive) {
		this.isReceive = isReceive;
	}
	/**
	 * @return the relationship
	 */
	public int getRelationship() {
		return relationship;
	}
	/**
	 * @param relationship the relationship to set
	 */
	public void setRelationship(int relationship) {
		this.relationship = relationship;
	}
	
}
