package com.teacher.data;

import java.io.Serializable;

public class ParentData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1059377924437450655L;
	private String relationship;
    private String subParentLoingId;
    private String pId;
    private String idCard;
    private String mobilePhone;
    private String subParentName;
    private String studentId;
    private String photo;
    private Boolean isReceive;
	public String getRelationship() {
		return relationship;
	}
	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
	public String getSubParentLoingId() {
		return subParentLoingId;
	}
	public void setSubParentLoingId(String subParentLoingId) {
		this.subParentLoingId = subParentLoingId;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getSubParentName() {
		return subParentName;
	}
	public void setSubParentName(String subParentName) {
		this.subParentName = subParentName;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public Boolean getIsReceive() {
		return isReceive;
	}
	public void setIsReceive(Boolean isReceive) {
		this.isReceive = isReceive;
	}
    
}
