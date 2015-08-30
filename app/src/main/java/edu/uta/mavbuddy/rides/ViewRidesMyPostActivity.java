package edu.uta.mavbuddy.rides;

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

public class ViewRidesMyPostActivity extends Activity {

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
	String condition = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_rides_my_post);
		
		Intent intent = getIntent();
		String postId = intent.getStringExtra("postID");
		condition = intent.getStringExtra("condition");
		
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
		getMenuInflater().inflate(R.menu.view_rides_my_post, menu);
		return true;
	}
	
	public void editPost(View view) {
		
		Intent intent = getIntent();
		String postId = intent.getStringExtra("postID");
		
		final Intent intent1 = new Intent(ViewRidesMyPostActivity.this, EditRidesMyPost.class);
		intent1.putExtra("postID", postId);
		intent1.putExtra("condition", condition);
		startActivity(intent1);
		
	}
	
	public void cancelRidePost(View view)
	{
		Intent intent = null;
		if(condition != null && condition.equalsIgnoreCase("innerRide"))
			intent = new Intent(this,RidesTab.class);
		else
			intent = new Intent(this,MyPostActivity.class);
		startActivity(intent);
	}
}
