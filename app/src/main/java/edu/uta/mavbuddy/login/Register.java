package edu.uta.mavbuddy.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import edu.uta.mavbuddy.R;
import edu.uta.mavbuddy.validation.ValidationRegister;

public class Register extends Activity {
	
	String Username;
	String Password;
	String EmailId;
	String pwdsalt = "*%S6ge#9nt";
	int Pwdhash;
	TextView error;
	EditText username;
	EditText password;
	EditText verifyPassword;
	EditText emailId;
	ValidationRegister validationRegister = new ValidationRegister();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		registerValidation();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	public void registerSuccess(View view)
	{
		username = (EditText) findViewById(R.id.username);
		password 		= (EditText) findViewById(R.id.password);
		verifyPassword = (EditText) findViewById(R.id.verifyPassword);
		emailId 		= (EditText) findViewById(R.id.emailId);
		
		
		final Intent intent = new Intent(Register.this,RegisterSuccess.class);
		
		if(validationRegister.isValidUsername(username,0) && validationRegister.isValidPassword(password, verifyPassword,0)
				&& validationRegister.isValidEmail(emailId,0) && validationRegister.isValidVerifyPassword(verifyPassword,password,0))
		{
			Username = username.getText().toString();
			Password = password.getText().toString();
			EmailId = emailId.getText().toString();
			
			// Check for unique username while registering
			ParseQuery<ParseObject> query = ParseQuery.getQuery("userDetails");
			query.whereEqualTo("username", Username);
			query.getFirstInBackground(new GetCallback<ParseObject>() {
				public void done(ParseObject object, ParseException e) {
					if ( object != null ) 
					{
						// Username found in database. Error!
						error = (TextView)findViewById(R.id.textView3); 
						error.setTextColor(Color.parseColor("#BC1212"));
						error.setText("Username already exists!");
					}
					else
					{
						// Username not taken. Register success!
						Password = Password + pwdsalt;
						Pwdhash = Password.hashCode();
						
						ParseObject userDetails = new ParseObject("userDetails");
						userDetails.put("username", Username);
						userDetails.put("password", Pwdhash);
						userDetails.put("emailId", EmailId);
						userDetails.put("forgot_password", 0);
						userDetails.saveInBackground();
						
						startActivity(intent);
					}
				}
			});
		}
		
	}  
	
	public void registerToHomeScreen(View view)
	{
		Intent intent = new Intent(Register.this,HomeScreen.class);
		startActivity(intent);
	}
	
	public void registerValidation(){
		
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		verifyPassword = (EditText) findViewById(R.id.verifyPassword);
		emailId = (EditText) findViewById(R.id.emailId);
		
		username.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				validationRegister.isValidUsername(username,0);
			}
		});
		
		password.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				validationRegister.isValidPassword(password,verifyPassword,0);
			}
		});
		
		verifyPassword.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				validationRegister.isValidPassword(verifyPassword,password,0);
			}
		});
		
		emailId.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				validationRegister.isValidEmail(emailId,0);
			}
		});
	}
}
