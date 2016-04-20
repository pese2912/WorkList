package com.example.memo.item;

public class Study {
	String date;
	String time;
	String content;
	
	public Study(String date, String time, String content) {
		super();
		this.date = date;
		this.time = time;
		this.content = content;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

}
