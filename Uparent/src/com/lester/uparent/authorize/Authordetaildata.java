package com.lester.uparent.authorize;

import java.io.Serializable;

public class Authordetaildata implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 737567614934397425L;

	
	
	public class data implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 7469716708996065350L;
		
		 private String content;
	        private String agentName;
	        private String studentId;
	        private String agentIdCard;
	        private String studentName;
	        private String agentTime;
	        private String orderStatus;
	        private String parentName;
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
			 * @return the agentName
			 */
			public String getAgentName() {
				return agentName;
			}
			/**
			 * @param agentName the agentName to set
			 */
			public void setAgentName(String agentName) {
				this.agentName = agentName;
			}
			/**
			 * @return the studentId
			 */
			public String getStudentId() {
				return studentId;
			}
			/**
			 * @param studentId the studentId to set
			 */
			public void setStudentId(String studentId) {
				this.studentId = studentId;
			}
			/**
			 * @return the agentIdCard
			 */
			public String getAgentIdCard() {
				return agentIdCard;
			}
			/**
			 * @param agentIdCard the agentIdCard to set
			 */
			public void setAgentIdCard(String agentIdCard) {
				this.agentIdCard = agentIdCard;
			}
			/**
			 * @return the studentName
			 */
			public String getStudentName() {
				return studentName;
			}
			/**
			 * @param studentName the studentName to set
			 */
			public void setStudentName(String studentName) {
				this.studentName = studentName;
			}
			/**
			 * @return the agentTime
			 */
			public String getAgentTime() {
				return agentTime;
			}
			/**
			 * @param agentTime the agentTime to set
			 */
			public void setAgentTime(String agentTime) {
				this.agentTime = agentTime;
			}
			/**
			 * @return the orderStatus
			 */
			public String getOrderStatus() {
				return orderStatus;
			}
			/**
			 * @param orderStatus the orderStatus to set
			 */
			public void setOrderStatus(String orderStatus) {
				this.orderStatus = orderStatus;
			}
			/**
			 * @return the parentName
			 */
			public String getParentName() {
				return parentName;
			}
			/**
			 * @param parentName the parentName to set
			 */
			public void setParentName(String parentName) {
				this.parentName = parentName;
			}
	        
	}
}

