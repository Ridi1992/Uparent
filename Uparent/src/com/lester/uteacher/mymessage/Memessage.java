package com.lester.uteacher.mymessage;

import java.io.Serializable;

public class Memessage {

	
	
	public class datas {

		private String content;
		private String login_id;
        private String id;
        private String send_time;
        private String type;
        private Boolean has_read;
        private String school_id;
        private String version;
		/**
		 * @return the content
		 */
		public String getContent() {
			return content;
		}
		/**
		 * @param content the content to set
		 */
		public void setContent(String content) {
			this.content = content;
		}
		/**
		 * @return the login_id
		 */
		public String getLogin_id() {
			return login_id;
		}
		/**
		 * @param login_id the login_id to set
		 */
		public void setLogin_id(String login_id) {
			this.login_id = login_id;
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
		 * @return the send_time
		 */
		public String getSend_time() {
			return send_time;
		}
		/**
		 * @param send_time the send_time to set
		 */
		public void setSend_time(String send_time) {
			this.send_time = send_time;
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
		 * @return the has_read
		 */
		public Boolean getHas_read() {
			return has_read;
		}
		/**
		 * @param has_read the has_read to set
		 */
		public void setHas_read(Boolean has_read) {
			this.has_read = has_read;
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
		/**
		 * @return the version
		 */
		public String getVersion() {
			return version;
		}
		/**
		 * @param version the version to set
		 */
		public void setVersion(String version) {
			this.version = version;
		}
        
	}

}
