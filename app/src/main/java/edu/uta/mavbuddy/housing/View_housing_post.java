package edu.uta.mavbuddy.housing;

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

public class View_housing_post extends Activity {

	TextView id;
	TextView name;
	TextView title;
	TextView aptName;
	TextView typeOfApt;
	TextView typeOfAccom;
	TextView noOfPerson;
	TextView moveInDate;
	TextView moveOutDate;
	TextView roomFor;
	TextView rent;
	TextView mobileNo;
	TextView emailId;
	TextView comments;
	String postId;
	String username;
	SharedPreferences sharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_housing_post);
		Intent intent = getIntent();
		
		sharedPreferences = getSharedPreferences(HomeScreen.MyPREFERENCES, Context.MODE_PRIVATE);
		username = sharedPreferences.getString(HomeScreen.name,"");
		
		postId = intent.getStringExtra("postID");
		id = (TextView) findViewById(R.id.postId);
		name = (TextView) findViewById(R.id.name);
		title = (TextView) findViewById(R.id.title);
		aptName = (TextView) findViewById(R.id.aptName);
		typeOfApt = (TextView) findViewById(R.id.typeOfApt);
		typeOfAccom = (TextView) findViewById(R.id.typeOfAccom);
		noOfPerson = (TextView) findViewById(R.id.noOfPerson);
		moveInDate = (TextView) findViewById(R.id.moveInDate);
		moveOutDate = (TextView) findViewById(R.id.moveOutDate);
		roomFor = (TextView) findViewById(R.id.roomFor);
		rent = (TextView) findViewById(R.id.rent);
		mobileNo = (TextView) findViewById(R.id.mobileNo);
		emailId = (TextView) findViewById(R.id.emailId);
		comments = (TextView) findViewById(R.id.comments);
		
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("housing");
		query.whereEqualTo("objectId", postId);

		try {
		List<ParseObject> postList = query.find();;
			for (ParseObject postObject : postList)
			{
				id.setText(postObject.getObjectId());
				name.setText(postObject.getString("name"));
				title.setText(postObject.getString("title"));
				aptName.setText(postObject.getString("apt_name"));
				typeOfApt.setText(postObject.getString("type_of_apt"));
				typeOfAccom.setText(postObject.getString("type_of_acc"));
				if(postObject.getNumber("persons_req") != null)
					noOfPerson.setText(postObject.getNumber("persons_req").toString());
				moveInDate.setText(postObject.getString("move_in_date"));
				moveOutDate.setText(postObject.getString("move_out_date"));
				if(postObject.getString("room_for").equalsIgnoreCase("m"))
					roomFor.setText("Male");
				else
					roomFor.setText("Female");
				if(postObject.getNumber("rent") != null)
					rent.setText(postObject.getNumber("rent").toString());
				if(postObject.getNumber("mob_no") != null)
					mobileNo.setText(postObject.getNumber("mob_no").toString());
				emailId.setText(postObject.getString("email"));
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
		getMenuInflater().inflate(R.menu.view_housing_post, menu);
		return true;
	}
	
	public void reportSpamHousing(View view)
	{
		int spamCount = 0;
		
		
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("housing");
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
		reportSpam.put("postType", 0);
		reportSpam.saveInBackground();
		
		ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("housing");
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
		
		final Intent intent = new Intent(View_housing_post.this,Tab1.class);
		Bundle b=new Bundle();
		b.putInt("condition",1);
		intent.putExtras(b);
		startActivity(intent);
	}
	
	public void cancelHousePost(View view)
	{
		Intent intent = new Intent(this,Tab1.class);
		Bundle b=new Bundle();
		b.putInt("condition",1);
		intent.putExtras(b);
		startActivity(intent);
	}
}
