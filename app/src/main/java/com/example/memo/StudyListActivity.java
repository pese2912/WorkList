package com.example.memo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;

import com.example.memo.adapter.MemoListAdapter.clickListener;
import com.example.memo.adapter.StudyListAdapter;
import com.example.memo.item.Study;
import com.example.memo.item.StudyList;
import com.example.memo.util.MemoSharedpreference;
import com.google.gson.Gson;

public class StudyListActivity extends Activity implements OnClickListener {
	Animation anim;
	Button btn_all_del;
	StudyListAdapter mAdapter;
	ListView list_study;
	ArrayList<Study> alStudy;
	Gson gson;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_study_list);
		
		String gsonData = MemoSharedpreference.getSharedPrefStudy(this);
		gson = new Gson();
		
		//리스트가져오기
		if(!gsonData.equals("")){
			alStudy =gson.fromJson(gsonData,StudyList.class).getAlStudy();
		}else{
			alStudy = new ArrayList<Study>();
		}
		list_study =  (ListView)findViewById(R.id.list_study);
		btn_all_del = (Button)findViewById(R.id.btn_all_del);

		btn_all_del.setOnClickListener(this);
		
		
		anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
		btn_all_del.startAnimation(anim);

		listSetting();
	}
	
	public void onStop(){
		super.onStop();
		//해당 데이터 저장
		String gsonData = gson.toJson(new StudyList(alStudy));		
		MemoSharedpreference.setSharedPrefStudy(this,gsonData);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==btn_all_del){
			alStudy.clear();
			mAdapter.notifyDataSetChanged();
		}
	}
	public void listSetting(){
		//메모내용및 등록일을 리스트뷰에 뿌려줄 Adapter
		                    //리스트뷰 R.layout.row_memo_list 레이아웃, 메모데이터, 클릭 리스너
		mAdapter = new StudyListAdapter( this,R.layout.row_study_list, alStudy,new StudyListAdapter.clickListener() {
			
			@Override
			public void OnClick(int position, int idx) {				
				//MemoListAdapter에서 넘어온 position, 메모글 아이디
				//1. 공유, 2. 삭제
				if (idx == 1) {
					Intent intent = new Intent(Intent.ACTION_SEND);					
					intent.setType("text/plain");
					//intent.putExtra(Intent.EXTRA_SUBJECT,"공유하기");
					intent.putExtra(Intent.EXTRA_TEXT,alStudy.get(position).getDate()+"\n"+alStudy.get(position).getContent()+"\n"+alStudy.get(position).getTime()+"하였음");
								
					startActivity(Intent.createChooser(intent, "선택하세요"));
				}else if(idx ==2){
					alStudy.remove(position);
					mAdapter.notifyDataSetChanged();
				}
			}
		});
		
		// 리스트뷰 MemoListAdapter 연결
		list_study.setAdapter(mAdapter);	
	}
	
}
