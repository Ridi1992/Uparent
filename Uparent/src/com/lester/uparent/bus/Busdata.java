package com.lester.uparent.bus;

import java.io.Serializable;

public class Busdata implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4019740720772765216L;
	 
	public class  data implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 5657745927113961158L;
		 private String teacherGender;
	         private String seatCount;
	         private String busTime;
	         private String teacherPhoto;
	         private String busLicense;
	         private String lineName;
	         private String teacherPhone;
	         private String teacherName;
	         
			/**
			 * @return the teacherGender
			 */
			public String getTeacherGender() {
				return teacherGender;
			}
			/**
			 * @param teacherGender the teacherGender to set
			 */
			public void setTeacherGender(String teacherGender) {
				this.teacherGender = teacherGender;
			}
			/**
			 * @return the seatCount
			 */
			public String getSeatCount() {
				return seatCount;
			}
			/**
			 * @param seatCount the seatCount to set
			 */
			public void setSeatCount(String seatCount) {
				this.seatCount = seatCount;
			}
			/**
			 * @return the busTime
			 */
			public String getBusTime() {
				return busTime;
			}
			/**
			 * @param busTime the busTime to set
			 */
			public void setBusTime(String busTime) {
				this.busTime = busTime;
			}
			/**
			 * @return the teacherPhoto
			 */
			public String getTeacherPhoto() {
				return teacherPhoto;
			}
			/**
			 * @param teacherPhoto the teacherPhoto to set
			 */
			public void setTeacherPhoto(String teacherPhoto) {
				this.teacherPhoto = teacherPhoto;
			}
			/**
			 * @return the busLicense
			 */
			public String getBusLicense() {
				return busLicense;
			}
			/**
			 * @param busLicense the busLicense to set
			 */
			public void setBusLicense(String busLicense) {
				this.busLicense = busLicense;
			}
			/**
			 * @return the lineName
			 */
			public String getLineName() {
				return lineName;
			}
			/**
			 * @param lineName the lineName to set
			 */
			public void setLineName(String lineName) {
				this.lineName = lineName;
			}
			/**
			 * @return the teacherPhone
			 */
			public String getTeacherPhone() {
				return teacherPhone;
			}
			/**
			 * @param teacherPhone the teacherPhone to set
			 */
			public void setTeacherPhone(String teacherPhone) {
				this.teacherPhone = teacherPhone;
			}
			/**
			 * @return the teacherName
			 */
			public String getTeacherName() {
				return teacherName;
			}
			/**
			 * @param teacherName the teacherName to set
			 */
			public void setTeacherName(String teacherName) {
				this.teacherName = teacherName;
			}
	         
	}

}
