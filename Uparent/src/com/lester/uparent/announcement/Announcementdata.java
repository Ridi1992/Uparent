package com.lester.uparent.announcement;

import java.io.Serializable;

public class Announcementdata{
	
	public class datas{
		 /**
		 * 
		 */
			private String content;
		 	private String id;
           private String title;
           private String releaser_id;
           private String school_id;
           private String release_time;
           private String version;
           private String titleImg;
           
		/**
		 * @return the titleImg
		 */
		public String getTitleImg() {
			return titleImg;
		}
		/**
		 * @param titleImg the titleImg to set
		 */
		public void setTitleImg(String titleImg) {
			this.titleImg = titleImg;
		}
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
		 * @return the title
		 */
		public String getTitle() {
			return title;
		}
		/**
		 * @param title the title to set
		 */
		public void setTitle(String title) {
			this.title = title;
		}
		/**
		 * @return the releaser_id
		 */
		public String getReleaser_id() {
			return releaser_id;
		}
		/**
		 * @param releaser_id the releaser_id to set
		 */
		public void setReleaser_id(String releaser_id) {
			this.releaser_id = releaser_id;
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
		 * @return the release_time
		 */
		public String getRelease_time() {
			return release_time;
		}
		/**
		 * @param release_time the release_time to set
		 */
		public void setRelease_time(String release_time) {
			this.release_time = release_time;
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
