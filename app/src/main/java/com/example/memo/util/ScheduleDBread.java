package com.example.memo.util;

public class ScheduleDBread {

	private int id;
	private String name;
	private String location;
	private String day;
	private int hourTime;	
	private int minuteTime;
	
	public ScheduleDBread(int id, String name, String location, String day,  int hour, int minute) {
		this.id = id;
		this.name = name;
		this.location = location;
		this.day = day;
		this.hourTime = hour;
		this.minuteTime = minute;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLoacation() {
		return location;
	}
	public void setLoacation(String loacation) {
		this.location = loacation;
	}
	public String getDay()	{
		return this.day;
	}
	public void setDay(String day){
		this.day = day;
	}
	
	public int getHourTime() {
		return hourTime;
	}
	public void setHourTime(int hourTime) {
		this.hourTime = hourTime;
	}
	public int getMinuteTime() {
		return minuteTime;
	}
	public void setMinuteTime(int minuteTime) {
		this.minuteTime = minuteTime;
	}
}
