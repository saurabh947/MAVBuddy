package edu.uta.mavbuddy.event;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import edu.uta.mavbuddy.MyPostActivity;
import edu.uta.mavbuddy.R;

public class ViewEventMyPostActivity extends Activity {

	TextView id;
	TextView title;
	TextView location;
	TextView startDate;
	TextView startTime;
	TextView endDate;
	TextView endTime;
	TextView cost;
	TextView description;
	String condition = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_event_my_post);
		
		Intent intent = getIntent();
		String postId = intent.getStringExtra("postID");
		condition = intent.getStringExtra("condition");
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
				
				if(postObject.getString("cost") != null)
					cost.setText(postObject.getString("cost"));
				
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
		getMenuInflater().inflate(R.menu.view_event_my_post, menu);
		return true;
	}
	
	public void editPost(View view) {
		
		Intent intent = getIntent();
		String postId = intent.getStringExtra("postID");
		
		final Intent intent1 = new Intent(ViewEventMyPostActivity.this, EditEventsMyPost.class);
		intent1.putExtra("postID", postId);
		intent1.putExtra("condition", condition);
		startActivity(intent1);
		
	}
	
	public void cancelEventPost(View view)
	{
		Intent intent = null;
		if(condition != null && condition.equalsIgnoreCase("innerEvent"))
			intent = new Intent(this,EventTab.class);
		else
			intent = new Intent(this,MyPostActivity.class);
		startActivity(intent);
	}
}
