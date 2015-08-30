package edu.uta.mavbuddy.rides;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import edu.uta.mavbuddy.AdapterForListView;
import edu.uta.mavbuddy.R;
import edu.uta.mavbuddy.Tab1;
import edu.uta.mavbuddy.login.HomeScreen;
import edu.uta.mavbuddy.validation.ValidationRides;

public class RidesTab extends FragmentActivity implements ActionBar.TabListener {

	SectionsPagerAdapter mSectionsPagerAdapter;

	ViewPager mViewPager;

	RadioGroup rg;
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
	ValidationRides validationRides = new ValidationRides();
	private String username;
	SharedPreferences sharedPreferences;
	int start_year, start_month, start_day,start_hr,start_min,end_hr,end_min;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rides_tab);

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
		getMenuInflater().inflate(R.menu.rides_tab, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		ListView list = (ListView) findViewById(R.id.rideslistView);
		final ListView list1;
		String[] eachTitle = null;
		String[] eachComment = null;
		String[] eachId = null;
		int count = 0;
		
		if(tab.getPosition() == 1)
		{
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("rides");
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
			
			AdapterForListView adapter = new AdapterForListView(RidesTab.this, eachId, eachTitle, eachComment,R.drawable.rides_icon);
		    list.setAdapter(adapter);
		    
		    list1 = list;
		    
			list1.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View view,
		                int position, long id) {
		        		 Intent myIntent = null;
		        		 myIntent = new Intent(view.getContext(), ViewRidesMyPostActivity.class);
		        		 myIntent.putExtra("condition", "innerRide");
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
		ListView list = (ListView) findViewById(R.id.rideslistView);
		final ListView list1;
		String[] eachTitle = null;
		String[] eachComment = null;
		String[] eachId = null;
		int count = 0;
		
		if(tab.getPosition() == 1)
		{
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("rides");
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
			
			AdapterForListView adapter = new AdapterForListView(RidesTab.this, eachId, eachTitle, eachComment,R.drawable.rides_icon);
		    list.setAdapter(adapter);
		    
		    list1 = list;
		    
			list1.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View view,
		                int position, long id) {
		        		 Intent myIntent = null;
		        		 myIntent = new Intent(view.getContext(), ViewRidesMyPostActivity.class);
		        		 myIntent.putExtra("condition", "innerRide");
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
				fragment = new AddRidesPost();
			else
				fragment = new MyRidesPostFragment();
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
				return getString(R.string.title_rides_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_rides_section2).toUpperCase(l);
			}
			return null;
		}
	}
	
	public void saveRidesPost(View view)
	{
		Intent intent = new Intent(RidesTab.this,Tab1.class);
		
		rg = (RadioGroup) findViewById(R.id.radioGroup1);
		name = (EditText) findViewById(R.id.name);
		title = (EditText) findViewById(R.id.title);
		destination = (EditText) findViewById(R.id.destination);
		date = (Button) findViewById(R.id.date);
		startTime = (Button) findViewById(R.id.startTime);
		endTime = (Button) findViewById(R.id.endTime);
		mobileNo = (EditText) findViewById(R.id.mobileNo);
		emailId = (EditText) findViewById(R.id.emailId);
		noOfPeople = (EditText) findViewById(R.id.noOfPerson);
		comments = (EditText) findViewById(R.id.comments);
		
		Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.mavbuddy001);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] data = stream.toByteArray();

		
		if(validationRides.isValidName(name,0) && validationRides.isValidTitle(title,0) && validationRides.isValidDestination(destination,0)
				&& validationRides.isValidDate(date,0, this) && validationRides.isValidStartTime(startTime, endTime,0, this) && validationRides.isValidEndTime(endTime, startTime,0, this)
				&& validationRides.isValidMobNo(mobileNo,0) && validationRides.isValidEmail(emailId,0)
				&& validationRides.isValidNoOfPerson(noOfPeople,0) && validationRides.isValidComments(comments,0))
		{
			int TypeofPost = rg.getCheckedRadioButtonId();
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
			ParseFile file = new ParseFile ("mavbuddy.png", data);
			file.saveInBackground();
			
			RadioButton btn = (RadioButton) findViewById(TypeofPost);
			String TypeOfPost = (String) btn.getText();
			
			ParseObject rides = new ParseObject("rides");
			rides.put("username",username);
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
			rides.put("image",file);
			rides.put("spamCount", 0);
			rides.saveInBackground();
			
			
			Bundle b=new Bundle();
			b.putInt("condition",2);
			intent.putExtras(b);
			startActivity(intent);
			
			Context context = getApplicationContext();
			CharSequence text = "Post added successfully!";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}
	
	public void cancelRidesPost(View view)
	{
		Intent intent = new Intent(RidesTab.this,Tab1.class);
		Bundle b=new Bundle();
		b.putInt("condition",2);
		intent.putExtras(b);
		startActivity(intent);
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
