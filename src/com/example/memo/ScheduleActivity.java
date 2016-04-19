package com.example.memo;

import java.util.ArrayList;

import com.example.memo.adapter.MemoListAdapter.clickListener;
import com.example.memo.adapter.ScheduleListAdapter;
import com.example.memo.util.MySQLiteOpenHelper;
import com.example.memo.util.ScheduleDBread;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;

/**
 * 시간표 액티비티
 *   리스트뷰에 시간표아이템들을 보여준다.
 */
public class ScheduleActivity extends Activity implements android.view.View.OnClickListener{
	
	ListView listSchedule; 		// 시간표 리스트뷰
	Button   BtnAddSchedule; 	// 시간표추가 버튼
	ScheduleListAdapter mAdapter; // 시간표리스트 어댑터 리스튜뷰와 시간표데이터를 연결시켜주는 역할	
	ArrayList<ScheduleDBread> mListData;	// 시간표데이터를 어댑터와 리스트뷰에 제공하기위한 공간  
	MySQLiteOpenHelper dbhelper;	// 시간표데이터가 저장되는 DB 헬퍼
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);
		listSchedule = (ListView)findViewById(R.id.list_schedule);
		BtnAddSchedule = (Button)findViewById(R.id.btn_schedule_add);
		BtnAddSchedule.setOnClickListener(this);
		mListData = new ArrayList<ScheduleDBread>();
		
		loadDataFromDB(); // DB에서 시간표 listdata를 얻기
		listSetting();	// 리스트뷰 관련 초기화
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbhelper.close(); // 사용한 DB 연결해제
	}
	@Override
	public void onClick(View v) {
		if(v == BtnAddSchedule){
			showScheduleDialog(false, 0,0); // 시간표 추가 다이얼로그 보여주기
		}
	}
	private void loadDataFromDB(){

		//데이터 베이스 생성 혹은 열기위해 필요한 객체를 생성
		dbhelper = new MySQLiteOpenHelper(getApplicationContext(),
				"MEMODB2.db", null, 1);

		// 시간표 전체 데이터를 얻어온다
		mListData = dbhelper.loadScheduleData(-1);	

	}
	public void listSetting(){
		//시간표 리스트어댑터를 초기화한다
		//시간표 리스트어댑터 생성자 (context, 시간표데이터, 클릭 리스너)
		mAdapter = new ScheduleListAdapter( this, mListData,new clickListener() {
			
			@Override
			public void OnClick(int position, int idx) {				
				//ScheduleListAdapter에서 넘어온 position, 시간표 아이디
				//1. 수정, 2. 삭제
				if (idx == 1) {								
					//시간표 수정 다이얼로그 호출   파라메터값 ( 수정, 해당 시간표 ID )
					showScheduleDialog(true,mListData.get(position).getId(), position); 			
				}
				if (idx == 2) {	// DB에서 삭제							
					dbhelper.delete("DELETE FROM SCHEDULEDB WHERE _id =" +  mListData.get(position).getId());					
					mListData = dbhelper.loadScheduleData(-1); // DB에서 전체 데이터 받기
					listSetting();		// 리스트뷰 관련 초기화
				}
			}
		});
		
		// 리스트뷰 ListAdapter 연결
		listSchedule.setAdapter(mAdapter);	
	}
	@SuppressLint("NewApi")
	public void showScheduleDialog(final boolean bModifymode, final int id, int position){
		
		AlertDialog.Builder builder= new AlertDialog.Builder(this); //다이얼로그 생성
		LayoutInflater inflater=getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.dialog_schedule_add, null); // XML 파일을 이용해서 뷰를 만들어 가져오기
		final Spinner spinnerDay = (Spinner)dialogView.findViewById(R.id.spinner_day); // 요일 스피너
		final EditText editName = (EditText)dialogView.findViewById(R.id.edit_name);	// 강의이름
		final EditText editLocation = (EditText)dialogView.findViewById(R.id.edit_location); // 강의실
		final TimePicker timepicker = (TimePicker)dialogView.findViewById(R.id.timePicker); // 시간
		String strTitle;
		String strBtnOK;
		if(bModifymode){ // 시간표 수정모드 일 경우
			strTitle = "시간표 수정";
			strBtnOK = "수정";
			// 기존 데이터를 다이얼로그에 초기화
			editName.setText(mAdapter.getItem(position).getName());
			editLocation.setText(mAdapter.getItem(position).getLoacation());
			if(mAdapter.getItem(position).getDay().equals("월")){
				spinnerDay.setSelection(0);
			}else if(mAdapter.getItem(position).getDay().equals("화")){
				spinnerDay.setSelection(1);
			}else if(mAdapter.getItem(position).getDay().equals("수")){
				spinnerDay.setSelection(2);
			}else if(mAdapter.getItem(position).getDay().equals("목")){
				spinnerDay.setSelection(3);
			}else if(mAdapter.getItem(position).getDay().equals("금")){
				spinnerDay.setSelection(4);
			}else if(mAdapter.getItem(position).getDay().equals("토")){
				spinnerDay.setSelection(5);
			}else{
				spinnerDay.setSelection(6);
			}
			if(android.os.Build.VERSION.SDK_INT >= 23){ // 안드로이드 sdk 버전에 따라 맞는 시간 get 함수 사용 
				timepicker.setHour(mAdapter.getItem(position).getHourTime());
				timepicker.setMinute(mAdapter.getItem(position).getMinuteTime());
			}else{
				timepicker.setCurrentHour(mAdapter.getItem(position).getHourTime());
				timepicker.setCurrentMinute(mAdapter.getItem(position).getMinuteTime());
			}
		}else{ // 추가모드일 경우
			strTitle = "시간표 추가";
			strBtnOK = "추가";
		}
		builder.setTitle(strTitle); //Dialog 제목						  
		builder.setView(dialogView); //위에서 inflater가 만든 dialogView 객체 세팅 (Customize)
		builder.setPositiveButton(strBtnOK, new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(DialogInterface dialog, int which) {			
				String strName = editName.getText().toString(); // 강의이름
				String strLocation = editLocation.getText().toString();//강의실
				String strDay = (String)spinnerDay.getSelectedItem();//요일
				int hour;
				int minute;
				if(android.os.Build.VERSION.SDK_INT >= 23){ // 안드로이드 sdk 버전에 따라 맞는 시간 get 함수 사용
					hour = timepicker.getHour();
					minute = timepicker.getMinute();							
				}else{

					hour = timepicker.getCurrentHour();
					minute = timepicker.getCurrentMinute();
				}				
				
				if(bModifymode){ // 수정모드일때 DB 업데이트
					dbhelper.insert("UPDATE SCHEDULEDB SET NAME=" + "'" + strName + "', "
							+ "LOCATION='" 	+ strLocation + "', "
							+ "DAY='" 	+ strDay + "', "
							+ "HOUR=" 		+ hour + ", "
							+ "MINUTE="		+ minute
							+ " WHERE _id =" +  id);				
					mListData = dbhelper.loadScheduleData(-1);
					// 리스트뷰에 갱신된 데이터를 뿌려준다
					listSetting();		
				}else{// 추가모드일때
					
					if(strName.equals("")){ // 강의이름이 빈칸이면 취소 
						return;
					}
					if(strLocation.equals("")){
						return;
					}
					String Qry = null;
					
					Qry = "(" +							
							"'"+ strName + "'," +
							"'"+ strLocation + "',"+							
							"'"+ strDay + "',"+
							hour + ","+ 
							minute +	
					")";	
					
					//시간표데이터 추가
					dbhelper.insert("INSERT INTO SCHEDULEDB (NAME,LOCATION,DAY,HOUR,MINUTE) VALUES " + Qry);
					
					//변경된 전체 시간표 데이터를 불러온다
					mListData = dbhelper.loadScheduleData(-1);
					//그리고 리스트뷰에 갱신된 데이터를 뿌려준다
					listSetting();	
					
				}
			}		
		});
		builder.setNegativeButton("취소", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 추가 or 수정 취소
			}
		});
		
		builder.show();
	}
	
}
