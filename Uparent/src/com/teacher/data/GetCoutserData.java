package com.teacher.data;

import java.util.ArrayList;

public class GetCoutserData {
	private String id;//	----课程表id
	private String time;//						----本周日期范围
	private String clazz_id;//	----班级id
	private String current_object;//					----本周目标
	private String create_date;//		----创建日期
	private String create_by;
	private String update_date;
	private String year;	//								----年份
	private String school_id;//	----学校id
	private String week;	//								----周数
	private String parent_work;//						----家长工作
	private String update_by;	//					
	private String version;//
	private ArrayList<ArrayList<scheduleRecords>> scheduleRecords;
	private ArrayList<courseInfo> courseInfo;
	
	
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
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @return the clazz_id
	 */
	public String getClazz_id() {
		return clazz_id;
	}

	/**
	 * @param clazz_id the clazz_id to set
	 */
	public void setClazz_id(String clazz_id) {
		this.clazz_id = clazz_id;
	}

	/**
	 * @return the current_object
	 */
	public String getCurrent_object() {
		return current_object;
	}

	/**
	 * @param current_object the current_object to set
	 */
	public void setCurrent_object(String current_object) {
		this.current_object = current_object;
	}

	/**
	 * @return the create_date
	 */
	public String getCreate_date() {
		return create_date;
	}

	/**
	 * @param create_date the create_date to set
	 */
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}


	/**
	 * @return the create_by
	 */
	public String getCreate_by() {
		return create_by;
	}

	/**
	 * @param create_by the create_by to set
	 */
	public void setCreate_by(String create_by) {
		this.create_by = create_by;
	}

	/**
	 * @param school_id the school_id to set
	 */
	public void setSchool_id(String school_id) {
		this.school_id = school_id;
	}

	/**
	 * @return the update_date
	 */
	public String getUpdate_date() {
		return update_date;
	}

	/**
	 * @param update_date the update_date to set
	 */
	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}

	/**
	 * @return the year
	 */
	public String getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}


	/**
	 * @return the school_id
	 */
	public String getSchool_id() {
		return school_id;
	}


	/**
	 * @return the week
	 */
	public String getWeek() {
		return week;
	}

	/**
	 * @param week the week to set
	 */
	public void setWeek(String week) {
		this.week = week;
	}


	/**
	 * @return the parent_work
	 */
	public String getParent_work() {
		return parent_work;
	}

	/**
	 * @param parent_work the parent_work to set
	 */
	public void setParent_work(String parent_work) {
		this.parent_work = parent_work;
	}

	/**
	 * @return the update_by
	 */
	public String getUpdate_by() {
		return update_by;
	}

	/**
	 * @param update_by the update_by to set
	 */
	public void setUpdate_by(String update_by) {
		this.update_by = update_by;
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
	 * @return the scheduleRecords
	 */
	public ArrayList<ArrayList<scheduleRecords>> getScheduleRecords() {
		return scheduleRecords;
	}

	/**
	 * @param scheduleRecords the scheduleRecords to set
	 */
	public void setScheduleRecords(
			ArrayList<ArrayList<scheduleRecords>> scheduleRecords) {
		this.scheduleRecords = scheduleRecords;
	}

	/**
	 * @return the courseInfo
	 */
	public ArrayList<courseInfo> getCourseInfo() {
		return courseInfo;
	}

	/**
	 * @param courseInfo the courseInfo to set
	 */
	public void setCourseInfo(ArrayList<courseInfo> courseInfo) {
		this.courseInfo = courseInfo;
	}

	public class scheduleRecords{
		private String no;
		private String name;
		/**
		 * @return the no
		 */
		public String getNo() {
			return no;
		}
		/**
		 * @param no the no to set
		 */
		public void setNo(String no) {
			this.no = no;
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
		
	}
	
	public class courseInfo{
		private String startTime;
		private int no;
		private String endTime;
		private String name;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * @return the startTime
		 */
		public String getStartTime() {
			return startTime;
		}
		/**
		 * @param startTime the startTime to set
		 */
		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}

		public int getNo() {
			return no;
		}
		public void setNo(int no) {
			this.no = no;
		}
		/**
		 * @return the endTime
		 */
		public String getEndTime() {
			return endTime;
		}
		/**
		 * @param endTime the endTime to set
		 */
		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}
		
	}
}
