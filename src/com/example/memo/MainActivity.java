package com.example.memo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	Button Btn_Memo, Btn_Famous, Btn_Schedule, Btn_Dial, Btn_Study,Btn_Card;
	Button Btn_Naver, Btn_Google, Btn_FaceBook;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Btn_Memo = (Button)findViewById(R.id.btn_memo);
		Btn_Famous = (Button)findViewById(R.id.btn_famous);
		Btn_Schedule = (Button)findViewById(R.id.btn_schedule);
		Btn_Dial = (Button)findViewById(R.id.btn_dial);
		Btn_Study = (Button)findViewById(R.id.btn_study);
		Btn_Card = (Button)findViewById(R.id.btn_card);
		Btn_Naver = (Button)findViewById(R.id.btn_naver);
		Btn_Google = (Button)findViewById(R.id.btn_google);
		Btn_FaceBook = (Button)findViewById(R.id.btn_facebook);				
		
		Btn_Memo.setOnClickListener(this);
		Btn_Famous.setOnClickListener(this);
		Btn_Schedule.setOnClickListener(this);
		Btn_Dial.setOnClickListener(this);
		Btn_Study.setOnClickListener(this);
		Btn_Card.setOnClickListener(this);
		Btn_Naver.setOnClickListener(this);
		Btn_Google.setOnClickListener(this);
		Btn_FaceBook.setOnClickListener(this);		
	}

	@Override
	public void onClick(View v) {
		
		Intent intent;
		
		if (Btn_Memo == v) 
		{
			intent = new Intent(this, MemoActivity.class);
			startActivity(intent);
		}
		else if (Btn_Famous == v) 
		{
			intent = new Intent(this, FamousListActivity.class);
			startActivity(intent);
		}
		else if (Btn_Schedule == v){
			intent = new Intent(this, ScheduleActivity.class);
			startActivity(intent);
		}
		else if (Btn_Study == v){
			intent = new Intent(this, StudyActivity.class);
			startActivity(intent);
		}
		else if (Btn_Dial == v) 
		{
			intent = new Intent(Intent.ACTION_DIAL);
		    startActivity(intent);
			
		}
		else if (Btn_Card ==v){
			intent = new Intent(this, CardActivity.class);
			startActivity(intent);
		}
		else if (Btn_Naver == v) 
		{
			intent = new Intent(Intent.ACTION_VIEW);
			Uri u = Uri.parse("http://www.naver.com/");
			intent.setData(u);
			startActivity(intent);
		}
		else if (Btn_Google == v) {
			intent = new Intent(Intent.ACTION_VIEW);
			Uri u = Uri.parse("http://www.google.co.kr");
			intent.setData(u);
			startActivity(intent);			
		}
		else if (Btn_FaceBook == v) {
			intent = new Intent(Intent.ACTION_VIEW);
			Uri u = Uri.parse("http://www.facebook.com");
			intent.setData(u);
			startActivity(intent);			
		}
	}
	
}
