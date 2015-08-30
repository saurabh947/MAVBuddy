package edu.uta.mavbuddy.login;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import edu.uta.mavbuddy.R;
import edu.uta.mavbuddy.validation.ValidationRegister;

public class ChangePassword extends Activity {

	EditText oldPass;
	EditText newPass;
	EditText confirmNewPass;
	String OldPass;
	String NewPass;
	String ConfirmNewPass;
	int DbPass;
	int Pwdhash;
	int NewPwdhash;
	TextView error;
	private String pwdsalt = "*%S6ge#9nt";
	ValidationRegister validationRegister = new ValidationRegister();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.change_password, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	public void changePass(View view) 
	{
		Intent intent = getIntent();
		final String username = intent.getStringExtra("username");

		final Intent intent1 = new Intent(ChangePassword.this, UserHomePage.class);

		oldPass = (EditText) findViewById(R.id.editText1);
		newPass = (EditText) findViewById(R.id.editText2);
		confirmNewPass = (EditText) findViewById(R.id.editText3);

		if(validationRegister.isValidPassword(newPass, confirmNewPass,0) && 
				validationRegister.isValidVerifyPassword(confirmNewPass,newPass,0))
		{				
			OldPass = oldPass.getText().toString();
			NewPass = newPass.getText().toString();
			ConfirmNewPass = confirmNewPass.getText().toString();

			ParseQuery<ParseObject> query = ParseQuery.getQuery("userDetails");
			query.whereEqualTo("username", username);
			query.getFirstInBackground(new GetCallback<ParseObject>() {
				public void done(ParseObject object, ParseException e) {
					if ( object != null ) 
					{						
						DbPass = object.getInt("password");
						OldPass = OldPass + pwdsalt;
						Pwdhash = OldPass.hashCode();
						if(DbPass != Pwdhash)
						{
							error = (TextView)findViewById(R.id.error); 
							error.setTextColor(Color.parseColor("#BC1212"));
							error.setText("Please check your old password!");
						}
						else
						{
							NewPass = NewPass + pwdsalt;
							NewPwdhash = NewPass.hashCode();

							ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("userDetails");
							query1.whereEqualTo("username", username);

							try {
								List<ParseObject> postList = query1.find();
								for (ParseObject users : postList)
								{
									users.put("password", NewPwdhash);
									users.put("forgot_password", 0);
									users.saveInBackground();
								}
							}
							catch (ParseException e1) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							intent1.putExtra("username", username);
							startActivity(intent1);

							Context context = getApplicationContext();
							CharSequence text = "Your password has been updated.";
							int duration = Toast.LENGTH_SHORT;
							Toast toast = Toast.makeText(context, text, duration);
							toast.show();
						}
					}
				}
			});		
		}		
	}

	public void cancelChange(View view)
	{
		Intent intent = getIntent();
		String username = intent.getStringExtra("username");

		Intent intent1 = new Intent(ChangePassword.this, UserHomePage.class);
		intent1.putExtra("username", username);
		startActivity(intent1);
	}

	public void registerValidation()
	{		
		newPass = (EditText) findViewById(R.id.password);
		confirmNewPass = (EditText) findViewById(R.id.verifyPassword);


		newPass.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				validationRegister.isValidPassword(newPass,confirmNewPass,0);
			}
		});

		confirmNewPass.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				validationRegister.isValidPassword(confirmNewPass,newPass,0);
			}
		});		
	}
}
