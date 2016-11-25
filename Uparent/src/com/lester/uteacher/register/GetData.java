package com.lester.uteacher.register;

import java.io.Serializable;
import java.util.List;

public class GetData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -365987308765422418L;

			private String id;
	        private String link_man;
	        private String remark;
	        private String address;
	        private String link_phone;
	        private String name;
	        private String region_id;
	        private String version;
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
			 * @return the link_man
			 */
			public String getLink_man() {
				return link_man;
			}
			/**
			 * @param link_man the link_man to set
			 */
			public void setLink_man(String link_man) {
				this.link_man = link_man;
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
			 * @return the link_phone
			 */
			public String getLink_phone() {
				return link_phone;
			}
			/**
			 * @param link_phone the link_phone to set
			 */
			public void setLink_phone(String link_phone) {
				this.link_phone = link_phone;
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
