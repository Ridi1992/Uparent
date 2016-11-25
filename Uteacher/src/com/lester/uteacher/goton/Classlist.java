package com.lester.uteacher.goton;

import java.io.Serializable;

public class Classlist implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8896173097910921339L;
	
	
	public class datas implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 8065906892672761398L;
		private String id;
		private String remark;
		private String name;
		private String code;
		private String school_id;
		
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
		 * @return the remark
		 */
		public String getRemark() {
			return remark;
		}
		/**
		 * @param remark the remark to set
		 */
		public void setRemark(String remark) {
			this.remark = remark;
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
		 * @return the code
		 */
		public String getCode() {
			return code;
		}
		/**
		 * @param code the code to set
		 */
		public void setCode(String code) {
			this.code = code;
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
