package com.lester.uteacher.leave;

import java.io.Serializable;

/**
 * @author Administrator
 *请假列表
 */
public class Leavelist implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6360717803642050167L;
	
	public class datas implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 6726081729952859325L;
		private String schoolName;
         private String student_id;
         private String teacherName;
         private String school_id;
         private String version;
         private String id;
         private String content;
         private String end_time;
         private String handle_time;
         private String studentName;
         private String process_status;
         private String start_time;
         private String teacher_id;
         private String bus_line_id;
         private String submit_time;
         private String parentName;
         private String parent_id;
         private String studentPhoto;
         
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
		 * @return the teacherName
		 */
		public String getTeacherName() {
			return teacherName;
		}
		/**
		 * @param teacherName the teacherName to set
		 */
		public void setTeacherName(String teacherName) {
			this.teacherName = teacherName;
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
		 * @return the end_time
		 */
		public String getEnd_time() {
			return end_time;
		}
		/**
		 * @param end_time the end_time to set
		 */
		public void setEnd_time(String end_time) {
			this.end_time = end_time;
		}
		/**
		 * @return the handle_time
		 */
		public String getHandle_time() {
			return handle_time;
		}
		/**
		 * @param handle_time the handle_time to set
		 */
		public void setHandle_time(String handle_time) {
			this.handle_time = handle_time;
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
		 * @return the process_status
		 */
		public String getProcess_status() {
			return process_status;
		}
		/**
		 * @param process_status the process_status to set
		 */
		public void setProcess_status(String process_status) {
			this.process_status = process_status;
		}
		/**
		 * @return the start_time
		 */
		public String getStart_time() {
			return start_time;
		}
		/**
		 * @param start_time the start_time to set
		 */
		public void setStart_time(String start_time) {
			this.start_time = start_time;
		}
		/**
		 * @return the teacher_id
		 */
		public String getTeacher_id() {
			return teacher_id;
		}
		/**
		 * @param teacher_id the teacher_id to set
		 */
		public void setTeacher_id(String teacher_id) {
			this.teacher_id = teacher_id;
		}
		/**
		 * @return the bus_line_id
		 */
		public String getBus_line_id() {
			return bus_line_id;
		}
		/**
		 * @param bus_line_id the bus_line_id to set
		 */
		public void setBus_line_id(String bus_line_id) {
			this.bus_line_id = bus_line_id;
		}
		/**
		 * @return the submit_time
		 */
		public String getSubmit_time() {
			return submit_time;
		}
		/**
		 * @param submit_time the submit_time to set
		 */
		public void setSubmit_time(String submit_time) {
			this.submit_time = submit_time;
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
		/**
		 * @return the parent_id
		 */
		public String getParent_id() {
			return parent_id;
		}
		/**
		 * @param parent_id the parent_id to set
		 */
		public void setParent_id(String parent_id) {
			this.parent_id = parent_id;
		}
         
	}

}
