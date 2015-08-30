package edu.uta.mavbuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterForListView extends ArrayAdapter<String>{

	private final Context context;
	private final String[] id;
	private final String[] title;
	private final String[] comments;
	private final Integer drawableInt;

	  public AdapterForListView(Context context, String[] id, String[] title, String[] comments, Integer drawableInt) {
	    super(context, R.layout.list_view_layout, id);
	    this.context = context;
	    this.id = id;
	    this.title=title;
	    this.comments=comments;
	    this.drawableInt=drawableInt;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
		  
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.list_view_layout, parent, false);
	    TextView textView1 = (TextView) rowView.findViewById(R.id.firstLine);
	    TextView textView2 = (TextView) rowView.findViewById(R.id.secondLine);
	    TextView textView3 = (TextView) rowView.findViewById(R.id.idLine);
	    ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
	    textView1.setText(title[position]);
	    textView2.setText(comments[position]);
	    textView3.setText(id[position]); 
	    imageView.setImageResource(drawableInt);

	    return rowView;
	  }
}
