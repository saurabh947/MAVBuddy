package edu.uta.mavbuddy;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import edu.uta.mavbuddy.event.EventTab;
import edu.uta.mavbuddy.event.EventsFragment;
import edu.uta.mavbuddy.event.View_event_post;
import edu.uta.mavbuddy.housing.HousingFragment;
import edu.uta.mavbuddy.housing.HousingTab;
import edu.uta.mavbuddy.housing.View_housing_post;
import edu.uta.mavbuddy.login.HomeScreen;
import edu.uta.mavbuddy.rides.RidesFragment;
import edu.uta.mavbuddy.rides.RidesTab;
import edu.uta.mavbuddy.rides.View_ride_post;

@SuppressLint("DefaultLocale")
public class Tab1 extends FragmentActivity implements ActionBar.TabListener {

	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	private String searchKey;
	
	String username;
	SharedPreferences sharedPreferences;
	Editor editor;
	
	//private OnFragmentInteractionListener mListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab1);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		sharedPreferences = getSharedPreferences(HomeScreen.MyPREFERENCES, Context.MODE_PRIVATE);
		username = sharedPreferences.getString(HomeScreen.name,"");
		
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

		Bundle b = getIntent().getExtras();
	    int con = b.getInt("condition");
		// For each of the sections in the app, add a tab to the action bar.
	    Boolean currentTab = false;
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			if(con == (i+1))
				currentTab = true;
			
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this),i,currentTab);
			
			currentTab = false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tab1, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		String[] eachTitle = null;
		String[] eachComment = null;
		String[] eachId = null;
		String[] spamId = null;
		int count = 0;
		ListView list=(ListView) findViewById(R.id.list1);
		final ListView list1;

		
		if(tab.getPosition() == 0)
		{
			int idCount = 0;
			Set<String> idSet = new HashSet<String>();
			ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("reportSpam");
	        query1.whereEqualTo("postType", 0);
	        query1.whereEqualTo("username", username);
			query1.orderByDescending("createdAt");
	        try {
				List<ParseObject> postList = query1.find();
				spamId = new String[postList.size()];
				for (ParseObject postObject : postList)
	            {
					spamId[idCount]=postObject.getString("postId");
	           		idCount=idCount+1;
	            }   
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        
	        for(int i=0;i<spamId.length;i++)
			{
	        	idSet.add(spamId[i]);
			}
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("housing");
	        query.whereNotContainedIn("objectId", idSet);
	        query.whereNotEqualTo("spamCount", 3);
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
			
			
			AdapterForListView adapter = new AdapterForListView(Tab1.this, eachId, eachTitle, eachComment, R.drawable.housing_icon);
		    list=(ListView) findViewById(R.id.list1);
		    list.setAdapter(adapter);
		    
		    list1 = list;
		    
			list1.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View view,
		                int position, long id) {
		        		 Intent myIntent = null;
		        		 myIntent = new Intent(view.getContext(), View_housing_post.class);
		        		 myIntent.putExtra("postID", list1.getItemAtPosition(position).toString());
		                 startActivityForResult(myIntent, 0);
		            }
		          });
		}
			
		else if(tab.getPosition() == 1)
		{
			int idCount = 0;
			Set<String> idSet = new HashSet<String>();
			ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("reportSpam");
	        query1.whereEqualTo("postType", 1);
	        query1.whereEqualTo("username", username);
			query1.orderByDescending("createdAt");
	        try {
				List<ParseObject> postList = query1.find();
				spamId = new String[postList.size()];
				for (ParseObject postObject : postList)
	            {
					spamId[idCount]=postObject.getString("postId");
	           		idCount=idCount+1;
	            }   
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        
	        for(int i=0;i<spamId.length;i++)
			{
	        	idSet.add(spamId[i]);
			}
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("rides");
	        query.whereNotContainedIn("objectId", idSet);
	        query.whereNotEqualTo("spamCount", 3);
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
			
			
			AdapterForListView adapter = new AdapterForListView(Tab1.this, eachId, eachTitle, eachComment,R.drawable.rides_icon);
		    list=(ListView) findViewById(R.id.list1);
		    list.setAdapter(adapter);
		    
		    list1 = list;
		    
			list1.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View view,
		                int position, long id) {
		        		 Intent myIntent = null;
		        		 myIntent = new Intent(view.getContext(), View_ride_post.class);
		        		 myIntent.putExtra("postID", list1.getItemAtPosition(position).toString());
		                 startActivityForResult(myIntent, 0);
		            }
		          });
		}
		else
		{	
			int idCount = 0;
			Set<String> idSet = new HashSet<String>();
			ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("reportSpam");
	        query1.whereEqualTo("postType", 2);
	        query1.whereEqualTo("username", username);
			query1.orderByDescending("createdAt");
	        try {
				List<ParseObject> postList = query1.find();
				spamId = new String[postList.size()];
				for (ParseObject postObject : postList)
	            {
					spamId[idCount]=postObject.getString("postId");
	           		idCount=idCount+1;
	            }   
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        
	        for(int i=0;i<spamId.length;i++)
			{
	        	idSet.add(spamId[i]);
			}
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("events");
	        query.whereNotContainedIn("objectId", idSet);
	        query.whereNotEqualTo("spamCount", 3);
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
			
			
			AdapterForListView adapter = new AdapterForListView(Tab1.this, eachId, eachTitle, eachComment, R.drawable.events_icon);
		    list=(ListView) findViewById(R.id.list1);
		    list.setAdapter(adapter);
		    
		    list1 = list;
		    
			list1.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View view,
		                int position, long id) {
		        		 Intent myIntent = null;
		        		 myIntent = new Intent(view.getContext(), View_event_post.class);
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

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		
		String[] eachTitle = null;
		String[] eachComment = null;
		String[] eachId = null;
		String[] spamId = null;
		int count = 0;
		ListView list = (ListView) findViewById(R.id.list1);
		final ListView list1; 
		
		if(tab.getPosition() == 0)
		{
			int idCount = 0;
			Set<String> idSet = new HashSet<String>();
			ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("reportSpam");
	        query1.whereEqualTo("postType", 0);
	        query1.whereEqualTo("username", username);
			query1.orderByDescending("createdAt");
	        try {
				List<ParseObject> postList = query1.find();
				spamId = new String[postList.size()];
				for (ParseObject postObject : postList)
	            {
					spamId[idCount]=postObject.getString("postId");
	           		idCount=idCount+1;
	            }   
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        
	        for(int i=0;i<spamId.length;i++)
			{
	        	idSet.add(spamId[i]);
			}
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("housing");
	        query.whereNotContainedIn("objectId", idSet);
	        query.whereNotEqualTo("spamCount", 3);
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
			
			
			AdapterForListView adapter = new AdapterForListView(Tab1.this, eachId, eachTitle, eachComment,R.drawable.housing_icon);
		    list=(ListView) findViewById(R.id.list1);
		    list.setAdapter(adapter);

		    list1 = list;
		    
			list1.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View view,
		                int position, long id) {
		        		 Intent myIntent = null;
		        		 myIntent = new Intent(view.getContext(), View_housing_post.class);
		        		 myIntent.putExtra("postID", list1.getItemAtPosition(position).toString());
		                 startActivityForResult(myIntent, 0);
		            }
		          });
		}
			
		else if(tab.getPosition() == 1)
		{
			int idCount = 0;
			Set<String> idSet = new HashSet<String>();
			ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("reportSpam");
	        query1.whereEqualTo("postType", 1);
	        query1.whereEqualTo("username", username);
			query1.orderByDescending("createdAt");
	        try {
				List<ParseObject> postList = query1.find();
				spamId = new String[postList.size()];
				for (ParseObject postObject : postList)
	            {
					spamId[idCount]=postObject.getString("postId");
	           		idCount=idCount+1;
	            }   
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        
	        for(int i=0;i<spamId.length;i++)
			{
	        	idSet.add(spamId[i]);
			}
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("rides");
	        query.whereNotContainedIn("objectId", idSet);
	        query.whereNotEqualTo("spamCount", 3);
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
			
			
			AdapterForListView adapter = new AdapterForListView(Tab1.this, eachId, eachTitle, eachComment,R.drawable.rides_icon);
		    list=(ListView) findViewById(R.id.list1);
		    list.setAdapter(adapter);
		    
		    list1 = list;
		    
			list1.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View view,
		                int position, long id) {
		        		 Intent myIntent = null;
		        		 myIntent = new Intent(view.getContext(), View_ride_post.class);
		        		 myIntent.putExtra("postID", list1.getItemAtPosition(position).toString());
		                 startActivityForResult(myIntent, 0);
		            }
		          });
		}
		else
		{
			int idCount = 0;
			Set<String> idSet = new HashSet<String>();
			ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("reportSpam");
	        query1.whereEqualTo("postType", 2);
	        query1.whereEqualTo("username", username);
			query1.orderByDescending("createdAt");
	        try {
				List<ParseObject> postList = query1.find();
				spamId = new String[postList.size()];
				for (ParseObject postObject : postList)
	            {
					spamId[idCount]=postObject.getString("postId");
	           		idCount=idCount+1;
	            }   
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        
	        for(int i=0;i<spamId.length;i++)
			{
	        	idSet.add(spamId[i]);
			}
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("events");
	        query.whereNotContainedIn("objectId", idSet);
	        query.whereNotEqualTo("spamCount", 3);
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
			
			
			AdapterForListView adapter = new AdapterForListView(Tab1.this, eachId, eachTitle, eachComment, R.drawable.events_icon);
		    list=(ListView) findViewById(R.id.list1);
		    list.setAdapter(adapter);
		    
		    list1 = list;
		    
			list1.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View view,
		                int position, long id) {
		        		 Intent myIntent = null;
		        		 myIntent = new Intent(view.getContext(), View_event_post.class);
		        		 myIntent.putExtra("postID", list1.getItemAtPosition(position).toString());
		                 startActivityForResult(myIntent, 0);
		            }
		          });
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
			Fragment fragment = new Fragment();
			if(position == 0)
				fragment = new HousingFragment();
			if(position == 1)
				fragment = new RidesFragment();
			if(position == 2)
				fragment = new EventsFragment();
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

	public void addHousePost(View view)
	{
		Intent intent = new Intent(Tab1.this,HousingTab.class);
		startActivity(intent);
	}
	
	public void addRidesPost(View view)
	{
		Intent intent = new Intent(Tab1.this,RidesTab.class);
		startActivity(intent);
	}
	
	public void addEventPost(View view)
	{
		Intent intent = new Intent(Tab1.this,EventTab.class);
		startActivity(intent);
	}
	
	public void searchHousePost(View view)
	{
		final ListView list1;
		String[] eachTitle = null;
		String[] eachComment = null;
		String[] eachId = null;
		String spamId[] = null;
		int idCount = 0;
		Set<String> idSet1 = new HashSet<String>();
		ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("reportSpam");
        query1.whereEqualTo("postType", 0);
        query1.whereEqualTo("username", username);
        query1.orderByDescending("createdAt");
        try {
			List<ParseObject> postList = query1.find();
			spamId = new String[postList.size()];
			for (ParseObject postObject : postList)
            {
				spamId[idCount]=postObject.getString("postId");
           		idCount=idCount+1;
            }   
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        for(int i=0;i<spamId.length;i++)
		{
        	idSet1.add(spamId[i]);
		}
		ListView list=(ListView) findViewById(R.id.list1);
		final TextView searchRes = (TextView) findViewById(R.id.searchHousingResult);
		searchRes.setText("");
		EditText search = (EditText) findViewById(R.id.searchHouse);
		searchKey = search.getText().toString();
		String[] eachKeyword;
		String[] eachTuple = new String[1000];
		int count=0;
		String delimiter = "\\s+";
		eachKeyword = searchKey.split(delimiter);
			
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("housing");
		query.whereNotContainedIn("objectId", idSet1);
        query.whereNotEqualTo("spamCount", 3);
        query.orderByDescending("createdAt");
        try {
			List<ParseObject> postList = query.find();
			for (ParseObject postObject : postList)
            {
           			eachTuple[count]=postObject.getObjectId();
           			eachTuple[count+1]=postObject.getString("title");
           			eachTuple[count+2]=postObject.getString("comments");
           			eachTuple[count+3]=postObject.getString("apt_name");
           			eachTuple[count+4]=postObject.getString("type_of_apt");
           			eachTuple[count+5]=postObject.getString("type_of_acc");
           			eachTuple[count+6]=postObject.getString("room_for");
           			count=count+7;
             }   
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        
        Set<String> idSet = new HashSet<String>();
		for(int i=0;i<eachKeyword.length;i++)
		{
	        int index=0;
	        while(index<eachTuple.length)
	        {
	        	if(eachTuple[index] != null)
	        	{
	        		if(eachTuple[index].toLowerCase().contains(eachKeyword[i].toLowerCase()))
	        		{
	        			idSet.add(eachTuple[index-(index%7)]);
	        			index += index % 7;
	        		}
	        	}
	        	index += 1;
	        }
		}
		
		if(idSet.size() != 0)
		{
			count = 0;
			ParseQuery<ParseObject> query3 = new ParseQuery<ParseObject>("housing");
	        query3.whereContainedIn("objectId", idSet);
	        query3.orderByDescending("createdAt");
	        try {
				List<ParseObject> postList = query3.find();
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
			
			
			AdapterForListView adapter = new AdapterForListView(Tab1.this, eachId, eachTitle, eachComment,R.drawable.housing_icon);
		    list=(ListView) findViewById(R.id.list1);
		    list.setAdapter(adapter);

		    list1 = list;
		    
			list1.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View view,
		                int position, long id) {
		        		 Intent myIntent = null;
		        		 myIntent = new Intent(view.getContext(), View_housing_post.class);
		        		 myIntent.putExtra("postID", list1.getItemAtPosition(position).toString());
		                 startActivityForResult(myIntent, 0);
		            }
		          });
		}
		else
		{
			list.setAdapter(null);
			searchRes.setText("No Results Found!");
		}
		
				
		
	}

	public void searchEventPost(View view)
	{
		final ListView list1;
		String[] eachTitle = null;
		String[] eachComment = null;
		String[] eachId = null;
		String spamId[] = null;
		int idCount = 0;
		Set<String> idSet1 = new HashSet<String>();
		ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("reportSpam");
        query1.whereEqualTo("postType", 2);
        query1.whereEqualTo("username", username);
        query1.orderByDescending("createdAt");
        try {
			List<ParseObject> postList = query1.find();
			spamId = new String[postList.size()];
			for (ParseObject postObject : postList)
            {
				spamId[idCount]=postObject.getString("postId");
           		idCount=idCount+1;
            }   
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        for(int i=0;i<spamId.length;i++)
		{
        	idSet1.add(spamId[i]);
		}
		
		ListView list=(ListView) findViewById(R.id.list1);
		final TextView searchRes = (TextView) findViewById(R.id.searchEventsResult);
		searchRes.setText("");
		EditText search = (EditText) findViewById(R.id.searchEvent);
		searchKey = search.getText().toString();
		final String[] eachKeyword;
		String delimiter = "\\s+";
		String[] eachTuple = new String[1000];
		int count=0;
		eachKeyword = searchKey.split(delimiter);
		
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("events");
        query.whereNotContainedIn("objectId", idSet1);
        query.whereNotEqualTo("spamCount", 3);
        query.orderByDescending("createdAt");
        try {
			List<ParseObject> postList = query.find();
			for (ParseObject postObject : postList)
            {
           			eachTuple[count]=postObject.getObjectId();
           			eachTuple[count+1]=postObject.getString("title");
           			eachTuple[count+2]=postObject.getString("description");
           			eachTuple[count+3]=postObject.getString("location");
           			count=count+4;
             }   
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        Set<String> idSet = new HashSet<String>();
		for(int i=0;i<eachKeyword.length;i++)
		{
	        int index=0;
	        while(index<eachTuple.length)
	        {
	        	if(eachTuple[index] != null)
	        	{
	        		if(eachTuple[index].toLowerCase().contains(eachKeyword[i].toLowerCase()))
	        		{
	        			idSet.add(eachTuple[index-(index%4)]);
	        			index += index % 4;
	        		}
	        	}
	        	index += 1;
	        }
		}
		
		if(idSet.size() != 0)
		{
			count = 0;
			ParseQuery<ParseObject> query2 = new ParseQuery<ParseObject>("events");
	        query2.whereContainedIn("objectId", idSet);
	        query2.whereNotEqualTo("spamCount", 3);
	        query2.orderByDescending("createdAt");
	        try {
				List<ParseObject> postList = query2.find();
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
			
			
			AdapterForListView adapter = new AdapterForListView(Tab1.this, eachId, eachTitle, eachComment, R.drawable.events_icon);
			list.setAdapter(adapter);
			
			list1 = list;
			
			list1.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View view,
		                int position, long id) {
		        		 Intent myIntent = null;
		        		 myIntent = new Intent(view.getContext(), View_event_post.class);
		        		 myIntent.putExtra("postID", list1.getItemAtPosition(position).toString());
		                 startActivityForResult(myIntent, 0);
		            }
		          });
		}
		else
		{
			list.setAdapter(null);
			searchRes.setText("No Results Found!");
		}
		
	}

	
	public void searchRidesPost(View view)
	{
		final ListView list1;
		String[] eachTitle = null;
		String[] eachComment = null;
		String[] eachId = null;
		String spamId[] = null;
		int idCount = 0;
		Set<String> idSet1 = new HashSet<String>();
		ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("reportSpam");
        query1.whereEqualTo("postType", 1);
        query1.whereEqualTo("username", username)
;			query1.orderByDescending("createdAt");
        try {
			List<ParseObject> postList = query1.find();
			spamId = new String[postList.size()];
			for (ParseObject postObject : postList)
            {
				spamId[idCount]=postObject.getString("postId");
           		idCount=idCount+1;
            }   
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        for(int i=0;i<spamId.length;i++)
		{
        	idSet1.add(spamId[i]);
		}
		ListView list=(ListView) findViewById(R.id.list1);
		final TextView searchRes = (TextView) findViewById(R.id.searchRidesResult);
		searchRes.setText("");
		EditText search = (EditText) findViewById(R.id.searchRides);
		searchKey = search.getText().toString();
		final String[] eachKeyword;
		String delimiter = "\\s+";
		eachKeyword = searchKey.split(delimiter);
		String[] eachTuple = new String[1000];
		int count=0;
		
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("rides");
		query.whereNotContainedIn("objectId", idSet1);
        query.whereNotEqualTo("spamCount", 3);
        query.orderByDescending("createdAt");
        try {
			List<ParseObject> postList = query.find();
			for (ParseObject postObject : postList)
            {
           			eachTuple[count]=postObject.getObjectId();
           			eachTuple[count+1]=postObject.getString("title");
           			eachTuple[count+2]=postObject.getString("comments");
           			eachTuple[count+3]=postObject.getString("destination");
           			count=count+4;
             }   
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        
        Set<String> idSet = new HashSet<String>();
		for(int i=0;i<eachKeyword.length;i++)
		{
	        int index=0;
	        while(index<eachTuple.length)
	        {
	        	if(eachTuple[index] != null)
	        	{
	        		if(eachTuple[index].toLowerCase().contains(eachKeyword[i].toLowerCase()))
	        		{
	        			idSet.add(eachTuple[index-(index%4)]);
	        			index += index % 4;
	        		}
	        	}
	        	index += 1;
	        }
		}
		
		if(idSet.size() != 0)
		{
			count = 0;
			ParseQuery<ParseObject> query3 = new ParseQuery<ParseObject>("rides");
	        query3.whereContainedIn("objectId", idSet);
	        query3.whereNotEqualTo("spamCount", 3);
	        query3.orderByDescending("createdAt");
	        try {
				List<ParseObject> postList = query3.find();
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
			
			
			AdapterForListView adapter = new AdapterForListView(Tab1.this, eachId, eachTitle, eachComment,R.drawable.rides_icon);
		    list=(ListView) findViewById(R.id.list1);
		    list.setAdapter(adapter);
		    
		    list1 = list;
		    
			list1.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View view,
		                int position, long id) {
		        		 Intent myIntent = null;
		        		 myIntent = new Intent(view.getContext(), View_ride_post.class);
		        		 myIntent.putExtra("postID", list1.getItemAtPosition(position).toString());
		                 startActivityForResult(myIntent, 0);
		            }
		          });
		}
		else
		{
			list.setAdapter(null);
			searchRes.setText("No Results Found!");
		}
	}
}
