package com.example.memo;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.memo.util.ScoreDB;

public class CardActivity extends Activity implements OnClickListener {
	Chronometer chronometer_card;
	int[] x = new int[16];
	// 카드 게임
	Button[] buttons = new Button[16];
	boolean selButton = false;
	int nButtonNo = -1;
	int nSelNo = -1;

	// 시작 //종료
	Button btn_start, btn_stop,btn_list;
	String name;
	int falseValue = 0;
	long mTime = 0L;
	ScoreDB dbAdapter;
	TextView tv_score;
	int invisible = 0;
	boolean isNotClick = true;
	int addValue=0;
	boolean isStart =false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card);

		chronometer_card = (Chronometer) findViewById(R.id.chronometer_card);
		chronometer_card.setOnChronometerTickListener(new OnChronometerTickListener() {
			@Override
			public void onChronometerTick(Chronometer cArg) {
				long time = SystemClock.elapsedRealtime() - cArg.getBase();
				int h = (int) (time / 3600000);
				int m = (int) (time - h * 3600000) / 60000;
				int s = (int) (time - h * 3600000 - m * 60000) / 1000;

				String mm = m < 10 ? "0" + m : m + "";
				String ss = s < 10 ? "0" + s : s + "";
				cArg.setText(mm + ":" + ss);
				mTime = time/1000; 
			}
		});
		chronometer_card.setText("00:00");
		int resid = R.id.button_01;
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = (Button) findViewById(resid);
			resid++;
			buttons[i].setOnClickListener(this);
		}
		btn_start = (Button) findViewById(R.id.btn_start);
		btn_stop = (Button) findViewById(R.id.btn_end);
		btn_list = (Button) findViewById(R.id.btn_list);
		
		tv_score = (TextView)findViewById(R.id.tv_score);
		tv_score.setText("0");
		btn_start.setOnClickListener(this);
		btn_stop.setOnClickListener(this);
		btn_list.setOnClickListener(this);
		dbAdapter = new ScoreDB(this);
		dbAdapter.Open();
		GameInit();
		
		

	}
	public void onDestroy(){
		super.onDestroy();
		dbAdapter.close();
		dbAdapter = null;
		GameInit();
	}

	public void setData() {
		ArrayList<Integer> alList = new ArrayList<Integer>();
		alList.add(0);
		alList.add(0);
		alList.add(1);
		alList.add(1);
		alList.add(2);
		alList.add(2);
		alList.add(3);
		alList.add(3);
		alList.add(4);
		alList.add(4);
		alList.add(5);
		alList.add(5);
		alList.add(6);
		alList.add(6);
		alList.add(7);
		alList.add(7);
		int k = 0;
		for (int i = 0; i < x.length; i++) {
			k = (int) (Math.random() * alList.size());
			x[i] = alList.get(k);
			alList.remove(k);
		}
		for (int i = 0; i < x.length; i++) {
			buttons[i].setText(x[i] + "");
			buttons[i].setVisibility(View.VISIBLE);
		}
		isNotClick= true;
		tv_score.setText("");
		isStart = true;
		handler.sendEmptyMessageDelayed(2, 3000);

	}

	public void startDialogshow() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("등록될 이름");
		alert.setMessage("이름을 입력하세요.");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				name =  value;
				
				// Do something with value!
				
				setData();
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.

			}
		});

		alert.show();
	}

	public void stopTime(boolean isEnd) {
		String time = chronometer_card.getText().toString();
		
		// 스탑워치 멈추기
		chronometer_card.stop();
		// 추가후 저장
		
		if(isEnd)
			new ValueAsync().execute();

		GameInit();
		handler.removeMessages(2);
	}
	public class ValueAsync extends AsyncTask<Void, Void, Long>
	{
		ProgressDialog pdDialog;
		int value;
		@Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdDialog = new ProgressDialog(CardActivity.this);
            pdDialog.setTitle("점수계산중입니다.");
            pdDialog.show();
            
        }
		@Override
		protected Long doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			//1초제한
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			value =setValue();
			long Return = dbAdapter.createScore(name, value);
			return Return;
			
			
		}
		@Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
             if(result<=0){
            	  Toast.makeText(CardActivity.this, "기록 입력 실패하였습니다", Toast.LENGTH_SHORT).show();
             }else{
            	 tv_score.setText(""+value);
             }
            if(pdDialog != null){
            	pdDialog.dismiss();
            }
             
        }
		
	}
	public int setValue(){
		int value;
		if(falseValue==0){
			value = 1000;
		}else if(falseValue<=5){
			value = 900;
		}else if(falseValue<=10){
			value = 700;
		}else if(falseValue<=15){
			value = 500;
		}else if(falseValue<=20){
			value = 300;
		}else {
			value = 100;
		}
		if(mTime<=5){
			value+=1000;
		}else if(mTime<=10){
			value+=900;
		}else if(mTime<=13){
			value+=850;
		}else if(mTime<=16){
			value+=850;
		}else if(mTime<=19){
			value+=800;
		}else if(mTime<=22){
			value+=750;
		}else if(mTime<=25){
			value+=700;
		}else if(mTime<=30){
			value+=650;
		}else if(mTime<=35){
			value+=600;
		}else if(mTime<=40){
			value+=500;
		}else if(mTime<=45){
			value+=400;
		}else if(mTime<=50){
			value+=300;
		}else if(mTime<=60){
			value+=200;
		}else{
			value+=100;
		}
		value+=addValue;
		return value;
		
	}

	@Override
	public void onClick(View v) {
		if (v == btn_start) {
			if(!isStart)
				startDialogshow();
		} else if(v == btn_stop){
			if(isStart){
				stopTime(false);
			}
		}else if(v == btn_list){
			Intent intent = new Intent(CardActivity.this,CardListActivity.class);
			startActivity(intent);
		}else {
			if(isNotClick){
				return;
			}
			for (int i = 0; i < buttons.length; i++) {
				if (v == buttons[i]) {
					if (selButton) {
						if (nButtonNo != i) {
							if (nSelNo == x[i]) {
								buttons[i].setVisibility(View.INVISIBLE);
								buttons[nButtonNo].setVisibility(View.INVISIBLE);
								invisible +=2;
								addValue += 100;
								//점수 추가 표시
								handler.sendEmptyMessage(3);
								if(invisible==16){
									stopTime(true);
								}
							} else {
								buttons[i].setText(""+x[i]);
								falseValue++;
							}
							// 초기화
							nSelNo = -1;
							nButtonNo = -1;
							selButton = false;
							// 핸들러로 0.5초뒤 초기화
							isNotClick= true;
							handler.sendEmptyMessageDelayed(1, 500);
						}
					} else {
						selButton = true;
						nSelNo = x[i];
						nButtonNo = i;
						buttons[i].setText(""+x[i]);
					}
				}
			}
		}
	}
	public void GameInit(){
		// 초기화
		nSelNo = -1;
		nButtonNo = -1;
		selButton = false;
		// 핸들러로 0.5초뒤 초기화
		isNotClick= true; 
		isStart = false;
		addValue = 0;
		invisible = 0;
		for (int i = 0; i < x.length; i++) {
			buttons[i].setText("");
			buttons[i].setVisibility(View.VISIBLE);
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				for (int i = 0; i < x.length; i++) {
					buttons[i].setText("");
				}
				isNotClick = false;
			}else if(msg.what ==2){
				for (int i = 0; i < x.length; i++) {
					buttons[i].setText("");
				}
				chronometer_card.setBase(SystemClock.elapsedRealtime());
				chronometer_card.start();
				isNotClick = false;
			}else if(msg.what ==3){
				tv_score.setText(addValue+"");
			}
		}
	};

}
