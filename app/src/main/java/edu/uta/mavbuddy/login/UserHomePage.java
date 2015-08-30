package edu.uta.mavbuddy.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import edu.uta.mavbuddy.MyPostActivity;
import edu.uta.mavbuddy.R;
import edu.uta.mavbuddy.Tab1;

public class UserHomePage extends Activity {
	
	private String username;
	SharedPreferences sharedPreferences;
	Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_home_page);
		sharedPreferences = getSharedPreferences(HomeScreen.MyPREFERENCES, Context.MODE_PRIVATE);
		username = sharedPreferences.getString(HomeScreen.name,"");
		TextView welcome = (TextView) findViewById(R.id.welcome);
		welcome.setText("Welcome "+username+ " !!!");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_home_page, menu);
		return true;
	}

	public void searchHousePage(View view)
	{
		Intent intent = new Intent(UserHomePage.this,Tab1.class);
		Bundle b=new Bundle();
		b.putInt("condition",1);
		intent.putExtras(b);
		startActivity(intent);
	}
	
	public void searchRidesPage(View view)
	{
		Intent intent = new Intent(UserHomePage.this,Tab1.class);
		Bundle b=new Bundle();
		b.putInt("condition",2);
		intent.putExtras(b);
		startActivity(intent);
	}
	
	public void searchEventPage(View view)
	{
		Intent intent = new Intent(UserHomePage.this,Tab1.class);
		Bundle b=new Bundle();
		b.putInt("condition",3);
		intent.putExtras(b);
		startActivity(intent);
	}
	
	public void myPost(View view)
	{
		Intent intent = new Intent(UserHomePage.this,MyPostActivity.class);
		startActivity(intent);
	}
	
	public void changePassword(View view)
	{
		Intent intent = new Intent(UserHomePage.this,ChangePassword.class);
		intent.putExtra("username", username);
		startActivity(intent);
	}
	
	public void signOut(View view)
	{
		SharedPreferences sharedpreferences = getSharedPreferences(HomeScreen.MyPREFERENCES, Context.MODE_PRIVATE);
		Editor editor = sharedpreferences.edit();
		editor.clear();
		editor.commit();
		Intent intent = new Intent(UserHomePage.this,HomeScreen.class);
		moveTaskToBack(true);
		UserHomePage.this.finish();
		startActivity(intent);
	}
}
