package com.example.memo.util;

public class MemoDBread {
	int id;
	String DATE;
	String MEMO;
	int Check;
	public MemoDBread() {
		// TODO Auto-generated constructor stub
	}
	
	public MemoDBread(int id, String date, String memo, int Check) {
		super();
		
		this.id    = id;	//메모아이디
		this.DATE  = date;  //메모등록일
		this.MEMO  = memo;  //메모내용
		this.Check = Check; //체크여부
	}

	public int getCheck() {
		return Check;
	}

	public void setCheck(int Check) {
		this.Check = Check;
	}
	
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getDATE() {
		return DATE;
	}


	public void setDATE(String dATE) {
		DATE = dATE;
	}


	public String getMEMO() {
		return MEMO;
	}


	public void setMEMO(String mEMO) {
		MEMO = mEMO;
	}



}
