package edu.uta.mavbuddy.housing;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import edu.uta.mavbuddy.AdapterForListView;
import edu.uta.mavbuddy.R;
import edu.uta.mavbuddy.Tab1;
import edu.uta.mavbuddy.login.HomeScreen;
import edu.uta.mavbuddy.validation.ValidationHousing;

public class HousingTab extends FragmentActivity implements
ActionBar.TabListener {

	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;

	String gender;

	EditText name;
	EditText title;
	EditText aptName;
	Spinner typeOfApt;
	Spinner typeOfAccom;
	EditText noOfPerson;
	Button moveInDate;
	Button moveOutDate;
	RadioGroup rg;
	EditText rent;
	EditText mobileNo;
	EditText emailId;
	EditText comments;

	ValidationHousing validationHousing = new ValidationHousing();
	String username;
	SharedPreferences sharedPreferences;
	int moveIn_year, moveIn_month, moveIn_day,moveOut_year, moveOut_month, moveOut_day;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_housing_tab);

		//for getting the username from the shared preferences
		sharedPreferences = getSharedPreferences(HomeScreen.MyPREFERENCES, Context.MODE_PRIVATE);
		username = sharedPreferences.getString(HomeScreen.name,"");

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
		.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.housing_tab, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		ListView list = (ListView) findViewById(R.id.housinglistView);
		final ListView list1;
		String[] eachTitle = null;
		String[] eachComment = null;
		String[] eachId = null;
		int count = 0;

		if(tab.getPosition() == 1)
		{
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("housing");
			query.whereEqualTo("username", username);
			query.orderByDescending("createdAt");
			try {
				List<ParseObject> postList = query.find();
				eachTitle = new String[postList.size()];
				eachComment = new String[postList.size()];
				eachId = new String[postList.size()];
				for (ParseObject postObject : postList)
				{
					eachId[count]=postObject.getObjectId();
					eachTitle[count]=postObject.getString("title");
					eachComment[count]=postObject.getString("comments");
					count=count+1;
				}   
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			AdapterForListView adapter = new AdapterForListView(HousingTab.this, eachId, eachTitle, eachComment, R.drawable.housing_icon);
			list.setAdapter(adapter);

			list1 = list;

			list1.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent myIntent = null;
					myIntent = new Intent(view.getContext(), ViewHousingMyPostActivity.class);
					myIntent.putExtra("condition", "innerHousing");
					myIntent.putExtra("postID", list1.getItemAtPosition(position).toString());
					startActivityForResult(myIntent, 0);
				}
			});
		}
		else
		{
			list.setVisibility(0);
			list.setAdapter(null);
		}

		mViewPager.setCurrentItem(tab.getPosition());
	}


	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {

		ListView list = (ListView) findViewById(R.id.housinglistView);
		final ListView list1;
		String[] eachTitle = null;
		String[] eachComment = null;
		String[] eachId = null;
		int count = 0;

		if(tab.getPosition() == 1)
		{
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("housing");
			query.whereEqualTo("username", username);
			query.orderByDescending("createdAt");
			try {
				List<ParseObject> postList = query.find();
				eachTitle = new String[postList.size()];
				eachComment = new String[postList.size()];
				eachId = new String[postList.size()];
				for (ParseObject postObject : postList)
				{
					eachId[count]=postObject.getObjectId();
					eachTitle[count]=postObject.getString("title");
					eachComment[count]=postObject.getString("comments");
					count=count+1;
				}   
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			AdapterForListView adapter = new AdapterForListView(HousingTab.this, eachId, eachTitle, eachComment, R.drawable.housing_icon);
			list.setAdapter(adapter);

			list1 = list;

			list1.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent myIntent = null;
					myIntent = new Intent(view.getContext(), ViewHousingMyPostActivity.class);
					myIntent.putExtra("condition", "innerHousing");
					myIntent.putExtra("postID", list1.getItemAtPosition(position).toString());
					startActivityForResult(myIntent, 0);
				}
			});
		}
		else
		{
			list.setVisibility(0);
			list.setAdapter(null);
		}
		mViewPager.setCurrentItem(tab.getPosition());
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment;
			if(position == 0)
				fragment = new AddHousePost();
			else
				fragment = new MyHousePostFragment();
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_housing_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_housing_section2).toUpperCase(l);
			}
			return null;
		}
	}

	public void saveHousePost(View view)
	{
		Intent intent = new Intent(HousingTab.this,Tab1.class);

		name = (EditText) findViewById(R.id.name);
		title = (EditText) findViewById(R.id.title);
		aptName = (EditText) findViewById(R.id.aptName);
		typeOfApt = (Spinner) findViewById(R.id.typeOfApt);
		typeOfAccom = (Spinner) findViewById(R.id.typeOfAccom);
		noOfPerson = (EditText) findViewById(R.id.noOfPerson);
		moveInDate = (Button) findViewById(R.id.moveInDate);
		moveOutDate = (Button) findViewById(R.id.moveOutDate);
		rg = (RadioGroup) findViewById(R.id.radioGroup1);
		rent = (EditText) findViewById(R.id.rent);
		mobileNo = (EditText) findViewById(R.id.mobileNo);
		emailId = (EditText) findViewById(R.id.emailId);
		comments = (EditText) findViewById(R.id.comments);

		Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.mavbuddy001);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] data = stream.toByteArray();


		if(validationHousing.isValidName(name,0) && validationHousing.isValidTitle(title,0) && validationHousing.isValidAptName(aptName,0)
				&& validationHousing.isValidNoOfPerson(noOfPerson,0) && validationHousing.isValidMoveInDate(moveInDate, moveOutDate,0, this)
				&& validationHousing.isValidMoveOutDate(moveOutDate, moveInDate,0,this) && validationHousing.isValidRent(rent,0)
				&& validationHousing.isValidMobNo(mobileNo,0) && validationHousing.isValidEmail(emailId,0) && validationHousing.isValidComments(comments,0))
		{
			String Name = name.getText().toString();
			String Title = title.getText().toString();
			String AptName = aptName.getText().toString();
			String TypeOfApt = typeOfApt.getSelectedItem().toString();
			String TypeOfAccom = typeOfAccom.getSelectedItem().toString();
			int NoOfPerson = Integer.parseInt(noOfPerson.getText().toString());
			String MoveInDate = moveInDate.getText().toString();
			String MoveOutDate = moveOutDate.getText().toString();
			int RoomFor = rg.getCheckedRadioButtonId();
			int Rent = 0;
			if(rent.getText().toString() != null && !rent.getText().toString().isEmpty())
				Rent = Integer.parseInt(rent.getText().toString());
			long MobileNo = 0;
			if(mobileNo.getText().toString() != null && !mobileNo.getText().toString().isEmpty())
				MobileNo = Long.parseLong(mobileNo.getText().toString());
			String EmailId = emailId.getText().toString();
			String Comments = comments.getText().toString();
			ParseFile file = new ParseFile ("mavbuddy.png", data);
			file.saveInBackground();

			RadioButton btn = (RadioButton) findViewById(RoomFor);
			gender = (String) btn.getText();

			ParseObject housing = new ParseObject("housing");
			housing.put("username",username);
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
			housing.put("image",file);
			housing.put("spamCount", 0);
			housing.saveInBackground();


			Bundle b=new Bundle();
			b.putInt("condition",1);
			intent.putExtras(b);
			startActivity(intent);

			Context context = getApplicationContext();
			CharSequence text = "Post added successfully!";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}

	}

	public void cancelHousePost(View view)
	{
		Intent intent = new Intent(HousingTab.this,Tab1.class);
		Bundle b=new Bundle();
		b.putInt("condition",1);
		intent.putExtras(b);
		startActivity(intent);
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
