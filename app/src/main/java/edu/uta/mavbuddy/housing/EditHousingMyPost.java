package edu.uta.mavbuddy.housing;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import edu.uta.mavbuddy.R;
import edu.uta.mavbuddy.validation.ValidationHousing;

public class EditHousingMyPost extends Activity {

	String gender;
	EditText name;
	EditText title;
	EditText aptName;
	Spinner typeOfApt;
	Spinner typeOfAccom;
	EditText noOfPerson;
	Button moveInDate;
	Button moveOutDate;
	RadioGroup roomFor;
	EditText rent;
	EditText mobileNo;
	EditText emailId;
	EditText comments;
	String postId;
	String condition;
	ValidationHousing validationHousing = new ValidationHousing();
	int moveIn_year, moveIn_month, moveIn_day,moveOut_year, moveOut_month, moveOut_day;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_housing_my_post);

		Intent intent = getIntent();
		postId = intent.getStringExtra("postID");
		condition = intent.getStringExtra("condition");
		
		name = (EditText) findViewById(R.id.name);
		title = (EditText) findViewById(R.id.title);
		aptName = (EditText) findViewById(R.id.aptName);
		typeOfApt = (Spinner) findViewById(R.id.typeOfApt);
		typeOfAccom = (Spinner) findViewById(R.id.typeOfAccom);
		noOfPerson = (EditText) findViewById(R.id.noOfPerson);
		moveInDate = (Button) findViewById(R.id.moveInDate);
		moveOutDate = (Button) findViewById(R.id.moveOutDate);
		roomFor = (RadioGroup) findViewById(R.id.radioGroup1);
		rent = (EditText) findViewById(R.id.rent);
		mobileNo = (EditText) findViewById(R.id.mobileNo);
		emailId = (EditText) findViewById(R.id.emailId);
		comments = (EditText) findViewById(R.id.comments);


		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("housing");
		query.whereEqualTo("objectId", postId);

		try {
			List<ParseObject> postList = query.find();
			for (ParseObject postObject : postList)
			{
				name.setText(postObject.getString("name"));
				title.setText(postObject.getString("title"));
				aptName.setText(postObject.getString("apt_name"));

				String apt = postObject.getString("type_of_apt");
				if (apt == "1BHK")
					typeOfApt.setSelection(0);
				else
					typeOfApt.setSelection(1);

				String acom = postObject.getString("type_of_acc");
				if (acom == "Permanent")
					typeOfAccom.setSelection(0);
				else
					typeOfAccom.setSelection(1);

				if(postObject.getNumber("persons_req") != null)
					noOfPerson.setText(postObject.getNumber("persons_req").toString());

				moveInDate.setText(postObject.getString("move_in_date"));
				moveOutDate.setText(postObject.getString("move_out_date"));

				if(postObject.getString("room_for").equalsIgnoreCase("m"))
					roomFor.check(R.id.radioMale);
				else
					roomFor.check(R.id.radioFemale);

				if(postObject.getNumber("rent") != null)
					rent.setText(postObject.getNumber("rent").toString());

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
		getMenuInflater().inflate(R.menu.edit_housing_my_post, menu);
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

		Intent intent1 = new Intent(EditHousingMyPost.this, ViewHousingMyPostActivity.class);

		if(validationHousing.isValidName(name,0) && validationHousing.isValidTitle(title,0) && validationHousing.isValidAptName(aptName,0)
				&& validationHousing.isValidNoOfPerson(noOfPerson,0) && validationHousing.isValidMoveInDate(moveInDate, moveOutDate,0,this)
				&& validationHousing.isValidMoveOutDate(moveOutDate, moveInDate,0,this) && validationHousing.isValidRent(rent,0)
				&& validationHousing.isValidMobNo(mobileNo,0) && validationHousing.isValidEmail(emailId,0) && validationHousing.isValidComments(comments,0))
		{

			final String Name = name.getText().toString();
			final String Title = title.getText().toString();
			final String AptName = aptName.getText().toString();
			final String TypeOfApt = typeOfApt.getSelectedItem().toString();
			final String TypeOfAccom = typeOfAccom.getSelectedItem().toString();
			final int NoOfPerson = Integer.parseInt(noOfPerson.getText().toString());
			final String MoveInDate = moveInDate.getText().toString();
			final String MoveOutDate = moveOutDate.getText().toString();
			final int RoomFor = roomFor.getCheckedRadioButtonId();
			int Rent = 0;
			if(rent.getText().toString() != null && !rent.getText().toString().isEmpty())
				Rent = Integer.parseInt(rent.getText().toString());
			long MobileNo = 0;
			if(mobileNo.getText().toString() != null && !mobileNo.getText().toString().isEmpty())
				MobileNo = Long.parseLong(mobileNo.getText().toString());
			final String EmailId = emailId.getText().toString();
			final String Comments = comments.getText().toString();
			RadioButton btn = (RadioButton) findViewById(RoomFor);
			gender = (String) btn.getText();

			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("housing");
			query.whereEqualTo("objectId", postId);

			try {
				List<ParseObject> postList = query.find();
				for (ParseObject housing : postList)
				{
					housing.put("name", Name);
					housing.put("title", Title);
					housing.put("apt_name", AptName);
					housing.put("type_of_apt", TypeOfApt);
					housing.put("type_of_acc", TypeOfAccom);
					housing.put("persons_req", NoOfPerson);
					housing.put("move_in_date", MoveInDate);
					housing.put("move_out_date", MoveOutDate);
					housing.put("room_for", gender);
					if(Rent != 0)
						housing.put("rent", Rent);
					if(MobileNo != 0)
						housing.put("mob_no", MobileNo);
					housing.put("email", EmailId);
					housing.put("comments", Comments);
					housing.saveInBackground();
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

		Intent intent1 = new Intent(EditHousingMyPost.this, ViewHousingMyPostActivity.class);
		intent1.putExtra("postID", postId);
		intent1.putExtra("condition", condition);
		startActivity(intent1);
	}

	@SuppressWarnings("deprecation")
	public void showMoveInDatePickerDialog(View v) {

		final Calendar c = Calendar.getInstance();
		moveIn_year = c.get(Calendar.YEAR);
		moveIn_month = c.get(Calendar.MONTH);
		moveIn_day = c.get(Calendar.DAY_OF_MONTH);

		showDialog(1);
	}

	@SuppressWarnings("deprecation")
	public void showMoveOutDatePickerDialog(View v) {

		final Calendar c = Calendar.getInstance();
		moveOut_year = c.get(Calendar.YEAR);
		moveOut_month = c.get(Calendar.MONTH);
		moveOut_day = c.get(Calendar.DAY_OF_MONTH);

		showDialog(2);
	}

	private DatePickerDialog.OnDateSetListener moveIn_dateListener =
			new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, 
				int monthOfYear, int dayOfMonth) {
			moveIn_year = year;
			moveIn_month = monthOfYear;
			moveIn_day = dayOfMonth;
			updateMoveInDate();
		}
	};

	private DatePickerDialog.OnDateSetListener moveOut_dateListener =
			new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, 
				int monthOfYear, int dayOfMonth) {
			moveOut_year = year;
			moveOut_month = monthOfYear;
			moveOut_day = dayOfMonth;
			updateMoveOutDate();
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 1:
			DatePickerDialog _date =   new DatePickerDialog(this, moveIn_dateListener,
					moveIn_year, moveIn_month, moveIn_day){
				@Override
				public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth)
				{   
					if (year < moveIn_year)
						view.updateDate(moveIn_year, moveIn_month, moveIn_day);

					if (monthOfYear < moveIn_month && year == moveIn_year)
						view.updateDate(moveIn_year, moveIn_month, moveIn_day);

					if (dayOfMonth < moveIn_day && year == moveIn_year && monthOfYear == moveIn_month)
						view.updateDate(moveIn_year, moveIn_month, moveIn_day);
				}
			};
			return _date;
		case 2:
			DatePickerDialog _enddate =   new DatePickerDialog(this, moveOut_dateListener,
					moveOut_year, moveOut_month, moveOut_day){
				@Override
				public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth)
				{   
					if (year < moveOut_year)
						view.updateDate(moveOut_year, moveOut_month, moveOut_day);

					if (monthOfYear < moveOut_month && year == moveOut_year)
						view.updateDate(moveOut_year, moveOut_month, moveOut_day);

					if (dayOfMonth < moveOut_day && year == moveOut_year && monthOfYear == moveOut_month)
						view.updateDate(moveOut_year, moveOut_month, moveOut_day);
				}
			};
			return _enddate;
		}
		return null;
	}

	public void updateMoveInDate()
	{
		moveInDate = (Button) findViewById(R.id.moveInDate);
		moveInDate.setText(moveIn_month + 1+"-"+moveIn_day+"-"+moveIn_year);
	}

	public void updateMoveOutDate()
	{
		moveOutDate = (Button) findViewById(R.id.moveOutDate);
		moveOutDate.setText(moveOut_month + 1+"-"+moveOut_day+"-"+moveOut_year);
	}
}
