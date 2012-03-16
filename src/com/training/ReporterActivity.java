package com.training;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ReporterActivity extends Activity {
	
	final String LOG_TAG = "myLogs";
	
	static final private int ADD_USER = 0;
	static final private int WORK_WITH_REPORT = 1;
	
	/** Global variables */
	String login, password;
	
	Button btnLogin;
	EditText etLogin, etPassword;

	DBHelper dbHelper;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);

	    btnLogin = (Button) findViewById(R.id.btnLogin);

	    etLogin = (EditText) findViewById(R.id.etLogin);
	    etPassword = (EditText) findViewById(R.id.etPassword);
	    
	    // ������� ������ ��� �������� � ���������� �������� ��
	    dbHelper = new DBHelper(this);
	}
	
	// Implementation of the button click handler defined in main.xml
    public void btnLoginClickHandler(View target)
    {
	    
	    // �������� ������ �� ����� �����
	    login = etLogin.getText().toString();
	    password = etPassword.getText().toString();
	    
	    //��������� ����� �� ���������������� ������������
	    if(login.equals("newcomer") && password.equals("newcomer") ){
	    	
	    	// ������� ����� intent object � �������� AddNewUser class
	    	Intent adduser_question = new Intent(this, com.training.AddNewUser.class);
	    	
	    	// ��������� AddNewUser ��� ����� �������� � ���� ���������� 
	    	startActivityForResult(adduser_question, ADD_USER);
	    	
	    	//�������� ���� ����� � ������
	    	etLogin.setText("");
	    	etPassword.setText("");
	    }
	    else{
	    	//��������� ���������� �� ����� � ������
	    	boolean boolCorrLogin = false;
	    	// ������������ � �� ��� ������ ������
	        SQLiteDatabase db = dbHelper.getReadableDatabase();
	        //������ ������ ���� ������ �� ������� users, �������� Cursor 
	        Cursor c = db.query("users", null, null, null, null, null, null);
	        // ������ ������� ������� �� ������ ������ �������
	        // ���� � ������� ��� �����, �������� false
	        if (c.moveToFirst()) {
	        	// ���������� ������ �������� �� ����� � �������
	        	int loginColIndex = c.getColumnIndex("login");
	        	int passwordColIndex = c.getColumnIndex("password");
	        	String currLogin, currPassword;
	          
	        	do {
	        		// �������� �������� �� ������� �������� � ����� ��� ��������� ����������
	        		currLogin = c.getString(loginColIndex); 
	        		currPassword = c.getString(passwordColIndex);
	        		//��������� ��������� �� ����� � ������
	        		//���� �� �� ������� �� �����
	        		//���� ��, ���������� �����
	        		if (login.equals(currLogin) && password.equals(currPassword)) {
	        			boolCorrLogin = true;
	        			break;
	        		}
	        		// ������� �� ��������� ������ 
	        		// � ���� ��������� ��� (������� - ���������), �� false - ������� �� �����
	        	} while (c.moveToNext());
	        	// ��������� ����������� � ��
	    	    dbHelper.close();
	        	// ���� ����� � ������ ���������� �������� addNewReport
	        	// ����� ������� Toast
	        	if (boolCorrLogin) {
	        		
	        		// ������� ����� intent object � �������� WorkWithReport class
	    	    	Intent workwithreport_question = new Intent(this, com.training.WorkWithReport.class);
	    	    	
	    	    	// ���������� ����� � �������� WorkWithReport
	    	    	workwithreport_question.putExtra("login", login);
	    	    	
	    	    	// ��������� WorkWithReport ��� ����� �������� � ���� ��������� 
	    	    	startActivityForResult(workwithreport_question, WORK_WITH_REPORT);
	    	    	
	    	    	etLogin.setText("");
	    	    	etPassword.setText("");
	        		
	        	}
	        	else
	        		Toast.makeText(
							getApplicationContext(),
							"Error! Incorrect login or password." +
							"\nPlease try again.",
							Toast.LENGTH_LONG).show();
	        	
	        } else {
	        	Toast.makeText(
						getApplicationContext(),
						"Error! DB of user is empty." +
						"\nPlease registered a new user.",
						Toast.LENGTH_LONG).show();
	        	etLogin.setText("newcomer");
		    	etPassword.setText("newcomer");
	        }	
	    }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// Check which activity returned data to us
    	switch(requestCode)
    	{
    	case ADD_USER:
    		// only proceed if the result was RESULT_OK
    		if(resultCode == RESULT_OK)
    		{
    			String newLogin, newPassword;
    			// Extract the data from the returned object and store it
    			// into variables
    			newLogin = data.getExtras().getString("login");
    			newPassword = data.getExtras().getString("password");
    			//���� ��� ������ ������ ������������ ������ ������������
    	    	Log.d(LOG_TAG, "--- Insert in mytable: ---");
    	    	// ������� ������ ��� ������
    	    	ContentValues cv = new ContentValues();
    	    	// ������������ � ��
    		    SQLiteDatabase db = dbHelper.getWritableDatabase();
    	    	// ���������� ������ ��� ������� � ���� ���: ������������ ������� - ��������
    	    	cv.put("login", newLogin);
    	    	cv.put("password", newPassword);
    	    	// ��������� ������ � �������� �� ID
    	    	long rowID = db.insert("users", null, cv);
    	    	Log.d(LOG_TAG, "row inserted, ID = " + rowID);
    	    	// ��������� ����������� � ��
    		    dbHelper.close();
    		    // �������� ��� ������� ������
    		    Toast.makeText(
    					getApplicationContext(),
    					"Registration succecful!",
    					Toast.LENGTH_SHORT).show();
    		}
    		break;
    	case WORK_WITH_REPORT:
    		// only proceed if the result was RESULT_OK
    		if(resultCode == RESULT_OK)
    		{
    			Toast.makeText(
						getApplicationContext(),
						"Work with reportDB succesful!",
						Toast.LENGTH_LONG).show();
    		}
    		break;
    	default:
    		break;
    	}
    }
    
    // Implementation of the button click handler defined in main.xml
    public void btnReadClickHandler(View target)
    {
	    // ������������ � �� ��� ���������� ������
	    SQLiteDatabase db = dbHelper.getReadableDatabase();
	    
	    //�������� � �� �� _id
	    //db.delete("allreports", "_id = 1", null);
	    //������ �������
	    //db.delete("allreports", null, null);
	    
	    //--------------------------------
	    //������� ������ � ������� <users>
	    Log.d(LOG_TAG, "--- Rows in table <users>: ---");
    	// ������ ������ ���� ������ �� ������� users, �������� Cursor 
    	Cursor c = db.query("users", null, null, null, null, null, null);
    	// ������ ������� ������� �� ������ ������ �������
    	// ���� � ������� ��� �����, �������� false
    	if (c.moveToFirst()) {
    		// ���������� ������ �������� �� ����� � �������
    		int idColIndex = c.getColumnIndex("_id");
    		int loginColIndex = c.getColumnIndex("login");
    		int passwordColIndex = c.getColumnIndex("password");

    		do {
    			// �������� �������� �� ������� �������� � ����� ��� � ���
    			Log.d(LOG_TAG,
    					"ID = " + c.getInt(idColIndex) + 
    					", login = " + c.getString(loginColIndex) + 
    					", password = " + c.getString(passwordColIndex));
    			// ������� ����������� ������ 
    			// � ���� ��������� ��� (������� - ���������), �� false - ������� �� �����
    		} while (c.moveToNext());
    	} 
    	else
    		Log.d(LOG_TAG, "0 rows");
    	//-------------------------------------
	    //������� ������ � ������� <allreports>
    	Log.d(LOG_TAG, "--- Rows in table <allreports>: ---");
     	//������ ������ ���� ������ �� ������� allreports, �������� Cursor 
     	c = db.query("allreports", null, null, null, null, null, null);
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
     			// �������� �������� �� ������� �������� � ����� ��� � ���
     			Log.d(LOG_TAG,
     					"ID = " + c.getInt(idColIndex) + 
     					", login = " + c.getString(loginColIndex) +
     					"\n" +
     					"date = " + c.getString(dateColIndex) +
     					", typeofactivitiese = " + c.getString(typeOfActivitesColIndex) +
     					"\n" +
     					"nameofproject = " + c.getString(nameOfProjectColIndex) +
     					", info = " + c.getString(infoColIndex) +
     					", time = " + c.getString(timeColIndex));
     			// ������� ����������� ������ 
     			// � ���� ��������� ��� (������� - ���������), �� false - ������� �� �����
     		} while (c.moveToNext());
     	} 
     	else
     		Log.d(LOG_TAG, "0 rows");
     	// ��������� ����������� � ��
	    dbHelper.close();
    }
    
	
}