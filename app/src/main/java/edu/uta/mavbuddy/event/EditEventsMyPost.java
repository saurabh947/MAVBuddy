package edu.uta.mavbuddy.event;

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
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import edu.uta.mavbuddy.R;
import edu.uta.mavbuddy.validation.ValidationEvent;


public class EditEventsMyPost extends Activity {

	EditText title;
	EditText location;
	Button startDate;
	Button startTime;
	Button endDate;
	Button endTime;
	EditText cost;
	EditText description;
	String postId;
	ValidationEvent validationEvent = new ValidationEvent();
	int start_year, start_month, start_day,end_year, end_month, end_day,start_hr,start_min,end_hr,end_min;
	String condition = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_events_my_post);

		Intent intent = getIntent();
		postId = intent.getStringExtra("postID");
		condition = intent.getStringExtra("condition");

		title = (EditText) findViewById(R.id.title);
		location = (EditText) findViewById(R.id.location);
		startDate = (Button) findViewById(R.id.startDate);
		startTime = (Button) findViewById(R.id.startTime);
		endDate = (Button) findViewById(R.id.endDate);
		endTime = (Button) findViewById(R.id.endTime);
		cost = (EditText) findViewById(R.id.cost);
		description = (EditText) findViewById(R.id.description);

		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("events");
		query.whereEqualTo("objectId", postId);

		try {
			List<ParseObject> postList = query.find();
			for (ParseObject postObject : postList)
			{

				title.setText(postObject.getString("title"));
				location.setText(postObject.getString("location"));

				String[] str1 = postObject.getString("start_time").split(" ");
				startDate.setText(str1[0]);
				startTime.setText(str1[1]);

				String[] str2 = postObject.getString("end_time").split(" ");
				endDate.setText(str2[0]);
				endTime.setText(str2[1]);

				cost.setText(postObject.getString("cost"));
				description.setText(postObject.getString("description"));
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
		getMenuInflater().inflate(R.menu.edit_events_my_post, menu);
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

		Intent intent1 = new Intent(EditEventsMyPost.this, ViewEventMyPostActivity.class);

		title = (EditText) findViewById(R.id.title);
		location = (EditText) findViewById(R.id.location);
		startDate = (Button) findViewById(R.id.startDate);
		startTime = (Button) findViewById(R.id.startTime);
		endDate = (Button) findViewById(R.id.endDate);
		endTime = (Button) findViewById(R.id.endTime);
		cost = (EditText) findViewById(R.id.cost);
		description = (EditText) findViewById(R.id.description);

		if(validationEvent.isValidTitle(title,0) && validationEvent.isValidLocation(location,0) && validationEvent.isValidStartDate(startDate,0,this)
				&& validationEvent.isValidStartTime(startTime,0,this) && validationEvent.isValidEndDate(endDate,startDate,0,this) && validationEvent.isValidEndTime(endTime, startTime,endDate,startDate,0,this)
				&& validationEvent.isValidCost(cost,0) && validationEvent.isValidDescription(description,0))
		{

			String Title = title.getText().toString();
			String Location = location.getText().toString();
			String StartDate = startDate.getText().toString();
			String StartTime = startTime.getText().toString();
			String EndDate = endDate.getText().toString();
			String EndTime = endTime.getText().toString();
			String Cost = cost.getText().toString();
			String Description = description.getText().toString();

			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("events");
			query.whereEqualTo("objectId", postId);

			try {
				List<ParseObject> postList = query.find();
				for (ParseObject events : postList)
				{
					events.put("title", Title);
					events.put("location", Location);
					events.put("start_time", StartDate+" "+StartTime);
					events.put("end_time", EndDate+" "+EndTime);
					events.put("cost", Cost);
					events.put("description", Description);
					events.saveInBackground();
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

		Intent intent1 = new Intent(EditEventsMyPost.this, ViewEventMyPostActivity.class);
		intent1.putExtra("postID", postId);
		intent1.putExtra("condition", condition);
		startActivity(intent1);
	}

	@SuppressWarnings("deprecation")
	public void showStartDateDialog(View v) {

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
	public void showEndDateDialog(View v) {

		final Calendar c = Calendar.getInstance();
		end_year = c.get(Calendar.YEAR);
		end_month = c.get(Calendar.MONTH);
		end_day = c.get(Calendar.DAY_OF_MONTH);

		showDialog(3);
	}

	@SuppressWarnings("deprecation")
	public void showEndTimePickerDialog(View v) {

		final Calendar c = Calendar.getInstance();
		end_hr = c.get(Calendar.HOUR_OF_DAY);
		end_min = c.get(Calendar.MINUTE);

		showDialog(4);
	}

	private DatePickerDialog.OnDateSetListener startDateListener =
			new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, 
				int monthOfYear, int dayOfMonth) {
			start_year = year;
			start_month = monthOfYear;
			start_day = dayOfMonth;
			updateStartDate();
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

	private DatePickerDialog.OnDateSetListener endDateListener =
			new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, 
				int monthOfYear, int dayOfMonth) {
			end_year = year;
			end_month = monthOfYear;
			end_day = dayOfMonth;
			updateEndDate();
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
			return new DatePickerDialog(this, startDateListener,
					start_year, start_month, start_day);
		case 2:
			return new TimePickerDialog(this, startTime_Listener, start_hr, start_min, true);
		case 3:
			DatePickerDialog _enddate =   new DatePickerDialog(this, endDateListener,
					end_year, end_month, end_day){
				@Override
				public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth)
				{   
					if (year < end_year)
						view.updateDate(end_year, end_month, end_day);

					if (monthOfYear < end_month && year == end_year)
						view.updateDate(end_year, end_month, end_day);

					if (dayOfMonth < end_day && year == end_year && monthOfYear == end_month)
						view.updateDate(end_year, end_month, end_day);
				}
			};
			return _enddate;
		case 4:
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

	public void updateStartDate()
	{
		startDate = (Button) findViewById(R.id.startDate);
		startDate.setText(start_month + 1+"-"+start_day+"-"+start_year);
	}

	public void updateEndDate()
	{
		endDate = (Button) findViewById(R.id.endDate);
		endDate.setText(end_month + 1+"-"+end_day+"-"+end_year);
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
