package com.teacher.data;

import java.util.ArrayList;

public class GetFeedbackdata {
	 private String feedbackPaperName;
	 private String homework;
	 private String feedback_paper_id;
	 private String date;
	 private String teacherName;
	 private float starNum;
	 private ArrayList<answers> answers;
	 
	 
	 public float getStarNum() {
		return starNum;
	}

	public void setStarNum(float starNum) {
		this.starNum = starNum;
	}

	/**
	 * @return the feedbackPaperName
	 */
	public String getFeedbackPaperName() {
		return feedbackPaperName;
	}

	/**
	 * @param feedbackPaperName the feedbackPaperName to set
	 */
	public void setFeedbackPaperName(String feedbackPaperName) {
		this.feedbackPaperName = feedbackPaperName;
	}

	/**
	 * @return the homework
	 */
	public String getHomework() {
		return homework;
	}

	/**
	 * @param homework the homework to set
	 */
	public void setHomework(String homework) {
		this.homework = homework;
	}

	/**
	 * @return the feedback_paper_id
	 */
	public String getFeedback_paper_id() {
		return feedback_paper_id;
	}

	/**
	 * @param feedback_paper_id the feedback_paper_id to set
	 */
	public void setFeedback_paper_id(String feedback_paper_id) {
		this.feedback_paper_id = feedback_paper_id;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
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
	 * @return the answers
	 */
	public ArrayList<answers> getAnswers() {
		return answers;
	}

	/**
	 * @param answers the answers to set
	 */
	public void setAnswers(ArrayList<answers> answers) {
		this.answers = answers;
	}

	public class answers{
		private String answer;
		private int feedbackOptionOrder;
		private String feedbackQuestionNumber;
		private int type;
		/**
		 * @return the answer
		 */
		public String getAnswer() {
			return answer;
		}
		/**
		 * @param answer the answer to set
		 */
		public void setAnswer(String answer) {
			this.answer = answer;
		}
		/**
		 * @return the feedbackOptionOrder
		 */
		public int getFeedbackOptionOrder() {
			return feedbackOptionOrder;
		}
		/**
		 * @param feedbackOptionOrder the feedbackOptionOrder to set
		 */
		public void setFeedbackOptionOrder(int feedbackOptionOrder) {
			this.feedbackOptionOrder = feedbackOptionOrder;
		}
		/**
		 * @return the feedbackQuestionNumber
		 */
		public String getFeedbackQuestionNumber() {
			return feedbackQuestionNumber;
		}
		/**
		 * @param feedbackQuestionNumber the feedbackQuestionNumber to set
		 */
		public void setFeedbackQuestionNumber(String feedbackQuestionNumber) {
			this.feedbackQuestionNumber = feedbackQuestionNumber;
		}
		/**
		 * @return the type
		 */
		public int getType() {
			return type;
		}
		/**
		 * @param type the type to set
		 */
		public void setType(int type) {
			this.type = type;
		}
		
	 }
}
