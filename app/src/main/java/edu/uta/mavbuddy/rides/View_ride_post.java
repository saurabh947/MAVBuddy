package edu.uta.mavbuddy.rides;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import edu.uta.mavbuddy.R;
import edu.uta.mavbuddy.Tab1;
import edu.uta.mavbuddy.login.HomeScreen;

public class View_ride_post extends Activity {

	TextView id;
	TextView typeOfPost;
	TextView name;
	TextView title;
	TextView destination;
	TextView date;
	TextView startTime;
	TextView endTime;
	TextView mobileNo;
	TextView emailId;
	TextView no_of_people;
	TextView comments;
	String postId;
	String username;
	SharedPreferences sharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_ride_post);
		Intent intent = getIntent();
		
		sharedPreferences = getSharedPreferences(HomeScreen.MyPREFERENCES, Context.MODE_PRIVATE);
		username = sharedPreferences.getString(HomeScreen.name,"");
		
		postId = intent.getStringExtra("postID");
		
		id = (TextView) findViewById(R.id.postId);
		typeOfPost = (TextView) findViewById(R.id.typeOfPost);
		name = (TextView) findViewById(R.id.name);
		title = (TextView) findViewById(R.id.title);
		destination = (TextView) findViewById(R.id.destination);
		date = (TextView) findViewById(R.id.date);
		startTime = (TextView) findViewById(R.id.startTime);
		endTime = (TextView) findViewById(R.id.endTime);
		mobileNo = (TextView) findViewById(R.id.mobileNo);
		emailId = (TextView) findViewById(R.id.emailId);
		no_of_people = (TextView) findViewById(R.id.no_of_people);
		comments = (TextView) findViewById(R.id.comments);
		
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("rides");
		query.whereEqualTo("objectId", postId);

		try {
		List<ParseObject> postList = query.find();;
			for (ParseObject postObject : postList)
			{
				id.setText(postObject.getObjectId());
				typeOfPost.setText(postObject.getString("type_of_post"));
				name.setText(postObject.getString("name"));
				title.setText(postObject.getString("title"));
				destination.setText(postObject.getString("destination"));
				date.setText(postObject.getString("date"));
				startTime.setText(postObject.getString("start_time"));
				endTime.setText(postObject.getString("end_time"));
				if(postObject.getNumber("mob_no") != null)
					mobileNo.setText(postObject.getNumber("mob_no").toString());
				emailId.setText(postObject.getString("email"));
				if(postObject.getNumber("no_of_people") != null)
					no_of_people.setText(postObject.getNumber("no_of_people").toString());
				comments.setText(postObject.getString("comments"));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_ride_post, menu);
		return true;
	}
	
	public void reportSpamRide(View view)
	{
		int spamCount = 0;
		
		
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("rides");
		query.whereEqualTo("objectId", postId);
		try {
			ParseObject object = query.getFirst();
			spamCount = object.getInt("spamCount");
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
		}
		
		ParseObject reportSpam = new ParseObject("reportSpam");
		reportSpam.put("username", username);
		reportSpam.put("postId", postId);
		reportSpam.put("postType", 1);
		reportSpam.saveInBackground();
		
		ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("rides");
		query1.whereEqualTo("objectId", postId);

		try {
			List<ParseObject> postList = query1.find();
			for (ParseObject events : postList)
			{
				events.put("spamCount", spamCount+1);
				events.saveInBackground();
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final Intent intent = new Intent(View_ride_post.this,Tab1.class);
		Bundle b=new Bundle();
		b.putInt("condition",2);
		intent.putExtras(b);
		startActivity(intent);
	}
	
	public void cancelRidePost(View view)
	{
		Intent intent = new Intent(this,Tab1.class);
		Bundle b=new Bundle();
		b.putInt("condition",2);
		intent.putExtras(b);
		startActivity(intent);
	}
}
