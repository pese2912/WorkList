package com.example.memo;

import java.util.ArrayList;

import com.example.memo.util.MemoDBread;
import com.example.memo.util.MySQLiteOpenHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MemoDetailActivity extends Activity implements OnClickListener {
	
	MySQLiteOpenHelper dbhelper;	
	int IDX;
	ArrayList<MemoDBread> data;
	TextView Memotxt;
	Button Btnback;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_memo_detail);
		
		//DB 세팅
		init();
		//뷰세팅
		ViewSetting();		
	}
	
	public void init(){
		//호출한 액티비티에서 넘어온값 세팅
		Intent  intent = getIntent();		
		IDX = intent.getExtras().getInt("IDX", 0);  
		
		dbhelper = new MySQLiteOpenHelper(getApplicationContext(),
				"MEMODB2.db", null, 1);
		
		//호출한 액티비티에서 넘어온값으로 해당 데이터 호출
		data = dbhelper.loadMemoData(IDX);	 		
	}
		
	public void ViewSetting(){
		Memotxt = (TextView)findViewById(R.id.memotxt);
		Memotxt.setText(data.get(0).getMEMO()); //내용을 Memotxt 뿌려준다
		
		Btnback = (Button)findViewById(R.id.btn_back);
		Btnback.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// 이벤트 처리
		switch (v.getId()) {
		case R.id.btn_back :
			//액티비티 종료
			finish();
			break;

		default:
			break;
		}
	}
	
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Toast.makeText(this, "MemoDetailActivity onDestroy()", Toast.LENGTH_SHORT).show();
        dbhelper.close();
    }
 
    @Override
    protected void onPause() {
        super.onPause();
        //Toast.makeText(this, "MemoDetailActivity onPause()", Toast.LENGTH_SHORT).show();
    }
 
    @Override
    protected void onRestart() {
        super.onRestart();
        //Toast.makeText(this, "MemoDetailActivity onRestart()", Toast.LENGTH_SHORT).show();
    }
 
    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(this, "MemoDetailActivity onResume()", Toast.LENGTH_SHORT).show();
    }
 
    @Override
    protected void onStart() {
        super.onStart();
        //Toast.makeText(this, "MemoDetailActivity onStart()", Toast.LENGTH_SHORT).show();
    }
 
    @Override
    protected void onStop() {
        super.onStop();
        //Toast.makeText(this, "MemoDetailActivity onStop()", Toast.LENGTH_SHORT).show();
    }


}
