package com.lester.uteacher.mystudent;

import java.io.Serializable;

public class GetMessgae implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7873512803135970602L;
	
	public class datas implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1337014379927160491L;
		private String content;
		private String send_time;
		private String sender_name;
		private String receiver_name;
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
		 * @return the sender_name
		 */
		public String getSender_name() {
			return sender_name;
		}
		/**
		 * @param sender_name the sender_name to set
		 */
		public void setSender_name(String sender_name) {
			this.sender_name = sender_name;
		}
		/**
		 * @return the receiver_name
		 */
		public String getReceiver_name() {
			return receiver_name;
		}
		/**
		 * @param receiver_name the receiver_name to set
		 */
		public void setReceiver_name(String receiver_name) {
			this.receiver_name = receiver_name;
		}
		
	}

}
