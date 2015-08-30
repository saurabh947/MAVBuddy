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

public class ValidationHousing {

	private Pattern pattern;
	private Matcher matcher;
	
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
	
	public boolean isValidAptName(EditText editText, int flag) throws NumberFormatException
	{
		if (editText.getText().toString().length() <= 0) {
			if(flag == 0)
				editText.setError("Appartment Name must not be empty.");
			return false;
		} else if (editText.getText().toString().length() > Constant.APT_NAME_LENGHT) {
			if(flag == 0)
				editText.setError("Appartment Name must not be more than "+Constant.APT_NAME_LENGHT+" characters.");
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
	
	@SuppressLint("SimpleDateFormat")
	public boolean isValidMoveInDate(Button editText, Button editText1, int flag, Context context) throws NumberFormatException
	{
		SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT);
		if (editText.getText().toString().length() <= 0) {
			if(flag == 0)
			{
				Toast toast = Toast.makeText(context, "Move in Date must not be empty.", Toast.LENGTH_SHORT);
				toast.show();
			}
			return false;
		} else {
			if (editText1.getText().toString().length() > 0)
			{
				try {
					Date moveInDate = sdf.parse(editText.getText().toString());
					Date moveOutDate = sdf.parse(editText1.getText().toString());
					
					if(moveInDate.after(moveOutDate))
					{
						if(flag == 0)
						{
							Toast toast = Toast.makeText(context, "Move in Date must not be greater than move out date.", Toast.LENGTH_SHORT);
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
	public boolean isValidMoveOutDate(Button editText, Button editText1, int flag, Context context) throws NumberFormatException
	{
		SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT);
		if (editText.getText().toString().length() <= 0) {
			if(flag == 0)
			{
				Toast toast = Toast.makeText(context, "Move out Date must not be empty.", Toast.LENGTH_SHORT);
				toast.show();
			}
			return false;
		} else {
			if (editText1.getText().toString().length() > 0)
			{
				try {
					Date moveInDate = sdf.parse(editText1.getText().toString());
					Date moveOutDate = sdf.parse(editText.getText().toString());
					
					if(moveInDate.after(moveOutDate))
					{
						if(flag == 0)
						{
							Toast toast = Toast.makeText(context, "Move out Date must be greater than or equal to move in date.", Toast.LENGTH_SHORT);
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
	
	public boolean isValidRent(EditText editText, int flag) throws NumberFormatException
	{
		if (editText.getText().toString().length() > Constant.RENT_LENGHT) {
			if(flag == 0)
				editText.setError("Rent must not be more than "+Constant.RENT_LENGHT+" digits.");
			return false;
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
