package com.lester.uteacher.mystudent;

import java.io.Serializable;
import java.util.List;

public class Student implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5137372527930293508L;

	
	public class datas implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = -2731075556401388865L;
		private String birthday;
        private String schoolName;
        private String remark;
        private String nation_display;
        private String id_card;
        private String school_id;
        private String version;
        private String id;
        private String busLineName;
        private String name;
        private int position_status;
        private String gender;
        private String urgent_link_phone;
        private String bus_line_id;
        private String position_status_display;
        private String clazz_id;
        private String urgent_link_man;
        private String clazzName;
        private String entry_time;
        private String photo;
        private String nation;
        private String birthplace;
        private String address;
        private String gender_display;
        private String registered_place;
        private List<parents> parents;
        
		/**
		 * @return the parents
		 */
		public List<parents> getParents() {
			return parents;
		}
		/**
		 * @param parents the parents to set
		 */
		public void setParents(List<parents> parents) {
			this.parents = parents;
		}
		/**
		 * @return the birthday
		 */
		public String getBirthday() {
			return birthday;
		}
		/**
		 * @param birthday the birthday to set
		 */
		public void setBirthday(String birthday) {
			this.birthday = birthday;
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
		 * @return the nation_display
		 */
		public String getNation_display() {
			return nation_display;
		}
		/**
		 * @param nation_display the nation_display to set
		 */
		public void setNation_display(String nation_display) {
			this.nation_display = nation_display;
		}
		/**
		 * @return the id_card
		 */
		public String getId_card() {
			return id_card;
		}
		/**
		 * @param id_card the id_card to set
		 */
		public void setId_card(String id_card) {
			this.id_card = id_card;
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
		 * @return the busLineName
		 */
		public String getBusLineName() {
			return busLineName;
		}
		/**
		 * @param busLineName the busLineName to set
		 */
		public void setBusLineName(String busLineName) {
			this.busLineName = busLineName;
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
		 * @return the position_status
		 */
		public int getPosition_status() {
			return position_status;
		}
		/**
		 * @param position_status the position_status to set
		 */
		public void setPosition_status(int position_status) {
			this.position_status = position_status;
		}
		/**
		 * @return the gender
		 */
		public String getGender() {
			return gender;
		}
		/**
		 * @param gender the gender to set
		 */
		public void setGender(String gender) {
			this.gender = gender;
		}
		/**
		 * @return the urgent_link_phone
		 */
		public String getUrgent_link_phone() {
			return urgent_link_phone;
		}
		/**
		 * @param urgent_link_phone the urgent_link_phone to set
		 */
		public void setUrgent_link_phone(String urgent_link_phone) {
			this.urgent_link_phone = urgent_link_phone;
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
		 * @return the position_status_display
		 */
		public String getPosition_status_display() {
			return position_status_display;
		}
		/**
		 * @param position_status_display the position_status_display to set
		 */
		public void setPosition_status_display(String position_status_display) {
			this.position_status_display = position_status_display;
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
		 * @return the urgent_link_man
		 */
		public String getUrgent_link_man() {
			return urgent_link_man;
		}
		/**
		 * @param urgent_link_man the urgent_link_man to set
		 */
		public void setUrgent_link_man(String urgent_link_man) {
			this.urgent_link_man = urgent_link_man;
		}
		/**
		 * @return the clazzName
		 */
		public String getClazzName() {
			return clazzName;
		}
		/**
		 * @param clazzName the clazzName to set
		 */
		public void setClazzName(String clazzName) {
			this.clazzName = clazzName;
		}
		/**
		 * @return the entry_time
		 */
		public String getEntry_time() {
			return entry_time;
		}
		/**
		 * @param entry_time the entry_time to set
		 */
		public void setEntry_time(String entry_time) {
			this.entry_time = entry_time;
		}
		/**
		 * @return the photo
		 */
		public String getPhoto() {
			return photo;
		}
		/**
		 * @param photo the photo to set
		 */
		public void setPhoto(String photo) {
			this.photo = photo;
		}
		/**
		 * @return the nation
		 */
		public String getNation() {
			return nation;
		}
		/**
		 * @param nation the nation to set
		 */
		public void setNation(String nation) {
			this.nation = nation;
		}
		/**
		 * @return the birthplace
		 */
		public String getBirthplace() {
			return birthplace;
		}
		/**
		 * @param birthplace the birthplace to set
		 */
		public void setBirthplace(String birthplace) {
			this.birthplace = birthplace;
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
		 * @return the gender_display
		 */
		public String getGender_display() {
			return gender_display;
		}
		/**
		 * @param gender_display the gender_display to set
		 */
		public void setGender_display(String gender_display) {
			this.gender_display = gender_display;
		}
		/**
		 * @return the registered_place
		 */
		public String getRegistered_place() {
			return registered_place;
		}
		/**
		 * @param registered_place the registered_place to set
		 */
		public void setRegistered_place(String registered_place) {
			this.registered_place = registered_place;
		}
        
	}
	
	
	public class parents implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = -6666731094838631639L;
		private String parentGender;
		private String parentId;
		private String parentMobilePhone;
		private String parentBirthday;
		private String parentCompany;
		private String parentPhoto;
		private String parentTelephone;
		private String parentEducation;
		private String parentName;
		private String parentIdCard;
		
		/**
		 * @return the parentIdCard
		 */
		public String getParentIdCard() {
			return parentIdCard;
		}
		/**
		 * @param parentIdCard the parentIdCard to set
		 */
		public void setParentIdCard(String parentIdCard) {
			this.parentIdCard = parentIdCard;
		}
		/**
		 * @return the parentGender
		 */
		public String getParentGender() {
			return parentGender;
		}
		/**
		 * @param parentGender the parentGender to set
		 */
		public void setParentGender(String parentGender) {
			this.parentGender = parentGender;
		}
		/**
		 * @return the parentId
		 */
		public String getParentId() {
			return parentId;
		}
		/**
		 * @param parentId the parentId to set
		 */
		public void setParentId(String parentId) {
			this.parentId = parentId;
		}
		/**
		 * @return the parentMobilePhone
		 */
		public String getParentMobilePhone() {
			return parentMobilePhone;
		}
		/**
		 * @param parentMobilePhone the parentMobilePhone to set
		 */
		public void setParentMobilePhone(String parentMobilePhone) {
			this.parentMobilePhone = parentMobilePhone;
		}
		/**
		 * @return the parentBirthday
		 */
		public String getParentBirthday() {
			return parentBirthday;
		}
		/**
		 * @param parentBirthday the parentBirthday to set
		 */
		public void setParentBirthday(String parentBirthday) {
			this.parentBirthday = parentBirthday;
		}
		/**
		 * @return the parentCompany
		 */
		public String getParentCompany() {
			return parentCompany;
		}
		/**
		 * @param parentCompany the parentCompany to set
		 */
		public void setParentCompany(String parentCompany) {
			this.parentCompany = parentCompany;
		}
		/**
		 * @return the parentPhoto
		 */
		public String getParentPhoto() {
			return parentPhoto;
		}
		/**
		 * @param parentPhoto the parentPhoto to set
		 */
		public void setParentPhoto(String parentPhoto) {
			this.parentPhoto = parentPhoto;
		}
		/**
		 * @return the parentTelephone
		 */
		public String getParentTelephone() {
			return parentTelephone;
		}
		/**
		 * @param parentTelephone the parentTelephone to set
		 */
		public void setParentTelephone(String parentTelephone) {
			this.parentTelephone = parentTelephone;
		}
		/**
		 * @return the parentEducation
		 */
		public String getParentEducation() {
			return parentEducation;
		}
		/**
		 * @param parentEducation the parentEducation to set
		 */
		public void setParentEducation(String parentEducation) {
			this.parentEducation = parentEducation;
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
