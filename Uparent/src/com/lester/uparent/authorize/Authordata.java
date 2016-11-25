package com.lester.uparent.authorize;

import java.io.Serializable;

public class Authordata implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3719639901660702344L;

	public class datas implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 2691142947308791634L;
		
		private String id;
        private String schoolName;
        private String ClazzName;
        private String agentName;
        private String agentIdCard;
        private String studentName;
        private String studentPhoto;
        private String agentPhone;
        private String agentTime;
        private String orderStatus;
        private String createDate;
        
        
		/**
		 * @return the createDate
		 */
		public String getCreateDate() {
			return createDate;
		}
		/**
		 * @param createDate the createDate to set
		 */
		public void setCreateDate(String createDate) {
			this.createDate = createDate;
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
		 * @return the schoolName
		 */
		public String getSchoolName() {
			return schoolName;
		}
		/**
		 * @param schoolName the schoolName to set
		 */
		public void setSchoolName(String schoolName) {
			this.schoolName = schoolName;
		}
		/**
		 * @return the clazzName
		 */
		public String getClazzName() {
			return ClazzName;
		}
		/**
		 * @param clazzName the clazzName to set
		 */
		public void setClazzName(String clazzName) {
			ClazzName = clazzName;
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
		 * @return the studentPhoto
		 */
		public String getStudentPhoto() {
			return studentPhoto;
		}
		/**
		 * @param studentPhoto the studentPhoto to set
		 */
		public void setStudentPhoto(String studentPhoto) {
			this.studentPhoto = studentPhoto;
		}
		/**
		 * @return the agentPhone
		 */
		public String getAgentPhone() {
			return agentPhone;
		}
		/**
		 * @param agentPhone the agentPhone to set
		 */
		public void setAgentPhone(String agentPhone) {
			this.agentPhone = agentPhone;
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
        
		
	}
}
