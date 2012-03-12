package com.training;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNewUser extends Activity {
	
	EditText etLogin, etPassword, etRepeatPassword;
	Button registration;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.addnewuser);
	    
	    etLogin = (EditText) findViewById(R.id.etLoginReg);
	    etPassword = (EditText) findViewById(R.id.etPasswordReg);
	    etRepeatPassword = (EditText) findViewById(R.id.etRepeatPasswordReg);
	}
	
	public void btnRegistrationClickHandler(View v){
		
		// Create an intent object to store the data
		// we want to send back to the first main activity	    
		
		Intent adduser_answer = new Intent();
		
		// получаем данные из полей ввода
	    String login = etLogin.getText().toString();
	    String password = etPassword.getText().toString();
	    String repeatpassword = etRepeatPassword.getText().toString();
	    
		// проверяем, совпадают ли пароли
		if (password.equals(repeatpassword)){
			//отсилаем данные, чтобы зарегестрировать нового пользователя
			adduser_answer.putExtra("login", login);
			adduser_answer.putExtra("password", password);
			
			// Signal that we have indeed a valid return object
			// by setting the activity result to RESULT_OK
			setResult(RESULT_OK, adduser_answer);
			
			// Finish this activity and return control to the
			// calling activity (Episode11 in this case)
			finish();
		}
		else{
			Toast.makeText(
					getApplicationContext(),
					"Error! Passwords are not equal." +
					"\nCheck the input data and try again.",
					Toast.LENGTH_LONG).show();
		}
	}
}
