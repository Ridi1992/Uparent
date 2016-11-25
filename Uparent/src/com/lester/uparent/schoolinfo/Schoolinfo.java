package com.lester.uparent.schoolinfo;

import java.io.Serializable;

public class Schoolinfo  implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 671384842111641791L;

	public class data implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = -6135125644308415471L;
		  private String linkPhone;
          private String address;
          private String linkMan;
          private String name;
          private String region_id;
        private String introduction;
		/**
		 * @return the linkPhone
		 */
		public String getLinkPhone() {
			return linkPhone;
		}
		/**
		 * @param linkPhone the linkPhone to set
		 */
		public void setLinkPhone(String linkPhone) {
			this.linkPhone = linkPhone;
		}
		/**
		 * @return the address
		 */
		public String getAddress() {
			return address;
		}
		/**
		 * @param address the address to set
		 */
		public void setAddress(String address) {
			this.address = address;
		}
		/**
		 * @return the linkMan
		 */
		public String getLinkMan() {
			return linkMan;
		}
		/**
		 * @param linkMan the linkMan to set
		 */
		public void setLinkMan(String linkMan) {
			this.linkMan = linkMan;
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
		 * @return the region_id
		 */
		public String getRegion_id() {
			return region_id;
		}
		/**
		 * @param region_id the region_id to set
		 */
		public void setRegion_id(String region_id) {
			this.region_id = region_id;
		}
		/**
		 * @return the introduction
		 */
		public String getIntroduction() {
			return introduction;
		}
		/**
		 * @param introduction the introduction to set
		 */
		public void setIntroduction(String introduction) {
			this.introduction = introduction;
		}
        
	}
	
}
