package com.example.memo.util;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

//MySQLiteOpenHelper 클래스를 생성 (SQLiteOpenHelper 상속받아서)
//데이터베이스를 생성하고 오픈하려면 SQLiteOpenHelper 객체를 사용한다.
public class MySQLiteOpenHelper extends SQLiteOpenHelper {
	
	//DB관련 변수선언
	private String            DBName       = "";  //DB명
	private String            DBPath       = "";  //DB경로
	
	//DB관련 객체선언
	private SQLiteDatabase    db           = null;
	
	public MySQLiteOpenHelper(Context context, //현재화면 content 어느 액티비티에서넘어왔는지 
			String name, //파일명
			CursorFactory factory, //커서팩토리 
			int version) { //버전번호
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
					
        this.DBName = name; //DB명
        this.DBPath = DBPath + "/Data"; //DB파일 저장 경로
              
        //sd 카드의 사용상태를 얻어옴
        String SdCart_state = Environment.getExternalStorageState();     
        
        
        try
        {
        	 //MEDIA_MOUNTED SD카드가 존재하고 연결이 되었으며 읽고 쓰기가 가능한 상태라면
        	 if (Environment.MEDIA_MOUNTED.equals(SdCart_state))
             {
        		    //외부 저장소(일반적으로 SD카드)의 최상위 경로를 가져옵니다
        		 	File sdCard = Environment.getExternalStorageDirectory();
                    File Path = new File(sdCard.getAbsolutePath() + File.separator + this.DBPath);
                    //해당 경로에 폴더가 존재하지 않다면 
                    if (!Path.exists())
                    {
                        //폴더 생성  
                    	Path.mkdirs();
                    }
                    
                    // MEMODB.DB 데이터베이스 파일 생성
                    db = context.openOrCreateDatabase(Path.getAbsolutePath() + File.separator + this.DBName, Activity.MODE_PRIVATE, null);
                	} else
                	{
                	db = context.openOrCreateDatabase(this.DBName, Activity.MODE_PRIVATE, null);
            }

        
        	//MEMODB 테이블이 존재하지 않다면 
    		if (!ExistTable("MEMODB2")) {
    			// 테이블 생성
    			String QUERY_CREATE_TABLE = 
    	            "CREATE TABLE MEMODB2( " +
    	             "_id INTEGER PRIMARY KEY AUTOINCREMENT,"+	  //AUTOINCREMENT 자동증가값을 의미함             	            
    	             "DATE	     TEXT,"+   //등록일
    	             "CK	     INTEGER DEFAULT 0,"+   //체크여부
    	             "MEMOTEXT   TEXT);";  //메모내용
    		
    			db.execSQL(QUERY_CREATE_TABLE);		
    		}
        } catch (Exception e)
        {
            // TODO: handle exception
        }		

		Log.v("DBHelper", "Constructure excuted!!!");
	}


	@Override
	// 최초 DB를 만들때 한번만 호출된다.
	public void onCreate(SQLiteDatabase db) {
		//MEMODB 테이블이 존재하지 않다면 
		
			// 테이블 생성
			String QUERY_MEMO_CREATE_TABLE = 
	            "CREATE TABLE MEMODB2( " +
	             "_id INTEGER PRIMARY KEY AUTOINCREMENT,"+	             	            
	             "DATE	     TEXT,"+   
	             "CK	     INTEGER DEFAULT 0,"+   //체크여부
	             "MEMOTEXT   TEXT);";
		
			db.execSQL(QUERY_MEMO_CREATE_TABLE);		
			
			String QUERY_SCHEDULE_CREATE_TABLE = 
		            "CREATE TABLE SCHEDULEDB( " +
		             "_id INTEGER PRIMARY KEY AUTOINCREMENT,"+	             	            
		             "NAME	     TEXT,"+   
		             "LOCATION	 TEXT,"+
		             "DAY TEXT,"+	
		             "HOUR   INTEGER," +
		             "MINUTE   INTEGER);";
			
			db.execSQL(QUERY_SCHEDULE_CREATE_TABLE);		
				
		Log.v("DBHelper", "onCreate excuted!!!");
	}
	
    
	private boolean ExistTable(String TableName)
    {
		//테이블 생성 확인 함수
        db = getReadableDatabase();
    	Cursor cursor = null;
        try
        {
        	//테이블 존재 여부 확인 쿼리
        	cursor = db.rawQuery(String.format("SELECT name FROM sqlite_master WHERE type='table' and name = '%s' ORDER BY name;", TableName), null);
            return cursor.moveToNext();
        } catch (Exception e)
        {
            return false;
        } finally
        {
        	if (cursor != null)
                cursor.close();
            cursor = null;
        }
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// 데이터베이스가 업그레이드 될때 호출되는 메서든
		Log.v("DBHelper", "onUpgrade excuted!!!");
	}

    public void insert(String _query) {
        //데이터 입력
    	db = getWritableDatabase();
    	// sql 실행하는 메서드
    	db.execSQL(_query);
    	// sql 사용후 종료
    	db.close();     
    }

    public void delete(String _query) {
        //데이터 삭제
    	db = getWritableDatabase();
    	// sql 실행하는 메서드
    	db.execSQL(_query);
    	// sql 사용후 종료
    	db.close();     
    }



    //DB에서 데이터 읽어오기
	public ArrayList<MemoDBread> loadMemoData(int idx) {
		ArrayList<MemoDBread> data = new ArrayList<MemoDBread>();
	    db = getReadableDatabase();
		
	    //DB에서 값을 받아오는 Cursor 데이터를 받을땐 커서로 받는다
	    Cursor cursor;
	    if (idx == -1) {
	    	 //-1 일경우 데이터 전체조회
	    	 cursor = db.rawQuery("select _id, DATE, MEMOTEXT, CK from MEMODB2", null);	
	    } else {
	    	 // 액티비티로부터 넘어온 메모아이디를 조회하여 보여준다 메모 내용 볼경우  
	    	 cursor = db.rawQuery("select _id, DATE, MEMOTEXT, CK from MEMODB2 WHERE _id = " + idx, null);
	    }
	    	    
		while (cursor.moveToNext()) {
			//cursor 가져온 데이터의 끝까지 읽은후 
			
			//MemoDBread 클래스에 넣어준다
			MemoDBread temp = new MemoDBread(
					cursor.getInt(0), //메모 id	
					cursor.getString(1), //메모내용
					cursor.getString(2),  //등록일
					cursor.getInt(3) //체크여부
					);
			data.add(temp);
		}
		db.close();
		
		Log.v("DBHelper", "loadData excuted!!!");
		return data;
	}
	public ArrayList<ScheduleDBread> loadScheduleData(int idx) {
		ArrayList<ScheduleDBread> data = new ArrayList<ScheduleDBread>();
	    db = getReadableDatabase();
		
	    //DB에서 값을 받아오는 Cursor 데이터를 받을땐 커서로 받는다
	    Cursor cursor;
	    if (idx == -1) {
	    	 //-1 일경우 데이터 전체조회
	    	 cursor = db.rawQuery("select _id, NAME, LOCATION, DAY, HOUR, MINUTE from SCHEDULEDB", null);	
	    } else {
	    	 // 액티비티로부터 넘어온 메모아이디를 조회하여 보여준다 메모 내용 볼경우  
	    	 cursor = db.rawQuery("select _id, NAME, LOCATION, DAY, HOUR, MINUTE from SCHEDULEDB WHERE _id = " + idx, null);
	    }
	    	    
		while (cursor.moveToNext()) {
			//cursor 가져온 데이터의 끝까지 읽은후 
			
			//ScheduleDBread 클래스에 넣어준다
			ScheduleDBread temp = new ScheduleDBread(
					cursor.getInt(0), // 시간표 id	
					cursor.getString(1), // 강의이름
					cursor.getString(2),  // 장소
					cursor.getString(3), // 요일
					cursor.getInt(4), // 시간 : 시
					cursor.getInt(5) // 시간 : 분
					);
			data.add(temp);
		} 
		db.close();
		
		Log.v("DBHelper", "loadData excuted!!!");
		return data;
	}


	
}
