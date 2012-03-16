package com.training;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class WorkWithReport extends Activity {
	
	final String LOG_TAG = "myLogs";
	static final private int MY_CALENDAR = 2;
	static final private int SHOW_RESULTS = 3;
	private final String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
	
	//Global variable
	String login;
	String month = months[2];
	
	EditText etYear,  etBeginDay,  etEndDay;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.workwithreports);
	    //получаем логин
	    login = this.getIntent().getExtras().getString("login");
	    TextView tv = (TextView) findViewById(R.id.tvWWR);
	    tv.setText("Hello,  " + login + "!  Now, you can work with your reports.");
	    
	    etYear = (EditText) findViewById(R.id.etYear);
	    etBeginDay = (EditText) findViewById(R.id.etBeginDay);
	    etEndDay = (EditText) findViewById(R.id.etEndDay);
	    
	    // адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, months);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        Spinner spinnerMonth = (Spinner) findViewById(R.id.SpinnerMonth);
        spinnerMonth.setAdapter(adapter);
        // выделяем элемент 
        spinnerMonth.setSelection(2);
        // устанавливаем обработчик нажатия
        spinnerMonth.setOnItemSelectedListener(new OnItemSelectedListener() {
        		@Override
        		public void onItemSelected(AdapterView<?> parent, View view,
        				int position, long id) {
        			// показываем позиция нажатого элемента
        			month = months[position];
        		}
        		@Override
        			public void onNothingSelected(AdapterView<?> arg0) {
        		}
        });
	   
	}
	
	public void btnAddReportClickHandler(View v){
		
		// создаем новый intent object и вызываем MyCalendar class
    	Intent mycalendar_question = new Intent(this, com.training.MyCalendar.class);
    	
    	// отправляем логин в активити MyCalendar
    	mycalendar_question.putExtra("login", login);
    	
    	// запускаем MyCalendar как новый активити и ждем результаты 
    	startActivityForResult(mycalendar_question, MY_CALENDAR);
	}
	
	public void btnShowResultsClickHandler(View v){
		
		// создаем новый intent object и вызываем ShowResults class
    	Intent showresults_question = new Intent(this, com.training.ShowResults.class);
    	
    	int intYear, intBeginDay, intEndDay;
    	
    	intYear = Integer.parseInt((String)etYear.getText().toString());
    	intBeginDay = Integer.parseInt(etBeginDay.getText().toString());
    	intEndDay = Integer.parseInt(etEndDay.getText().toString());
    	
    	// отправляем данные в активити ShowResults
    	showresults_question.putExtra("login", login);
    	showresults_question.putExtra("intYear", intYear);
    	showresults_question.putExtra("month", month);
    	showresults_question.putExtra("intBeginDay", intBeginDay);
    	showresults_question.putExtra("intEndDay", intEndDay);
    	
    	//запускаем ShowResults как новый активити и ждем результаты 
    	startActivityForResult(showresults_question, SHOW_RESULTS);
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
    	case SHOW_RESULTS:
    		// only proceed if the result was RESULT_OK
    		if(resultCode == RESULT_OK)
    		{	
    		    // сообщаем что все в порядке
    		    Toast.makeText(
    					getApplicationContext(),
    					"Consolidated report complete succecful!",
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
