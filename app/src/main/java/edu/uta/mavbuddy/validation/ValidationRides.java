package edu.uta.mavbuddy.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import edu.uta.mavbuddy.common.Constant;

public class ValidationRides {

	private Pattern pattern;
	private Matcher matcher;
	@SuppressLint("SimpleDateFormat")
	SimpleDateFormat sdf = new SimpleDateFormat(Constant.TIME_FORMAT);
	
	public boolean isValidName(EditText editText, int flag) throws NumberFormatException
	{
		if (editText.getText().toString().length() <= 0) {
			if(flag == 0)
				editText.setError("Name must not be empty.");
			return false;
		} else if (editText.getText().toString().length() > Constant.NAME_LENGHT) {
			if(flag == 0)
				editText.setError("Name must not be more than "+Constant.NAME_LENGHT+" characters.");
			return false;
		}
		return true;
	}
	
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
	
	public boolean isValidDestination(EditText editText, int flag) throws NumberFormatException
	{
		if (editText.getText().toString().length() <= 0) {
			if(flag == 0)
				editText.setError("Destination must not be empty.");
			return false;
		} else if (editText.getText().toString().length() > Constant.DESTINATION_LENGHT) {
			if(flag == 0)
				editText.setError("Destination must not be more than "+Constant.DESTINATION_LENGHT+" characters.");
			return false;
		}
		return true;
	}
	
	public boolean isValidDate(Button editText, int flag, Context context) throws NumberFormatException
	{
		if (editText.getText().toString().length() <= 0) {
			if(flag == 0)
			{
				Toast toast = Toast.makeText(context, "Date must not be empty.", Toast.LENGTH_SHORT);
				toast.show();
			}
			return false;
		}
		return true;
	}
	
	public boolean isValidStartTime(Button editText, Button editText1, int flag, Context context) throws NumberFormatException
	{
		if (editText.getText().toString().length() <= 0) {
			if(flag == 0)
			{
				Toast toast = Toast.makeText(context, "Start Time not be empty.", Toast.LENGTH_SHORT);
				toast.show();
			}
			return false;
		} 
		else
		{
			if (editText1.getText().toString().length() > 0)
			{
				try {
					Date startTime = sdf.parse(editText.getText().toString());
					Date endTime = sdf.parse(editText1.getText().toString());
					
					if(startTime.after(endTime))
					{
						if(flag == 0)
						{
							Toast toast = Toast.makeText(context, "Start Time must not be greater than End Time.", Toast.LENGTH_SHORT);
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
	
	public boolean isValidEndTime(Button editText, Button editText1, int flag, Context context) throws NumberFormatException
	{
		if (editText.getText().toString().length() <= 0) {
			if(flag == 0)
			{
				Toast toast = Toast.makeText(context, "End Time must not be empty.", Toast.LENGTH_SHORT);
				toast.show();
			}
			return false;
		} 
		else 
		{
			if (editText1.getText().toString().length() > 0)
			{
				try {
					Date startTime = sdf.parse(editText1.getText().toString());
					Date endTime = sdf.parse(editText.getText().toString());
					
					if(startTime.after(endTime))
					{
						if(flag == 0)
						{
							Toast toast = Toast.makeText(context, "End Time must be greater than or equal to Start Time.", Toast.LENGTH_SHORT);
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
	
	public boolean isValidMobNo(EditText editText, int flag) throws NumberFormatException
	{
		if (editText.getText().toString().length() > 0 && editText.getText().toString().length() != Constant.MOB_NO_LENGHT) {
			if(flag == 0)
				editText.setError("Mobile Number must be exact "+Constant.MOB_NO_LENGHT+" digits.");
			return false;
		}
		return true;
	}
	
	public boolean isValidEmail(EditText editText, int flag) throws NumberFormatException
	{
		pattern = Pattern.compile(Constant.EMAIL_PATTERN);
		
		if (editText.getText().toString().length() <= 0) {
			if(flag == 0)
				editText.setError("Email must not be empty.");
			return false;
		} else {
			matcher = pattern.matcher(editText.getText().toString());
			if(!matcher.matches())
			{
				if(flag == 0)
					editText.setError("Email is not in proper format.");
				return false;
			}
		}
		
		if (editText.getText().toString().length() > Constant.EMAIL_LENGHT) {
			if(flag == 0)
				editText.setError("Email must not be more than "+Constant.EMAIL_LENGHT+" characters.");
			return false;
		}
		return true;
	}
	
	public boolean isValidNoOfPerson(EditText editText, int flag) throws NumberFormatException
	{
		if (editText.getText().toString().length() <= 0) {
			if(flag == 0)
				editText.setError("Number of Person must not be empty.");
			return false;
		} else if (editText.getText().toString().length() > Constant.NO_OF_PERSON_LENGHT) {
			if(flag == 0)
				editText.setError("Number of Person must not be more than "+Constant.NO_OF_PERSON_LENGHT+" digits.");
			return false;
		}
		return true;
	}
	
	public boolean isValidComments(EditText editText, int flag) throws NumberFormatException
	{
		if (editText.getText().toString().length() > Constant.COMMENTS_LENGHT) {
			if(flag == 0)
				editText.setError("Comments must not be more than "+Constant.COMMENTS_LENGHT+" characters.");
			return false;
		}
		return true;
	}
}
