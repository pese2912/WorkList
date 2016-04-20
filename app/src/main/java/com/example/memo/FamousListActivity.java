package com.example.memo;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
/**
 * 
 * 명언 액티비티
 *
 */
public class FamousListActivity extends ListActivity{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.famous)));  
	}
}
