package edu.uta.mavbuddy.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import edu.uta.mavbuddy.R;
import edu.uta.mavbuddy.validation.ValidationLogin;


public class HomeScreen extends Activity {

	private String Username;
	private String Password;
	private String pwdsalt = "*%S6ge#9nt";
	int Pwdhash;
	TextView myText;
	ValidationLogin validationLogin = new ValidationLogin();
	private EditText username;
	private EditText password;
	public static final String MyPREFERENCES = "MyPrefs" ;
	public static final String name = "usernameKey"; 
	public static final String pass = "passwordKey";
	SharedPreferences sharedPreferences;
	Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Parse.initialize(this, "NMTwPKAweEH0MrGH5UAIdE8nKC9mc3GkMlvCGALE", "I35KGgtQ2giCz0Ig3QqraJOiorDuxVhdKlUL4Jd5");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);


		loginValidation();
	}
	
	@Override
	protected void onResume() {
		sharedPreferences=getSharedPreferences(MyPREFERENCES, 
				Context.MODE_PRIVATE);
		
		if (sharedPreferences.contains(name))
		{
			if(sharedPreferences.contains(pass)){
				Intent i = new Intent(this,edu.uta.mavbuddy.login.UserHomePage.class);
				startActivity(i);
			}
		}
		super.onResume();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_screen, menu);

		return true;
	}

	public void login(final View view)
	{		
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);

		final Intent intent = new Intent(HomeScreen.this,UserHomePage.class);
		final Intent intent1 = new Intent(HomeScreen.this,ChangePassword.class);

		if(validationLogin.isValidUsername(username,0) && validationLogin.isValidPassword(password,0))
		{
			Username = username.getText().toString();
			Password = password.getText().toString();

			Password = Password + pwdsalt;
			Pwdhash = Password.hashCode();

			ParseQuery<ParseObject> query = ParseQuery.getQuery("userDetails");
			query.whereEqualTo("username", Username);
			try {
				ParseObject object = query.getFirst();
				// Valid username found
				int Orighash = object.getInt("password");
				if(Orighash == Pwdhash)
				{
					// Password matched for given username
					sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
					editor = sharedPreferences.edit();
					editor.putString(name, Username);
					editor.putString(pass, Password);
					editor.commit();
					
					if(object.getInt("forgot_password") == 1)
					{
						intent1.putExtra("username", Username);
						startActivity(intent1);
					}
					else
					{
						intent.putExtra("username", Username);
						startActivity(intent);	
					}					
				}
				else
				{
					// Invalid Password for given username
					TextView error = (TextView)findViewById(R.id.error); 
					error.setTextColor(Color.parseColor("#BC1212"));
					error.setText("Invalid Username/Password!");
				}
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				// Invalid username error
				TextView error = (TextView)findViewById(R.id.error); 
				error.setTextColor(Color.parseColor("#BC1212"));
				error.setText("Invalid Username/Password!"); 
			}
		}

	}

	public void openRegisterPage(View view)
	{
		Intent intent = new Intent(HomeScreen.this,Register.class);
		startActivity(intent);
	}

	public void loginValidation(){

		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);

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
				validationLogin.isValidUsername(username,0);
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
				validationLogin.isValidPassword(password,0);
			}
		});
	}

	public void forgotPassword(View view)
	{
		Intent intent = new Intent(HomeScreen.this,ResetPassword.class);
		startActivity(intent);
	}

}