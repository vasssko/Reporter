package com.training;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddNewReport extends Activity {
	
	final String LOG_TAG = "myLogs";
	
	// Global variables
	DBHelper dbHelper;
	String login;
	String date_month_year;
	String[] arrayTypeOfActivities = {"self development", "working time", "extra time", "team time"};
	String typeOfActivities = arrayTypeOfActivities[0];
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.addnewreport);
	    // �������� �����
	    login = this.getIntent().getExtras().getString("login");
	    date_month_year = this.getIntent().getExtras().getString("date");
	    
	    // ������� ������ ��� �������� � ���������� �������� ��
	    dbHelper = new DBHelper(this);
	    
	    // �������
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayTypeOfActivities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        Spinner spinnerTypeOfActivities = (Spinner) findViewById(R.id.SpinnerTypeOfActivities);
        spinnerTypeOfActivities.setAdapter(adapter);
        // �������� ������� 
        spinnerTypeOfActivities.setSelection(0);
        // ������������� ���������� �������
        spinnerTypeOfActivities.setOnItemSelectedListener(new OnItemSelectedListener() {
        		@Override
        		public void onItemSelected(AdapterView<?> parent, View view,
        				int position, long id) {
        			// ���������� ������� �������� ��������
        			typeOfActivities = arrayTypeOfActivities[position];
        		}
        		@Override
        			public void onNothingSelected(AdapterView<?> arg0) {
        		}
        });
	}
	
	public void btnSaveReportClickHandler(View v){
		
		// ADD NEW REPORT
		ContentValues cv = new ContentValues();
		// ������������ � ��
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		// ���������� ������ ��� ������� � ���� ���: ������������ ������� - ��������
		String nameOfProject, info, time;
		
		// ��������� ������ �������
		
		EditText etNameOfProject = (EditText) findViewById(R.id.edNameOfProject);
		nameOfProject = etNameOfProject.getText().toString();
		
		EditText etInfo = (EditText) findViewById(R.id.edInfo);
		info = etInfo.getText().toString();
		
		EditText etTime = (EditText) findViewById(R.id.edTime);
		time = etTime.getText().toString();
		
		cv.put("login", login);
		cv.put("date", date_month_year);
		cv.put("typeofactivities", typeOfActivities);
		cv.put("nameofproject", nameOfProject);
		cv.put("info", info);
		cv.put("time", time);
		
		// ��������� ������ � �������� �� ID
		long rowID = db.insert("allreports", null, cv);
		Log.d(LOG_TAG, "row inserted in allreports, ID = " + rowID);
		// ��������� ����������� � ��
		dbHelper.close();
		
		//����������� ������ �������� ����� ���������� reporta � ��
		
		// Create an intent object to store the data
		// we want to send back to the first main activity	 
		Intent addnewreport_answer = new Intent();		
		// Signal that we have indeed a valid return object
		// by setting the activity result to RESULT_OK
		setResult(RESULT_OK, addnewreport_answer);
		// Finish this activity and return control to the
		// calling activity (My Calendar in this case)
		finish();
	}
	
}
