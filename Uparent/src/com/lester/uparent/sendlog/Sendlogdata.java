package com.lester.uparent.sendlog;

import java.io.Serializable;

public class Sendlogdata implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 872556082038086587L;

	public  class datas implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -4421704381155970389L;
		private String  id;
		private int  model;
		private String  student_id;
		private String  type;
		private String  transfer_time;
		private String  school_id;
		private String  photo;
		
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
		 * @return the model
		 */
		public int getModel() {
			return model;
		}
		/**
		 * @param model the model to set
		 */
		public void setModel(int model) {
			this.model = model;
		}
		/**
		 * @return the student_id
		 */
		public String getStudent_id() {
			return student_id;
		}
		/**
		 * @param student_id the student_id to set
		 */
		public void setStudent_id(String student_id) {
			this.student_id = student_id;
		}
		/**
		 * @return the type
		 */
		public String getType() {
			return type;
		}
		/**
		 * @param type the type to set
		 */
		public void setType(String type) {
			this.type = type;
		}
		/**
		 * @return the transfer_time
		 */
		public String getTransfer_time() {
			return transfer_time;
		}
		/**
		 * @param transfer_time the transfer_time to set
		 */
		public void setTransfer_time(String transfer_time) {
			this.transfer_time = transfer_time;
		}
		/**
		 * @return the school_id
		 */
		public String getSchool_id() {
			return school_id;
		}
		/**
		 * @param school_id the school_id to set
		 */
		public void setSchool_id(String school_id) {
			this.school_id = school_id;
		}
		
	}
}
