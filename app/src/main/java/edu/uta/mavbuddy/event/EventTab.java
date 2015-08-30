package edu.uta.mavbuddy.event;

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
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import edu.uta.mavbuddy.AdapterForListView;
import edu.uta.mavbuddy.R;
import edu.uta.mavbuddy.Tab1;
import edu.uta.mavbuddy.login.HomeScreen;
import edu.uta.mavbuddy.validation.ValidationEvent;

public class EventTab extends FragmentActivity implements ActionBar.TabListener {

	SectionsPagerAdapter mSectionsPagerAdapter;

	ViewPager mViewPager;
	EditText title;
	EditText location;
	Button startDate;
	Button startTime;
	Button endDate;
	Button endTime;
	EditText cost;
	EditText description;
	ValidationEvent validationEvent = new ValidationEvent();
	ParseUser currentUser;
	private String username;
	SharedPreferences sharedPreferences;
	int start_year, start_month, start_day,end_year, end_month, end_day,start_hr,start_min,end_hr,end_min;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Parse.initialize(this, "NMTwPKAweEH0MrGH5UAIdE8nKC9mc3GkMlvCGALE", "I35KGgtQ2giCz0Ig3QqraJOiorDuxVhdKlUL4Jd5");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_tab);
		
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
		getMenuInflater().inflate(R.menu.event_tab, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		ListView list = (ListView) findViewById(R.id.eventlistView);
		final ListView list1;
		String[] eachTitle = null;
		String[] eachComment = null;
		String[] eachId = null;
		int count = 0;
		
		if(tab.getPosition() == 1)
		{
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("events");
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
	           			eachComment[count]=postObject.getString("description");
	           			count=count+1;
	             }   
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			AdapterForListView adapter = new AdapterForListView(EventTab.this, eachId, eachTitle, eachComment, R.drawable.events_icon);
		    list.setAdapter(adapter);
		    
		    list1 = list;
		    
			list1.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View view,
		                int position, long id) {
		        		 Intent myIntent = null;
		        		 myIntent = new Intent(view.getContext(), ViewEventMyPostActivity.class);
		        		 myIntent.putExtra("condition", "innerEvent");
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
		
		ListView list = (ListView) findViewById(R.id.eventlistView);
		final ListView list1;
		String[] eachTitle = null;
		String[] eachComment = null;
		String[] eachId = null;
		int count = 0;
		
		if(tab.getPosition() == 1)
		{
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("events");
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
	           			eachComment[count]=postObject.getString("description");
	           			count=count+1;
	             }   
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			AdapterForListView adapter = new AdapterForListView(EventTab.this, eachId, eachTitle, eachComment, R.drawable.events_icon);
		    list.setAdapter(adapter);
		    
		    list1 = list;
		    
			list1.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View view,
		                int position, long id) {
		        		 Intent myIntent = null;
		        		 myIntent = new Intent(view.getContext(), ViewEventMyPostActivity.class);
		        		 myIntent.putExtra("condition", "innerEvent");
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
				fragment = new AddEventPost();
			else
				fragment = new MyEventPostFragment();
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
				return getString(R.string.title_event_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_event_section2).toUpperCase(l);
			}
			return null;
		}
	}
	
	public void saveEventPost(View view)
	{
		Intent intent = new Intent(EventTab.this,Tab1.class);
		
		title = (EditText) findViewById(R.id.title);
		location = (EditText) findViewById(R.id.location);
		startDate = (Button) findViewById(R.id.startDate);
		startTime = (Button) findViewById(R.id.startTime);
		endDate = (Button) findViewById(R.id.endDate);
		endTime = (Button) findViewById(R.id.endTime);
		cost = (EditText) findViewById(R.id.cost);
		description = (EditText) findViewById(R.id.description);
		
		Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.mavbuddy001);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] data = stream.toByteArray();

		
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
			ParseFile file = new ParseFile ("mavbuddy.png", data);
			file.saveInBackground();
			
			ParseObject events = new ParseObject("events");
			events.put("username", username);
			events.put("title", Title);
			events.put("location", Location);
			events.put("start_time", StartDate+" "+StartTime);
			events.put("end_time", EndDate+" "+EndTime);
			events.put("cost", Cost);
			events.put("description", Description);
			events.put("image",file);
			events.put("spamCount",0);
			events.saveInBackground();
			
			
			Bundle b=new Bundle();
			b.putInt("condition",3);
			intent.putExtras(b);
			startActivity(intent);
			
			Context context = getApplicationContext();
			CharSequence text = "Post added successfully!";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}
	
	public void cancelEventPost(View view)
	{
		Intent intent = new Intent(EventTab.this,Tab1.class);
		Bundle b=new Bundle();
		b.putInt("condition",3);
		intent.putExtras(b);
		startActivity(intent);
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
