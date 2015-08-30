package edu.uta.mavbuddy.event;

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

public class View_event_post extends Activity {

	TextView id;
	TextView title;
	TextView location;
	TextView startDate;
	TextView startTime;
	TextView endDate;
	TextView endTime;
	TextView cost;
	TextView description;
	String postId;
	String username;
	SharedPreferences sharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_event_post);
		Intent intent = getIntent();
		
		sharedPreferences = getSharedPreferences(HomeScreen.MyPREFERENCES, Context.MODE_PRIVATE);
		username = sharedPreferences.getString(HomeScreen.name,"");
		
		postId = intent.getStringExtra("postID");
		String stTime = "";
		String enTime = "";
		
		id = (TextView) findViewById(R.id.postId);
		title = (TextView) findViewById(R.id.title);
		location = (TextView) findViewById(R.id.location);
		startDate = (TextView) findViewById(R.id.startDate);
		startTime = (TextView) findViewById(R.id.startTime);
		endDate = (TextView) findViewById(R.id.endDate);
		endTime = (TextView) findViewById(R.id.endTime);
		cost = (TextView) findViewById(R.id.cost);
		description = (TextView) findViewById(R.id.description);
		
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("events");
		query.whereEqualTo("objectId", postId);

		try {
		List<ParseObject> postList = query.find();;
			for (ParseObject postObject : postList)
			{
				id.setText(postObject.getObjectId());
				title.setText(postObject.getString("title"));
				location.setText(postObject.getString("location"));
				
				stTime = postObject.getString("start_time");
				String sTime [] = stTime.split(" ");
				startDate.setText(sTime[0]);
				startTime.setText(sTime[1]);
				
				enTime = postObject.getString("end_time");
				String eTime [] = enTime.split(" ");
				endDate.setText(eTime[0]);
				endTime.setText(eTime[1]);
				
				if(postObject.getNumber("cost") != null)
					cost.setText(postObject.getNumber("cost").toString());
				description.setText(postObject.getString("description"));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_event_post, menu);
		return true;
	}
	
	public void reportSpamEvent(View view)
	{
		int spamCount = 0;
		
		
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("events");
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
		reportSpam.put("postType", 2);
		reportSpam.saveInBackground();
		
		ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("events");
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
		
		final Intent intent = new Intent(View_event_post.this,Tab1.class);
		Bundle b=new Bundle();
		b.putInt("condition",3);
		intent.putExtras(b);
		startActivity(intent);
		
	}
	
	public void cancelEventPost(View view)
	{
		Intent intent = new Intent(this,Tab1.class);
		Bundle b=new Bundle();
		b.putInt("condition",3);
		intent.putExtras(b);
		startActivity(intent);
	}
}
