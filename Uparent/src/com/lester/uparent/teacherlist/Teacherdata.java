package com.lester.uparent.teacherlist;

import java.io.Serializable;

public class Teacherdata implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -532129051103091131L;
	
	public class datas implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = -1281043166997448968L;
		private String mobile_phone;
		private String name;
		private String gender;
		private String photo;
		
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
		 * @return the mobile_phone
		 */
		public String getMobile_phone() {
			return mobile_phone;
		}
		/**
		 * @param mobile_phone the mobile_phone to set
		 */
		public void setMobile_phone(String mobile_phone) {
			this.mobile_phone = mobile_phone;
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
		
	}
	

}
