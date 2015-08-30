package edu.uta.mavbuddy.rides;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import edu.uta.mavbuddy.R;
import edu.uta.mavbuddy.validation.ValidationRides;

public class EditRidesMyPost extends Activity {
	
	RadioGroup typeOfPost;
	EditText name;
	EditText title;
	EditText destination;
	Button date;
	Button startTime;
	Button endTime;
	EditText mobileNo;
	EditText emailId;
	EditText noOfPeople;
	EditText comments;
	String postId;
	String condition = "";
	
	ValidationRides validationRides = new ValidationRides();
	int start_year, start_month, start_day,start_hr,start_min,end_hr,end_min;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_rides_my_post);
		
		Intent intent = getIntent();
		postId = intent.getStringExtra("postID");
		condition = intent.getStringExtra("condition");
		
		typeOfPost = (RadioGroup) findViewById(R.id.radioGroup1);
		name = (EditText) findViewById(R.id.name);
		title = (EditText) findViewById(R.id.title);
		destination = (EditText) findViewById(R.id.destination);
		date = (Button) findViewById(R.id.date);
		noOfPeople = (EditText) findViewById(R.id.noOfPeople);
		startTime = (Button) findViewById(R.id.startTime);
		endTime = (Button) findViewById(R.id.endTime);
		mobileNo = (EditText) findViewById(R.id.mobileNo);
		emailId = (EditText) findViewById(R.id.emailId);
		comments = (EditText) findViewById(R.id.comments);
		
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("rides");
		query.whereEqualTo("objectId", postId);
		
		try {
			List<ParseObject> postList = query.find();
			for (ParseObject postObject : postList)
			{
				name.setText(postObject.getString("name"));
				title.setText(postObject.getString("title"));
				destination.setText(postObject.getString("destination"));
				date.setText(postObject.getString("date"));

				if(postObject.getNumber("no_of_people") != null)
					noOfPeople.setText(postObject.getNumber("no_of_people").toString());
				
				startTime.setText(postObject.getString("start_time"));
				endTime.setText(postObject.getString("end_time"));

				if(postObject.getString("type_of_post").equalsIgnoreCase("post"))
					typeOfPost.check(R.id.radioButton1);
				else
					typeOfPost.check(R.id.radioButton2);

				if(postObject.getNumber("mob_no") != null)
					mobileNo.setText(postObject.getNumber("mob_no").toString());

				emailId.setText(postObject.getString("email"));
				comments.setText(postObject.getString("comments"));
			}
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_rides_my_post, menu);
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
	
	public void updatePost(View view) {

		Intent intent = getIntent();
		String postId = intent.getStringExtra("postID");

		Intent intent1 = new Intent(EditRidesMyPost.this, ViewRidesMyPostActivity.class);
		
		typeOfPost = (RadioGroup) findViewById(R.id.radioGroup1);
		name = (EditText) findViewById(R.id.name);
		title = (EditText) findViewById(R.id.title);
		destination = (EditText) findViewById(R.id.destination);
		date = (Button) findViewById(R.id.date);
		startTime = (Button) findViewById(R.id.startTime);
		endTime = (Button) findViewById(R.id.endTime);
		mobileNo = (EditText) findViewById(R.id.mobileNo);
		emailId = (EditText) findViewById(R.id.emailId);
		noOfPeople = (EditText) findViewById(R.id.noOfPeople);
		comments = (EditText) findViewById(R.id.comments);

		if(validationRides.isValidName(name,0) && validationRides.isValidTitle(title,0) && validationRides.isValidDestination(destination,0)
				&& validationRides.isValidDate(date,0, this) && validationRides.isValidStartTime(startTime, endTime,0, this) && validationRides.isValidEndTime(endTime, startTime,0, this)
				&& validationRides.isValidMobNo(mobileNo,0) && validationRides.isValidEmail(emailId,0)
				&& validationRides.isValidNoOfPerson(noOfPeople,0) && validationRides.isValidComments(comments,0))
		{


		int TypeofPost = typeOfPost.getCheckedRadioButtonId();
		String Name = name.getText().toString();
		String Title = title.getText().toString();
		String Destination = destination.getText().toString();
		String Date = date.getText().toString();
		String StartTime = startTime.getText().toString();
		String EndTime = endTime.getText().toString();
		long MobileNo = 0;
		if(mobileNo.getText().toString() != null && !mobileNo.getText().toString().isEmpty())
			MobileNo = Long.parseLong(mobileNo.getText().toString());
		String EmailId = emailId.getText().toString();
		int NoOfPeople = Integer.parseInt(noOfPeople.getText().toString());
		String Comments = comments.getText().toString();
		
		RadioButton btn = (RadioButton) findViewById(TypeofPost);
		String TypeOfPost = (String) btn.getText();
		
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("rides");
		query.whereEqualTo("objectId", postId);
		
		try {
			List<ParseObject> postList = query.find();
			for (ParseObject rides : postList)
			{
				rides.put("type_of_post", TypeOfPost);
				rides.put("name", Name);
				rides.put("title", Title);
				rides.put("destination", Destination);
				rides.put("date", Date);
				rides.put("start_time", StartTime);
				rides.put("end_time", EndTime);
				if(MobileNo != 0)
					rides.put("mob_no", MobileNo);
				rides.put("email", EmailId);
				rides.put("no_of_people", NoOfPeople);
				rides.put("comments", Comments);
				rides.saveInBackground();
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		intent1.putExtra("postID", postId);
		intent1.putExtra("condition", condition);
		startActivity(intent1);

		Context context = getApplicationContext();
		CharSequence text = "Post updated successfully!";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		}
	}
	
	public void cancelEdit(View view){
		Intent intent = getIntent();
		String postId = intent.getStringExtra("postID");

		Intent intent1 = new Intent(EditRidesMyPost.this, ViewRidesMyPostActivity.class);
		intent1.putExtra("condition", condition);
		intent1.putExtra("postID", postId);
		startActivity(intent1);
	}

	@SuppressWarnings("deprecation")
	public void showDateDialog(View v) {

		final Calendar c = Calendar.getInstance();
		start_year = c.get(Calendar.YEAR);
		start_month = c.get(Calendar.MONTH);
		start_day = c.get(Calendar.DAY_OF_MONTH);
	    
		showDialog(1);
	}
	
	@SuppressWarnings("deprecation")
	public void showStartTimePickerDialog(View v) {

		final Calendar c = Calendar.getInstance();
		start_hr = c.get(Calendar.HOUR_OF_DAY);
		start_min = c.get(Calendar.MINUTE);
	    
		showDialog(2);
	}
	
	@SuppressWarnings("deprecation")
	public void showEndTimePickerDialog(View v) {

		final Calendar c = Calendar.getInstance();
		end_hr = c.get(Calendar.HOUR_OF_DAY);
		end_min = c.get(Calendar.MINUTE);
	    
		showDialog(3);
	}

	private DatePickerDialog.OnDateSetListener dateListener =
            new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, 
                                  int monthOfYear, int dayOfMonth) {
                start_year = year;
                start_month = monthOfYear;
                start_day = dayOfMonth;
                updateDate();
            }
        };
        
    private TimePickerDialog.OnTimeSetListener startTime_Listener =
    		new TimePickerDialog.OnTimeSetListener() {
				
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					start_hr = hourOfDay;
					start_min = minute;
					updateStartTime();
				}
			};
        
	private TimePickerDialog.OnTimeSetListener endTime_Listener =
    		new TimePickerDialog.OnTimeSetListener() {
				
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					end_hr = hourOfDay;
					end_min = minute;
					updateEndTime();
				}
			};

	@Override
	protected Dialog onCreateDialog(int id) {
	    switch (id) {
	    case 1:
	    	DatePickerDialog _date =   new DatePickerDialog(this, dateListener,
	        		start_year, start_month, start_day){
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                {   
                    if (year < start_year)
                        view.updateDate(start_year, start_month, start_day);

                    if (monthOfYear < start_month && year == start_year)
                        view.updateDate(start_year, start_month, start_day);

                    if (dayOfMonth < start_day && year == start_year && monthOfYear == start_month)
                        view.updateDate(start_year, start_month, start_day);
                }
            };
            return _date;
	    case 2:
	    	TimePickerDialog _startTime = new TimePickerDialog(this, startTime_Listener, start_hr, start_min, true){

				@Override
				public void onTimeChanged(TimePicker view, int hourOfDay,int minute) {
					 if (hourOfDay < start_hr)
						 updateTime(start_hr, start_min);
					 if (minute < start_min && hourOfDay == start_hr)
						 updateTime(start_hr, start_min);
				}
	    		
	    	};
	        return _startTime;
	    case 3:
	    	TimePickerDialog _endTime = new TimePickerDialog(this, endTime_Listener, end_hr, end_min, true){

				@Override
				public void onTimeChanged(TimePicker view, int hourOfDay,int minute) {
					 if (hourOfDay < end_hr)
						 updateTime(end_hr, end_min);
					 if (minute < end_min && hourOfDay == end_hr)
						 updateTime(end_hr, end_min);
				}
	    		
	    	};
	        return _endTime;
	    }
	    return null;
	}
	
	public void updateDate()
	{
		date = (Button) findViewById(R.id.date);
		date.setText(start_month + 1+"-"+start_day+"-"+start_year);
	}
	
	public void updateStartTime()
	{
		startTime = (Button) findViewById(R.id.startTime);
		startTime.setText(start_hr+":"+start_min);
	}
	
	public void updateEndTime()
	{
		endTime = (Button) findViewById(R.id.endTime);
		endTime.setText(end_hr+":"+end_min);
	}
}
