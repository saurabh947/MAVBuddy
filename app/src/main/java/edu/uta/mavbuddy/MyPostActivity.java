package edu.uta.mavbuddy;

import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import edu.uta.mavbuddy.event.MyEventPostFragment;
import edu.uta.mavbuddy.event.ViewEventMyPostActivity;
import edu.uta.mavbuddy.housing.MyHousePostFragment;
import edu.uta.mavbuddy.housing.ViewHousingMyPostActivity;
import edu.uta.mavbuddy.login.HomeScreen;
import edu.uta.mavbuddy.rides.MyRidesPostFragment;
import edu.uta.mavbuddy.rides.ViewRidesMyPostActivity;

public class MyPostActivity extends FragmentActivity implements
		ActionBar.TabListener {

	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	String username;
	SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_post);
		
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
		getMenuInflater().inflate(R.menu.my_post, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		ListView list = (ListView) findViewById(R.id.myPostlistView);
		final ListView list1;
		String[] eachTitle = null;
		String[] eachComment = null;
		String[] eachId = null;
		int count = 0;
		
		if(tab.getPosition() == 0)
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
			
			AdapterForListView adapter = new AdapterForListView(MyPostActivity.this, eachId, eachTitle, eachComment, R.drawable.housing_icon);
		    list.setAdapter(adapter);

		    list1 = list;
		    
			list1.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View view,
		                int position, long id) {
		        		 Intent myIntent = null;
		        		 myIntent = new Intent(view.getContext(), ViewHousingMyPostActivity.class);
		        		 myIntent.putExtra("condition", "outerHousing");
		        		 myIntent.putExtra("postID", list1.getItemAtPosition(position).toString());
		                 startActivityForResult(myIntent, 0);
		            }
		          });
		}
		else if(tab.getPosition() == 1)
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
			
			AdapterForListView adapter = new AdapterForListView(MyPostActivity.this, eachId, eachTitle, eachComment,R.drawable.rides_icon);
		    list.setAdapter(adapter);

		    list1 = list;
		    
			list1.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View view,
		                int position, long id) {
		        		 Intent myIntent = null;
		        		 myIntent = new Intent(view.getContext(), ViewRidesMyPostActivity.class);
		        		 myIntent.putExtra("condition", "outerRide");
		        		 myIntent.putExtra("postID", list1.getItemAtPosition(position).toString());
		                 startActivityForResult(myIntent, 0);
		            }
		          });
		}
		else
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
			
			AdapterForListView adapter = new AdapterForListView(MyPostActivity.this, eachId, eachTitle, eachComment, R.drawable.events_icon);
		    list.setAdapter(adapter);

		    list1 = list;
		    
			list1.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View view,
		                int position, long id) {
		        		 Intent myIntent = null;
		        		 myIntent = new Intent(view.getContext(), ViewEventMyPostActivity.class);
		        		 myIntent.putExtra("condition", "outerEvent");
		        		 myIntent.putExtra("postID", list1.getItemAtPosition(position).toString());
		                 startActivityForResult(myIntent, 0);
		            }
		          });
		}
			    
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		
		ListView list = (ListView) findViewById(R.id.myPostlistView);
		final ListView list1;
		String[] eachTitle = null;
		String[] eachComment = null;
		String[] eachId = null;
		int count = 0;
		
		if(tab.getPosition() == 0)
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
			
			AdapterForListView adapter = new AdapterForListView(MyPostActivity.this, eachId, eachTitle, eachComment, R.drawable.housing_icon);
		    list.setAdapter(adapter);

		    list1 = list;
		    
			list1.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View view,
		                int position, long id) {
		        		 Intent myIntent = null;
		        		 myIntent = new Intent(view.getContext(), ViewHousingMyPostActivity.class);
		        		 myIntent.putExtra("postID", list1.getItemAtPosition(position).toString());
		        		 myIntent.putExtra("condition", "outerHousing");
		                 startActivityForResult(myIntent, 0);
		            }
		          });
		}
		else if(tab.getPosition() == 1)
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
			
			AdapterForListView adapter = new AdapterForListView(MyPostActivity.this, eachId, eachTitle, eachComment,R.drawable.rides_icon);
		    list.setAdapter(adapter);

		    list1 = list;
		    
			list1.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View view,
		                int position, long id) {
		        		 Intent myIntent = null;
		        		 myIntent = new Intent(view.getContext(), ViewRidesMyPostActivity.class);
		        		 myIntent.putExtra("condition", "outerRide");
		        		 myIntent.putExtra("postID", list1.getItemAtPosition(position).toString());
		                 startActivityForResult(myIntent, 0);
		            }
		          });
		}
		else
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
			
			AdapterForListView adapter = new AdapterForListView(MyPostActivity.this, eachId, eachTitle, eachComment, R.drawable.events_icon);
		    list.setAdapter(adapter);

		    list1 = list;
		    
			list1.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View view,
		                int position, long id) {
		        		 Intent myIntent = null;
		        		 myIntent = new Intent(view.getContext(), ViewEventMyPostActivity.class);
		        		 myIntent.putExtra("condition", "outerEvent");
		        		 myIntent.putExtra("postID", list1.getItemAtPosition(position).toString());
		                 startActivityForResult(myIntent, 0);
		            }
		          });
		}
	    
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
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
				fragment = new MyHousePostFragment();
			else if(position == 1)
				fragment = new MyRidesPostFragment();
			else
				fragment = new MyEventPostFragment();
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}
}
