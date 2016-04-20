package com.example.memo.adapter;

import java.util.ArrayList;

import com.example.memo.R;
import com.example.memo.adapter.MemoListAdapter.clickListener;
import com.example.memo.util.MemoDBread;
import com.example.memo.util.ScheduleDBread;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ScheduleListAdapter extends BaseAdapter {

	private ArrayList<ScheduleDBread> scheduleList;
	private Context mContext;
	private clickListener listener;
	
	public ScheduleListAdapter(Context context, ArrayList<ScheduleDBread> scheduleList, clickListener listener) {

		this.mContext = context;
		this.scheduleList = scheduleList;
		this.listener = listener;
		//안드로이드에서 inflate 를 사용하면 xml 에 씌여져 있는 view 의 정의를 실제 view 객체로 만드는 역할
		//inflate 를 사용하기 위해서는 우선 inflater 를 얻어옴
	}

	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return scheduleList.size();
	}

	@Override
	public ScheduleDBread getItem(int arg0) {
		// TODO Auto-generated method stub
		return scheduleList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// ListView의 뿌려질 한줄의 Row를 설정 
				View v = null;
				final int pos = position;
				if (v == null) {
					//xml 파일을 불러온다 row_memo_list

					LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					v = inflater.inflate(R.layout.row_schedule_list, parent, false);
				}
				
				TextView txtName = (TextView)v.findViewById(R.id.txt_name);//강의내용
				TextView txtLocation = (TextView)v.findViewById(R.id.txt_loation); //강의장쇼
				TextView txtTime = (TextView)v.findViewById(R.id.txt_time); //등록일
				Button btnmod = (Button)v.findViewById(R.id.btn_mod); //수정버튼
				Button btndel = (Button)v.findViewById(R.id.btn_del); //삭제버튼
					
				if(scheduleList.get(pos).getName() !=null) {
					//data에 저장된 position 째의 값을 가져온다 
					txtName.setText(scheduleList.get(pos).getName());
				}
					
				if(scheduleList.get(pos).getLoacation() !=null)
					txtLocation.setText(scheduleList.get(pos).getLoacation());
				
				txtTime.setText(scheduleList.get(pos).getDay()+" " + scheduleList.get(pos).getHourTime() +":"+scheduleList.get(pos).getMinuteTime());
				
				//수정버튼 클릭이벤트
				btnmod.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//메인엑티비티에서 넘어온 listener 다시 되돌려줌
						listener.OnClick(pos, 1);
					}
				});
					
				//삭제버튼 클릭이벤트
				btndel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						listener.OnClick(pos, 2);
					}
				});		
				
			
				//view 를 화면에 그린다
				return v;
	}

}
