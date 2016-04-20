package com.example.memo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.memo.adapter.StudyListAdapter;
import com.example.memo.item.Study;
import com.example.memo.item.StudyList;
import com.example.memo.util.MemoSharedpreference;
import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;

public class StudyActivity extends Activity implements OnClickListener {
	String inputData="";
	FloatingActionButton btn_start,btn_end,btn_list;
	Chronometer chronometer_study;
	SimpleDateFormat sdf;
	boolean isStart = false;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_study);
		btn_start = (FloatingActionButton)findViewById(R.id.btn_start);
		btn_end = (FloatingActionButton)findViewById(R.id.btn_end);
		btn_list = (FloatingActionButton)findViewById(R.id.btn_list);
		sdf =  new SimpleDateFormat("yyyy-MM-dd");
		
		chronometer_study = (Chronometer)findViewById(R.id.chronometer_study);
		chronometer_study.setOnChronometerTickListener(new OnChronometerTickListener(){
	        @Override
	            public void onChronometerTick(Chronometer cArg) {
	            long time = SystemClock.elapsedRealtime() - cArg.getBase();
	            int h   = (int)(time /3600000);
	            int m = (int)(time - h*3600000)/60000;
	            int s= (int)(time - h*3600000- m*60000)/1000 ;
	            String hh = h < 10 ? "0"+h: h+"";
	            String mm = m < 10 ? "0"+m: m+"";
	            String ss = s < 10 ? "0"+s: s+"";
	            cArg.setText(hh+":"+mm+":"+ss);
	        }
	    });
		
		chronometer_study.setText("00:00:00");
		
		btn_start.setOnClickListener(this);
		btn_end.setOnClickListener(this);
		btn_list.setOnClickListener(this);
		
	}
	
	public void dialogshow(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("공부");
		alert.setMessage("공부할 내용을 입력하세요.");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				inputData = value;
				chronometer_study.setBase(SystemClock.elapsedRealtime());
				chronometer_study.start();
				// Do something with value!
			}
		});


		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
						
					}
				});
		
		alert.show(); 
	}
	public void stopTime(){
		String time =chronometer_study.getText().toString();
		String gsonData = MemoSharedpreference.getSharedPrefStudy(this);
		Gson gson = new Gson();
		ArrayList<Study> alStudy;
		//리스트가져오기
		if(!gsonData.equals("")){
			alStudy =gson.fromJson(gsonData,StudyList.class).getAlStudy();
		}else{
			alStudy = new ArrayList<Study>();
		}
		//스탑워치 멈추기
		chronometer_study.stop();
		//추가후 저장
		
		alStudy.add(0,new Study(sdf.format(new Date()),chronometer_study.getText().toString(),inputData));
		gsonData = gson.toJson(new StudyList(alStudy));
		
		MemoSharedpreference.setSharedPrefStudy(this,gsonData);

		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == btn_start){
			if(!isStart){
				dialogshow();
				isStart =  true;
			}
		}else if(v==btn_end){
			if(isStart){
				stopTime();
				isStart = false;
			}
		}else if(v==btn_list){
			
		
			
			Intent intent = new Intent(StudyActivity.this,StudyListActivity.class);
			startActivity(intent);
		}
		
	}

	
}
