package com.training;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class WorkWithReport extends Activity {
	
	final String LOG_TAG = "myLogs";
	static final private int MY_CALENDAR = 2;
	
	//Global variable
	String login;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.workwithreports);
	    //получаем логин
	    login = this.getIntent().getExtras().getString("login");
	    TextView tv = (TextView) findViewById(R.id.tvWWR);
	    tv.setText("Hello,  " + login + "!  Now, you can work with your reports.");
	   
	}
	
	public void btnAddReportClickHandler(View v){
		
		// создаем новый intent object и вызываем MyCalendar class
    	Intent mycalendar_question = new Intent(this, com.training.MyCalendar.class);
    	
    	// отправляем логин в активити MyCalendar
    	mycalendar_question.putExtra("login", login);
    	
    	// запускаем MyCalendar как новый активити и ждем результаты 
    	startActivityForResult(mycalendar_question, MY_CALENDAR);
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// Check which activity returned data to us
    	switch(requestCode)
    	{
    	case MY_CALENDAR:
    		// only proceed if the result was RESULT_OK
    		if(resultCode == RESULT_OK)
    		{	
    		    // сообщаем что репорт создан
    		    Toast.makeText(
    					getApplicationContext(),
    					"Report create succecful!",
    					Toast.LENGTH_SHORT).show();
    		}
    		break;
    	default:
    		break;
    	}
    }
	
	public void btnLogoutClickHandler(View v){
		// Create an intent object to store the data
		// we want to send back to the first main activity	 
		Intent workwithreport_answer = new Intent();		
		// Signal that we have indeed a valid return object
		// by setting the activity result to RESULT_OK
		setResult(RESULT_OK, workwithreport_answer);
		// Finish this activity and return control to the
		// calling activity (ReporterActivity in this case)
		finish();
	}

}
