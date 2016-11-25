package com.teacher.data;

import java.io.Serializable;
import java.util.ArrayList;

public class CouserData implements Serializable {

	
		private String starttime;
		private String endtime;
		private String title;
		private String content;
		
		/**
		 * @return the starttime
		 */
		public String getStarttime() {
			return starttime;
		}
		/**
		 * @param starttime the starttime to set
		 */
		public void setStarttime(String starttime) {
			this.starttime = starttime;
		}
		/**
		 * @return the endtime
		 */
		public String getEndtime() {
			return endtime;
		}
		/**
		 * @param endtime the endtime to set
		 */
		public void setEndtime(String endtime) {
			this.endtime = endtime;
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
		
}
