package com.training;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ShowResults extends Activity {
	
	private final String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
	
	// Global variables
	DBHelper dbHelper;
	String login;
	int intYear, intBeginDay, intEndDay;
	String month;
	String[] arrayTypeOfActivities = {"self development", "working time", "extra time", "team time"};
	
	String selection = null;
    String[] selectionArgs = null;
	
	EditText etShowResults;
	
	int[] hour = new int[4];
	int[] minute = new int[4];
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.showresults);
	    
	    etShowResults = (EditText) findViewById(R.id.etShowResults);
	    
	    // �������� ������ �� ��������
	    login = this.getIntent().getExtras().getString("login");
	    month = this.getIntent().getExtras().getString("month");
	    intYear = this.getIntent().getExtras().getInt("intYear");
	    intBeginDay = this.getIntent().getExtras().getInt("intBeginDay");
	    intEndDay = this.getIntent().getExtras().getInt("intEndDay");
	    
	    // ������� ������ ��� �������� � ���������� �������� ��
	    dbHelper = new DBHelper(this);
	    
	    // ������������ � �� ��� ���������� ������
	    SQLiteDatabase db = dbHelper.getReadableDatabase();
	    
	    Cursor c = null;
	    		
	    //������� ������ � ������� <allreports> for our login
    	etShowResults.append("-= Rows in table <allreports> for Login <" + login + ">  from  " +
    			intBeginDay + " - " + intEndDay +" " + month + " " + intYear + ":  =-\n\n");
    	
    	selection = "login = ?";
    	selectionArgs = new String[] { login };
     	//������ ������ ���� ������ �� ������� allreports, � ������� � ��������� �� ����, �������� Cursor 
     	c = db.query("allreports", null, selection, selectionArgs, null, null, "date");
     	
     	
     	// ������ ��� ������������ ��������� ���� � ������� �� ������� allreports
     	int beforeCurrDay = 0, beforeCurrYear = 0;
     	String beforeCurrMonth = "";
     	
     	
     	// ������ ������� ������� �� ������ ������ �������
     	// ���� � ������� ��� �����, �������� false
     	if (c.moveToFirst()) {
     		
     		// ���������� ������ �������� �� ����� � �������
     		int idColIndex = c.getColumnIndex("_id");
     		int loginColIndex = c.getColumnIndex("login");
     		int dateColIndex = c.getColumnIndex("date");
     		int typeOfActivitesColIndex = c.getColumnIndex("typeofactivities");
     		int nameOfProjectColIndex = c.getColumnIndex("nameofproject");
     		int infoColIndex = c.getColumnIndex("info");
     		int timeColIndex = c.getColumnIndex("time");
     		
     		do {
     			
     			// ������� ��� ���������� ������� ���� � ������ �� ������� allreports
         		int currDay = 0, currYear = 0;
         		String currMonth = "";
         		// c�������� ���� ��� ������
         		String day_month_year = c.getString(dateColIndex);
         		// ��������� ������ day_month_yea � �������� ����: �������� ����, �����, ���
         		int iStart = 0;
         		// ��������� ���� � ����� ��� � ���� ����:
         		if (day_month_year.charAt(1) == '-') {
         			currDay = Integer.parseInt(day_month_year.substring(0, 1));
         			iStart = 2;
         		}
         		if (day_month_year.charAt(2) == '-') {
         			currDay = Integer.parseInt(day_month_year.substring(0, 2));
         			iStart = 3;
         		}
         		boolean boolMonth = true;
         		String strTempYear = "";
         		// ����������� ������ day_month_year ������, ��������� ����� � ���
         		for(int i = iStart; i < day_month_year.length();  i++){
         			//��������� �� ����� �� ������
         			if (day_month_year.charAt(i) == '-'){
         				boolMonth = false;
         				continue;
         				}
         			if (boolMonth) currMonth += day_month_year.charAt(i);
         			else strTempYear += day_month_year.charAt(i);
         		};
         		// ���������� ���
         		currYear = Integer.parseInt(strTempYear);
         		
         		if ((currDay >= intBeginDay) && (currDay <= intEndDay) && (currMonth.equals(month)) && (currYear == intYear)){
         			// ��������� ����� �� �������� ����
         			if (!(currDay == beforeCurrDay)) etShowResults.append("\n===== Date: "
         					+ currDay + " " + currMonth + " " + currYear 
         					+ " =======================================\n");
         			// �������� �������� �� ������� �������� � ������� ������ �� ����� 
         			beforeCurrDay = currDay;
         			etShowResults.append(
         					"NAME OF PROJECT = " + c.getString(nameOfProjectColIndex)
         					+ ",     INFO = " + c.getString(infoColIndex) + "\n" +
         					"TYPE OF ACTIVITES = " + c.getString(typeOfActivitesColIndex)
         					+ ",     TIME = " + c.getString(timeColIndex) + "\n" + 
         					"----------------------------------------------------------------------------------\n");
         			// ������ ��� �������� ������ �������
         			String currTime;
         			int currHour = 0, currMinute = 0;
         			// �������� ������� �����, ���������� ��� � currHour, currMinute
         			currTime = c.getString(timeColIndex);
         			boolean boolHour = true;
         			String strTempHour = "", strTempMinute = "";
         			for(int i = 0; i < currTime.length(); i++){
         				if (currTime.charAt(i) == ':'){
         					boolHour = false;
         					continue;
         				}
         				if (boolHour) strTempHour += currTime.charAt(i);
         				else strTempMinute += currTime.charAt(i);
         			}
         			currHour = Integer.parseInt(strTempHour);
         			currMinute = Integer.parseInt(strTempMinute);
         			// ������� ����� typeOfActivites
         			String currTypeOfActivites = c.getString(typeOfActivitesColIndex);
         			if (currTypeOfActivites.equals(arrayTypeOfActivities[0])){
         				hour[0] += currHour;
         				minute[0] += currMinute;
         			}
         			if (currTypeOfActivites.equals(arrayTypeOfActivities[1])){
         				hour[1] += currHour;
         				minute[1] += currMinute;
         			}
         			if (currTypeOfActivites.equals(arrayTypeOfActivities[2])){
         				hour[2] += currHour;
         				minute[2] += currMinute;
         			}
         			if (currTypeOfActivites.equals(arrayTypeOfActivities[3])){
         				hour[3] += currHour;
         				minute[3] += currMinute;
         			}
         		}	
     		} while (c.moveToNext());
     		
     		// ��������� ��������� ����������� �������:
 			for (int i = 0; i < arrayTypeOfActivities.length; i++){
 				hour[i] += (int) (minute[i] / 60);
 				minute[i] = minute[i] % 60;			
 			}
 			// ������� ��������� �� ����� �������:
 			etShowResults.append("\n____________________________________________________________________________\n" +
 					"                                          TOTAL TIME:\n");
 			for (int i = 0; i < arrayTypeOfActivities.length; i++){
 				if (minute[i] > 9){
 					etShowResults.append("TYPE OF ACTIVITES = " + arrayTypeOfActivities[i] + " (" + hour[i] +":" + minute[i]+ ")\n");
 				}
 				else etShowResults.append("TYPE OF ACTIVITES = " + arrayTypeOfActivities[i] + " (" + hour[i] +":0" + minute[i]+ ")\n");
 			} 		
     	} 
     	else
     		etShowResults.append("0 rows");
     	dbHelper.close();
	}
	
	public void btnBackClickHandler(View v){
		
		//����������� ������ �������� ����� ������ ����� � EditText
		
		// Create an intent object to store the data
  		// we want to send back to the first main activity	 
  		Intent showresult_answer = new Intent();		
  		// Signal that we have indeed a valid return object
  		// by setting the activity result to RESULT_OK
  		setResult(RESULT_OK, showresult_answer);
  		// Finish this activity and return control to the
  		// calling activity (My Calendar in this case)
  		finish();
	}
}
