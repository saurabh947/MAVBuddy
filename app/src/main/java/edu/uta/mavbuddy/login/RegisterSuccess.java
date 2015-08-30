package edu.uta.mavbuddy.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import edu.uta.mavbuddy.R;

public class RegisterSuccess extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_success);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register_success, menu);
		return true;
	}
	
	public void registerSuccessToHomeScreen(View view)
	{
		Intent intent = new Intent(RegisterSuccess.this,HomeScreen.class);
		startActivity(intent);
	}

}
