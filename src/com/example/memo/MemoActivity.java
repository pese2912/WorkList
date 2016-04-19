package com.example.memo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.example.memo.adapter.MemoListAdapter;
import com.example.memo.adapter.MemoListAdapter.clickListener;
import com.example.memo.util.MemoDBread;
import com.example.memo.util.MySQLiteOpenHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/*
   - 인텐트를 사용해 이동할 수 있는 것들이 많은데 그것들을 어떻게 쓸지 고민 해보고 내 앱에서는 어떠한 부분에서 인텐트가 필요할지 코드로 구현 해본다.
   
   		[명시적인텐트]
  		1. 메모액티비티에서는 메모내용을 클릭하여 디테일 액티비티(MemoDetailActivity)로 이동하기 위해 사용  
  		2. 이때 선택한 메모내용을 전달해야 하므로 intent에 값을 넣어서 보낸다.
  		   intent.putExtra("IDX", data.get(position).getId()); 메모내용의 아이디값 전달
  		3. 디테일 액티비티에서 다시 값을 찾는다
  			IDX = intent.getExtras().getInt("IDX", 0);
  			
  		[암시적 인텐트]
  		1. url를 이용해서 웹페이지를 띄운다  		
			intent = new Intent(Intent.ACTION_VIEW);
			Uri u = Uri.parse("http://www.google.co.kr");
			intent.setData(u);
			startActivity(intent);
		2. 전화앱을 띄운다
			intent = new Intent(Intent.ACTION_DIAL);
		    startActivity(intent);   
   
   
   - 내가 만들고 싶은 앱의 액티비티에서는 어떤 생명주기를 어떻게 쓸 수 있을 것인가 생각   		
    	MainActivity.onCreate()
    	-> Button에 리스너 등록 -> onResume() -> 대기
    	-> 버튼을 누를경우  OnClick이벤트 발생 -> 등록한 리스너 실행(OnClickListener)
    	-> 할일 추가 버튼 누른경우  메모액티비티 시작(인텐트 사용)
		-> MainActivity.onPause() -> MainActivity.onStop() 
    		-> MemoActivity.onCreate()
			 	-> 리스트뷰 초기화, DB 생성...
			-> onResume() -> 대기
			-> 메모 아이템을 터치한 경우
			-> MemoActivity.onPause() -> 
				-> MemoDetailActivity->onCreate() 
				-> DB불러오기, 해당 메모데이터 찾아서 출력
				-> 뒤로가기 누르면 onPause() -> .. -> onDestroy()
					-> DB 종료
					-> SQLiteDatabase인스턴스의 close() 메소드를 호출해 종료한다.
			-> MemoActivity.onRestart()-> 
				-> 리스트뷰 갱신
				-> DB에 있는 정보를 불러내어 리스트뷰에 표시한다
			-> 뒤로가기 누르면 MemoActvity.onPause() -> onDestroy()
				-> DB 종료
				-> SQLiteDatabase인스턴스의 close() 메소드를 호출해 종료한다.
		-> MainActiviy.onResume()->
		-> 대기
*/

public class MemoActivity extends Activity implements OnClickListener {
	
	ListView LvMemo; //저장된 메모리스트
	Button   BtnWrite; //메모쓰기 버튼
	MySQLiteOpenHelper dbhelper;	
	ArrayList<MemoDBread> data; //메모데이터 저장
	MemoListAdapter mAdapter; //메모리스트 어댑터 리스튜뷰와 메모데이터를 연결시켜주는 역할	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_memo);
				
		//DB 세팅
		init();
		//뷰세팅
		ViewSetting();		
		//리스트뷰 세팅
		listSetting();

	}
	
	public void init(){
		//데이터 베이스 생성 혹은 열기위해 필요한 객체를 생성
		dbhelper = new MySQLiteOpenHelper(getApplicationContext(),
				"MEMODB2.db", null, 1);
		
		//MEMODB 를 조회회 전체 데이터를 불러옴 
		// -1 전체 데이터 불러오기 값
		data = dbhelper.loadMemoData(-1);	
	}
	
	public void listSetting(){
		//메모내용및 등록일을 리스트뷰에 뿌려줄 Adapter
		                    //리스트뷰 R.layout.row_memo_list 레이아웃, 메모데이터, 클릭 리스너
		mAdapter = new MemoListAdapter( this,R.layout.row_memo_list, data,new clickListener() {
			
			@Override
			public void OnClick(int position, int idx) {				
				//MemoListAdapter에서 넘어온 position, 메모글 아이디
				//1. 수정, 2. 삭제, 3. 내용보기, 4. 체크
				if (idx == 1) {								
					//글수정 다이얼로그 호출   파라메터값 1. 수정,  3. 내용보기
					mDialogShow(1,data.get(position).getId()); 			
				}
				if (idx == 2) {								
					dbhelper.delete("DELETE FROM MEMODB2 WHERE _id =" +  data.get(position).getId());					
					data = dbhelper.loadMemoData(-1);
					listSetting();				
				}
				if (idx == 3) {
					//메모내용 클릭하여 디테일 페이지로 액티비티 이동
					Intent intent = new Intent(MemoActivity.this, MemoDetailActivity.class);	
					// 메모아이디를(data.get(position).getId()) MemoDetailActivity 로 넘겨줌 
					intent.putExtra("IDX", data.get(position).getId());
					//MemoDetailActivity 호출
					startActivity(intent);
				}
				if (idx == 4) {								
					int val = 0;
					if (data.get(position).getCheck() == 1) {
						val = 0;
					} else {
						val = 1;
					}
					//체크박스 업데이트
					dbhelper.insert("UPDATE MEMODB2 SET CK=" + "" + val + "" +" WHERE _id =" +  data.get(position).getId());				
					data = dbhelper.loadMemoData(-1);
					listSetting();				
				}
			}
		});
		
		// 리스트뷰 MemoListAdapter 연결
		LvMemo.setAdapter(mAdapter);	
	}
	
	public void ViewSetting(){
		//메모리스트
		LvMemo      = (ListView)findViewById(R.id.list_memo);		
		//메모쓰기 
		BtnWrite    = (Button)findViewById(R.id.btn_memo_write);
		
		//버튼 이벤트 등록
		//MainActivity 자신이 OnClickListener 를 구현(implements)했기 때문에 자기자신(this)를  인자로 넘겨줌 
		BtnWrite.setOnClickListener(this); 
	}

	
	public void mDialogShow(final int category, final int id){
		//메모쓰기
		//AlertDialog 는 다이얼로그 클래스를 상속받은 클래스
		//AlertDialog.Builder 클래스를 사용하여 생성자인 AlertDialog.Builder(this) 를 써서 alert 생성
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		if (category== 1) {
			alert.setTitle("메모수정"); //팝업창 타이틀
		} else {
			alert.setTitle("메모쓰기"); //팝업창 타이틀
		}
		final EditText input = new EditText(this); //EditText 추가
		alert.setView(input); //팝업창에 EditText ADD
		
		//OK 버튼
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				value.toString();
				
				if (category== 1) {
					dbhelper.insert("UPDATE MEMODB2 SET MEMOTEXT=" + "'" + value + "'" +" WHERE _id =" +  id);				
					data = dbhelper.loadMemoData(-1);
					// 리스트뷰에 갱신된 데이터를 뿌려준다
					listSetting();		
				} else {
					String Qry = null;
					//현재시간 지정된 형식 yyyy-MM-dd HH:mm:ss text로 변경
					long now = System.currentTimeMillis();
		            SimpleDateFormat nowTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		            String strNow = nowTimeFormat.format(new java.util.Date(now));		
					
					Qry = "(" +							
							"'"+ strNow + "'," +
							""+ 0 + "," +	
							"'"+ value + "'" +	
					")";	
					
					//메모데이터 입력
					dbhelper.insert("INSERT INTO MEMODB2 (DATE,CK, MEMOTEXT) VALUES " + Qry);
					
					//입력이 됐으니 갱신된 전체 데이터를 불러온다
					data = dbhelper.loadMemoData(-1);
					//그리고 리스트뷰에 갱신된 데이터를 뿌려준다
					listSetting();					
				}
				

			}
		});

		//Cancel 버튼
		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});
		
		alert.show(); 		
	}
	
	
	
	@Override
	public void onClick(View v) {
		// 이벤트 처리
		switch (v.getId()) {
		case R.id.btn_memo_write:
				//글쓰기 다이얼로그 호출 파라메터값 1. 수정,  3. 내용보기
				mDialogShow(3,0); 
			break;

		default:
			break;
		}
	}
	
	
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
     	// MemoActivity가 종료될 때
        // 사용한 데이터베이스를  SQLiteDatabase인스턴스의 close() 메소드를 호출해 연결을 해제한다.
        dbhelper.close();
        Toast.makeText(this, "MemoActivity onDestroy()", Toast.LENGTH_SHORT).show();
    }
 
    @Override
    protected void onPause() {
        super.onPause();
        //Toast.makeText(this, "MemoActivity onPause()", Toast.LENGTH_SHORT).show();
    }
 
    @Override
    protected void onRestart() {
        super.onRestart();
        //전체데이터 갱신
        data = dbhelper.loadMemoData(-1);
        //Toast.makeText(this, "MemoActivity onRestart()", Toast.LENGTH_SHORT).show();
    }
 
    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(this, "MemoActivity onResume()", Toast.LENGTH_SHORT).show();
    }
 
    @Override
    protected void onStart() {
        super.onStart();
        //Toast.makeText(this, "MemoActivity onStart()", Toast.LENGTH_SHORT).show();
    }
 
    @Override
    protected void onStop() {
        super.onStop();
        //Toast.makeText(this, "MemoActivity onStop()", Toast.LENGTH_SHORT).show();
    }

}
