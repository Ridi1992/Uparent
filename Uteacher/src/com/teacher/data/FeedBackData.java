package com.teacher.data;

import java.util.ArrayList;

public class FeedBackData {

	private String content;
	private String subcontent;
	private boolean  isInput;
	private String special_act;
	private String number;
	
	
	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}
	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}
	/**
	 * @return the special_act
	 */
	public String getSpecial_act() {
		return special_act;
	}
	/**
	 * @param special_act the special_act to set
	 */
	public void setSpecial_act(String special_act) {
		this.special_act = special_act;
	}
	/**
	 * @return the isInput
	 */
	public boolean isInput() {
		return isInput;
	}
	/**
	 * @param isInput the isInput to set
	 */
	public void setInput(boolean isInput) {
		this.isInput = isInput;
	}



	private ArrayList< Option> options;
	
	
	public void  indata(){
		options=new ArrayList<Option>();
	}
	/**
	 * @return the options
	 */
	public ArrayList<Option> getOptions() {
		return options;
	}



	/**
	 * @param options the options to set
	 */
	public void setOptions(ArrayList<Option> options) {
		this.options = options;
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
	 * @return the subcontent
	 */
	public String getSubcontent() {
		return subcontent;
	}



	/**
	 * @param subcontent the subcontent to set
	 */
	public void setSubcontent(String subcontent) {
		this.subcontent = subcontent;
	}


	
}
