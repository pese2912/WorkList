package com.example.memo.item;

import java.util.ArrayList;

public class StudyList {
	ArrayList<Study> alStudy;

	public StudyList(ArrayList<Study> alStudy) {
		super();
		this.alStudy = alStudy;
	}

	public ArrayList<Study> getAlStudy() {
		return alStudy;
	}

	public void setAlStudy(ArrayList<Study> alStudy) {
		this.alStudy = alStudy;
	}
	
}
