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
	    
	    // создаем объект для создания и управления версиями БД
	    dbHelper = new DBHelper(this);
	}
	
	// Implementation of the button click handler defined in main.xml
    public void btnLoginClickHandler(View target)
    {
	    
	    // получаем данные из полей ввода
	    login = etLogin.getText().toString();
	    password = etPassword.getText().toString();
	    
	    //проверяем нужно ли зарегестрировать пользователя
	    if(login.equals("newcomer") && password.equals("newcomer") ){
	    	
	    	// создаем новый intent object и вызываем AddNewUser class
	    	Intent adduser_question = new Intent(this, com.training.AddNewUser.class);
	    	
	    	// запускаем AddNewUser как новый активити и ждем результаты 
	    	startActivityForResult(adduser_question, ADD_USER);
	    	
	    	//зануляем поля логин и пароль
	    	etLogin.setText("");
	    	etPassword.setText("");
	    }
	    else{
	    	//проверяем корректные ли логин и пароль
	    	boolean boolCorrLogin = false;
	    	// подключаемся к БД для чтения данных
	        SQLiteDatabase db = dbHelper.getReadableDatabase();
	        //делаем запрос всех данных из таблицы users, получаем Cursor 
	        Cursor c = db.query("users", null, null, null, null, null, null);
	        // ставим позицию курсора на первую строку выборки
	        // если в выборке нет строк, вернется false
	        if (c.moveToFirst()) {
	        	// определяем номера столбцов по имени в выборке
	        	int loginColIndex = c.getColumnIndex("login");
	        	int passwordColIndex = c.getColumnIndex("password");
	        	String currLogin, currPassword;
	          
	        	do {
	        		// получаем значения по номерам столбцов и пишем все локальные переменные
	        		currLogin = c.getString(loginColIndex); 
	        		currPassword = c.getString(passwordColIndex);
	        		//проверяем совпадают ли логин и пароль
	        		//если да то выходим из цикла
	        		//если не, продолжаем поиск
	        		if (login.equals(currLogin) && password.equals(currPassword)) {
	        			boolCorrLogin = true;
	        			break;
	        		}
	        		// переход на следующую строку 
	        		// а если следующей нет (текущая - последняя), то false - выходим из цикла
	        	} while (c.moveToNext());
	        	// закрываем подключение к БД
	    	    dbHelper.close();
	        	// если логин и пароль корректные стартуем addNewReport
	        	// иначе виводим Toast
	        	if (boolCorrLogin) {
	        		
	        		// создаем новый intent object и вызываем WorkWithReport class
	    	    	Intent workwithreport_question = new Intent(this, com.training.WorkWithReport.class);
	    	    	
	    	    	// отправляем логин в активити WorkWithReport
	    	    	workwithreport_question.putExtra("login", login);
	    	    	
	    	    	// запускаем WorkWithReport как новый активити и ждем резульаты 
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
    			//если все прошло удачно регистрируем нового пользователя
    	    	Log.d(LOG_TAG, "--- Insert in mytable: ---");
    	    	// создаем объект для данных
    	    	ContentValues cv = new ContentValues();
    	    	// подключаемся к БД
    		    SQLiteDatabase db = dbHelper.getWritableDatabase();
    	    	// подготовим данные для вставки в виде пар: наименование столбца - значение
    	    	cv.put("login", newLogin);
    	    	cv.put("password", newPassword);
    	    	// вставляем запись и получаем ее ID
    	    	long rowID = db.insert("users", null, cv);
    	    	Log.d(LOG_TAG, "row inserted, ID = " + rowID);
    	    	// закрываем подключение к БД
    		    dbHelper.close();
    		    // сообщаем что аккаунт создан
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
	    // подключаемся к БД для считивания данных
	    SQLiteDatabase db = dbHelper.getReadableDatabase();
	    
	    //Удаление з БД по _id
	    //db.delete("allreports", "_id = 1", null);
	    //Чистим таблицу
	    //db.delete("allreports", null, null);
	    
	    //--------------------------------
	    //выводим данные с таблицы <users>
	    Log.d(LOG_TAG, "--- Rows in table <users>: ---");
    	// делаем запрос всех данных из таблицы users, получаем Cursor 
    	Cursor c = db.query("users", null, null, null, null, null, null);
    	// ставим позицию курсора на первую строку выборки
    	// если в выборке нет строк, вернется false
    	if (c.moveToFirst()) {
    		// определяем номера столбцов по имени в выборке
    		int idColIndex = c.getColumnIndex("_id");
    		int loginColIndex = c.getColumnIndex("login");
    		int passwordColIndex = c.getColumnIndex("password");

    		do {
    			// получаем значения по номерам столбцов и пишем все в лог
    			Log.d(LOG_TAG,
    					"ID = " + c.getInt(idColIndex) + 
    					", login = " + c.getString(loginColIndex) + 
    					", password = " + c.getString(passwordColIndex));
    			// переход наследующую строку 
    			// а если следующей нет (текущая - последняя), то false - выходим из цикла
    		} while (c.moveToNext());
    	} 
    	else
    		Log.d(LOG_TAG, "0 rows");
    	//-------------------------------------
	    //выводим данные с таблицы <allreports>
    	Log.d(LOG_TAG, "--- Rows in table <allreports>: ---");
     	//делаем запрос всех данных из таблицы allreports, получаем Cursor 
     	c = db.query("allreports", null, null, null, null, null, null);
     	// ставим позицию курсора на первую строку выборки
     	// если в выборке нет строк, вернется false
     	if (c.moveToFirst()) {
     		// определяем номера столбцов по имени в выборке
     		int idColIndex = c.getColumnIndex("_id");
     		int loginColIndex = c.getColumnIndex("login");
     		int dateColIndex = c.getColumnIndex("date");
     		int typeOfActivitesColIndex = c.getColumnIndex("typeofactivities");
     		int nameOfProjectColIndex = c.getColumnIndex("nameofproject");
     		int infoColIndex = c.getColumnIndex("info");
     		int timeColIndex = c.getColumnIndex("time");

     		do {
     			// получаем значения по номерам столбцов и пишем все в лог
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
     			// переход наследующую строку 
     			// а если следующей нет (текущая - последняя), то false - выходим из цикла
     		} while (c.moveToNext());
     	} 
     	else
     		Log.d(LOG_TAG, "0 rows");
     	// закрываем подключение к БД
	    dbHelper.close();
    }
    
	
}