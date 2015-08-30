package edu.uta.mavbuddy.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import edu.uta.mavbuddy.common.Constant;

public class ValidationEvent {

	@SuppressLint("SimpleDateFormat")
	SimpleDateFormat sdfDate = new SimpleDateFormat(Constant.DATE_FORMAT);
	@SuppressLint("SimpleDateFormat")
	SimpleDateFormat sdfDateTime = new SimpleDateFormat(Constant.DATE_TIME_FORMAT);
	
	public boolean isValidTitle(EditText editText, int flag) throws NumberFormatException
	{
		if (editText.getText().toString().length() <= 0) {
			if(flag == 0)
				editText.setError("Title must not be empty.");
			return false;
		} else if (editText.getText().toString().length() > Constant.TITLE_LENGHT) {
			if(flag == 0)
				editText.setError("Title must not be more than "+Constant.TITLE_LENGHT+" characters.");
			return false;
		}
		return true;
	}
	
	public boolean isValidLocation(EditText editText, int flag) throws NumberFormatException
	{
		if (editText.getText().toString().length() <= 0) {
			if(flag == 0)
				editText.setError("Location must not be empty.");
			return false;
		} else if (editText.getText().toString().length() > Constant.LOCATION_LENGHT) {
			if(flag == 0)
				editText.setError("Location must not be more than "+Constant.LOCATION_LENGHT+" characters.");
			return false;
		}
		return true;
	}
	
	public boolean isValidStartDate(Button editText, int flag, Context context) throws NumberFormatException
	{
		if (editText.getText().toString().length() <= 0) {
			if(flag == 0)
			{
				Toast toast = Toast.makeText(context, "Start Date must not be empty.", Toast.LENGTH_SHORT);
				toast.show();
			}
			return false;
		} 
		return true;
	}
	
	@SuppressLint("SimpleDateFormat")
	public boolean isValidStartTime(Button editText, int flag, Context context) throws NumberFormatException
	{
		if (editText.getText().toString().length() <= 0) {
			if(flag == 0)
			{
				Toast toast = Toast.makeText(context, "Start Time must not be empty.", Toast.LENGTH_SHORT);
				toast.show();
			}
			return false;
		} 		
		return true;
	}
	
	public boolean isValidEndDate(Button editText,Button editText1, int flag, Context context) throws NumberFormatException
	{
		if (editText.getText().toString().length() <= 0) {
			if(flag == 0)
			{
				Toast toast = Toast.makeText(context, "End Date must not be empty.", Toast.LENGTH_SHORT);
				toast.show();
			}
			return false;
		} 
		else
		{
			if (editText1.getText().toString().length() > 0)
			{
				try {
					Date startDate = sdfDate.parse(editText1.getText().toString());
					Date endDate = sdfDate.parse(editText.getText().toString());
					
					if(startDate.after(endDate))
					{
						if(flag == 0)
						{
							Toast toast = Toast.makeText(context, "End Date must be less than Start Date.", Toast.LENGTH_SHORT);
							toast.show();
						}
						return false;
					}
					
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
	
	@SuppressLint("SimpleDateFormat")
	public boolean isValidEndTime(Button editText, Button editText1,Button editText2, Button editText3, int flag, Context context) throws NumberFormatException
	{
		if (editText.getText().toString().length() <= 0) {
			if(flag == 0)
			{
				Toast toast = Toast.makeText(context, "End Time must not be empty.", Toast.LENGTH_SHORT);
				toast.show();
			}
			return false;
		} else {
			if (editText1.getText().toString().length() > 0 && editText2.getText().toString().length() > 0 && editText3.getText().toString().length() > 0)
			{
				try {
					Date startTime = sdfDateTime.parse(editText3.getText().toString()+" "+editText1.getText().toString());
					Date endTime = sdfDateTime.parse(editText2.getText().toString()+" "+editText.getText().toString());
					
					if(startTime.after(endTime))
					{
						if(flag == 0)
						{
							Toast toast = Toast.makeText(context, "End Time must be greater than or equal to Start Time..", Toast.LENGTH_SHORT);
							toast.show();
						}
						return false;
					}
					
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		
		return true;
	}
	
	public boolean isValidCost(EditText editText, int flag) throws NumberFormatException
	{
		if (editText.getText().toString().length() > 0 && editText.getText().toString().length() > Constant.COST_LENGHT) {
			if(flag == 0)
				editText.setError("Cost must not be more than "+Constant.COST_LENGHT+" digits.");
			return false;
		}
		return true;
	}
	
	public boolean isValidDescription(EditText editText, int flag) throws NumberFormatException
	{
		if (editText.getText().toString().length() > Constant.DESCRIPTION_LENGHT) {
			if(flag == 0)
				editText.setError("Description must not be more than "+Constant.DESCRIPTION_LENGHT+" characters.");
			return false;
		}
		return true;
	}
}
