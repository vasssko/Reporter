package com.training;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ReporterActivity extends Activity {
	
	final String LOG_TAG = "myLogs";
	static final private int ADD_USER = 0;
	static final private int ADD_REPORT = 1;
	
	/** Global variables */
	
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
	    String login = etLogin.getText().toString();
	    String password = etPassword.getText().toString();
	    
	    //��������� ����� �� ���������������� ������������
	    if(login.equals("newcomer") && password.equals("newcomer") ){
	    	
	    	// ������� ����� intent object � �������� AddNewUser class
	    	Intent adduser_question = new Intent(this, com.training.AddNewUser.class);
	    	
	    	// ��������� AddNewUser ��� ����� �������� � ���� ��������� 
	    	startActivityForResult(adduser_question, ADD_USER);
	    	
	    	//�������� ���� ����� � ������
	    	etLogin.setText("");
	    	etPassword.setText("");
	    }
	    else{
	    	//��������� ���������� �� ����� � ������
	    	boolean boolCorrLogin = false;
	    	// ������������ � ��
	        SQLiteDatabase db = dbHelper.getWritableDatabase();
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
	        	// ���� ����� � ������ ���������� �������� addNewReport
	        	// ����� ������� Toast
	        	if (boolCorrLogin) {
	        		// ������� ����� intent object � �������� AddNewReport class
	    	    	Intent addreport_question = new Intent(this, com.training.AddNewReport.class);
	    	    	
	    	    	// ��������� AddNewReport ��� ����� �������� � ���� ��������� 
	    	    	startActivityForResult(addreport_question, ADD_REPORT);
	    	    	
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
    	case ADD_REPORT:
    		// only proceed if the result was RESULT_OK
    		if(resultCode == RESULT_OK)
    		{
    			
    		}
    		break;
    	default:
    		break;
    	}
    }
    
    // Implementation of the button click handler defined in main.xml
    public void btnReadClickHandler(View target)
    {
	    // ������������ � ��
	    SQLiteDatabase db = dbHelper.getWritableDatabase();
    	
	    Log.d(LOG_TAG, "--- Rows in mytable: ---");
    	// ������ ������ ���� ������ �� ������� mytable, �������� Cursor 
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
    }
    
	class DBHelper extends SQLiteOpenHelper {
		public DBHelper(Context context) {
			// ����������� �����������
			super(context, "reporterDB", null, 1);
	    }

	    @Override
	    public void onCreate(SQLiteDatabase db) {
	    	Log.d(LOG_TAG, "--- onCreate database ---");
	    	// ������� ������� � ������
	    	db.execSQL("create table users ("
	    			+ "_id integer primary key autoincrement," 
	    			+ "login text,"
	    			+ "password text" + ");");
	    }
	    	
	    @Override
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	    }
	}
}