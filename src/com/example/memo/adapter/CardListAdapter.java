package com.example.memo.adapter;

import java.util.ArrayList;

import com.example.memo.R;
import com.example.memo.item.CardRank;
import com.example.memo.item.Study;
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

public class CardListAdapter extends BaseAdapter{
		
	ArrayList<CardRank> data = new ArrayList<CardRank>();	
	private Context mContext;
	private int resId;
	private clickListener listener;
	LayoutInflater inflater;


	public CardListAdapter(Context context, int res, ArrayList<CardRank> dic,clickListener listener) {	
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
			v = inflater.inflate(R.layout.row_rank_list, parent, false);
		}
		
		TextView tvRank = (TextView)v.findViewById(R.id.tv_rank);//랭크
		TextView tvName = (TextView)v.findViewById(R.id.tv_name); //이름
		TextView tvValue = (TextView)v.findViewById(R.id.tv_value); //점수
		
		
			
		if(data.get(pos).getName() !=null) {
			// 
			tvName.setText(data.get(pos).getName());
		}
			
		
		tvValue.setText(data.get(pos).getValue()+"점");
	
		tvRank.setText((pos+1)+"위");
	
		
		
		
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
