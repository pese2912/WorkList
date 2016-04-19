package com.example.memo.adapter;

import java.util.ArrayList;

import com.example.memo.R;
import com.example.memo.util.MemoDBread;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MemoListAdapter extends BaseAdapter{
		
	ArrayList<MemoDBread> data = new ArrayList<MemoDBread>();	
	private Context mContext;
	private int resId;
	private clickListener listener;
	LayoutInflater inflater;


	public MemoListAdapter(Context context, int res, ArrayList<MemoDBread> dic,clickListener listener) {	
		mContext = context;
		resId = res;
		data = dic;
		this.listener = listener;
		//안드로이드에서 inflate 를 사용하면 xml 에 씌여져 있는 view 의 정의를 실제 view 객체로 만드는 역할
		//inflate 를 사용하기 위해서는 우선 inflater 를 얻어옴
		inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// ListView의 뿌려질 한줄의 Row를 설정 
		View v = null;
		final int pos = position;
		if (v == null) {
			//xml 파일을 불러온다 row_memo_list
			v = inflater.inflate(resId, parent, false);
		}
		
		TextView txtMemo = (TextView)v.findViewById(R.id.txt_memo);//메모내용
		TextView txtDate = (TextView)v.findViewById(R.id.txt_date); //등록일
		Button btnmod = (Button)v.findViewById(R.id.btn_mod); //수정버튼
		Button btndel = (Button)v.findViewById(R.id.btn_del); //삭제버튼
		CheckBox btnCK = (CheckBox)v.findViewById(R.id.cbox); //체크버튼
			
		if(data.get(pos).getMEMO() !=null) {
			//data에 저장된 position 째의 값을 가져온다 
			txtMemo.setText(data.get(pos).getMEMO());
		}
			
		if(data.get(pos).getDATE() !=null)
			txtDate.setText(data.get(pos).getDATE());
		
		if(data.get(pos).getCheck() == 1) {
			btnCK.setChecked(true);
		} else {
			btnCK.setChecked(false);
		}
		
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
		
		//체크버튼 클릭이벤트
		btnCK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listener.OnClick(pos, 4);
			}
		});
		
		//내용 클릭이벤트
		RelativeLayout llBody = (RelativeLayout)v.findViewById(R.id.ll_row_memo_list);
		llBody.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listener.OnClick(pos, 3);
			}
		});
	
		//view 를 화면에 그린다
		return v;
	}

	public interface clickListener {
		//리스트뷰 클릭이벤트
		public void OnClick(int position, int idx);
	}

	@Override
	public int getCount() {
		// Adapter가 관리할 Data의 개수를 설정 합니다.
		return data.size();
	}
	
	public void getSelects(int Pos) {
		// TODO Auto-generated method stub		
	}

	@Override
	public Object getItem(int position) {
		// Adapter가 관리하는 Data의 Item 의 Position을 <객체> 형태로 얻어 옵니다.
		return null;
	}

	@Override
	public long getItemId(int position) {
		// Adapter가 관리하는 Data의 Item 의 position 값의 ID 를 얻어 옵니다.
		return data.size();
	}
}
