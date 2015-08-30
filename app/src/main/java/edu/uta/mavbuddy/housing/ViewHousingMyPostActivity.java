package edu.uta.mavbuddy.housing;

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

public class ViewHousingMyPostActivity extends Activity {

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
	String condition = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_housing_my_post);
		
		Intent intent = getIntent();
		String postId = intent.getStringExtra("postID");
		condition = intent.getStringExtra("condition");
		
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
		List<ParseObject> postList = query.find();
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
		getMenuInflater().inflate(R.menu.view_housing_my_post, menu);
		return true;
	}
	
	public void editPost(View view) {
		
		Intent intent = getIntent();
		String postId = intent.getStringExtra("postID");
		
		final Intent intent1 = new Intent(ViewHousingMyPostActivity.this, EditHousingMyPost.class);
		intent1.putExtra("postID", postId);
		intent1.putExtra("condition", condition);
		startActivity(intent1);
		
	}
	
	public void cancelHousePost(View view)
	{
		Intent intent = null;
		if(condition != null && condition.equalsIgnoreCase("innerHousing"))
			intent = new Intent(this,HousingTab.class);
		else
			intent = new Intent(this,MyPostActivity.class);
		startActivity(intent);
	}
}
