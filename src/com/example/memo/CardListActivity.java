package com.example.memo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.example.memo.adapter.CardListAdapter;
import com.example.memo.adapter.MemoListAdapter.clickListener;
import com.example.memo.adapter.StudyListAdapter;
import com.example.memo.item.CardRank;
import com.example.memo.item.Study;
import com.example.memo.item.StudyList;
import com.example.memo.util.MemoSharedpreference;
import com.example.memo.util.ScoreDB;
import com.google.gson.Gson;

public class CardListActivity extends Activity implements OnClickListener {
	
	CardListAdapter mAdapter;
	ListView list_card;
	ArrayList<CardRank> alCardRank =  new ArrayList<CardRank>();
	ScoreDB dbAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_list);
		
		list_card =  (ListView)findViewById(R.id.list_card);
		dbAdapter = new ScoreDB(this);
		dbAdapter.Open();
		setData();
		listSetting();
	}
	private void setData() {
		// TODO Auto-generated method stub
		Cursor c= dbAdapter.fetchAllEntry();
		alCardRank.clear();
		while(c.moveToNext()){
			System.out.println("c.getString(1),c.getInt(2)"+c.getString(1)+c.getInt(2));
			alCardRank.add(new CardRank(c.getString(1),c.getInt(2)));
			if(alCardRank.size()==10){
				break;
			}
		}
		
	}
	public void onDestroy(){
		super.onDestroy();
		dbAdapter.close();
		dbAdapter = null;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	public void listSetting(){
		//랭크내용을 리스트뷰에 뿌려줄 Adapter
		                    //리스트뷰 R.layout.row_rank_list 레이아웃, 랭크데이터, 클릭 리스너
		
		mAdapter = new CardListAdapter( this,R.layout.row_rank_list, alCardRank,new CardListAdapter.clickListener() {
			
			@Override
			public void OnClick(int position, int idx) {				
				
			}
		});
		
		// 리스트뷰 MemoListAdapter 연결
		list_card.setAdapter(mAdapter);	
	}
	
}
