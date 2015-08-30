package edu.uta.mavbuddy.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.uta.mavbuddy.common.Constant;

import android.widget.EditText;

public class ValidationRegister {

	private Pattern pattern;
	private Matcher matcher;
 
	public boolean isValidUsername(EditText editText, int flag) throws NumberFormatException
	{
		if (editText.getText().toString().length() <= 0) {
			if(flag == 0)
				editText.setError("Username must not be empty.");
			return false;
		} else if (editText.getText().toString().length() > Constant.USERNAME_LENGHT) {
			if(flag == 0)
				editText.setError("Username must not be more than "+Constant.USERNAME_LENGHT+" characters.");
			return false;
		} else {
			pattern = Pattern.compile(Constant.USERNAME_PATTERN);
			matcher = pattern.matcher(editText.getText().toString());
			if(! matcher.matches())
			{
				if(flag == 0)
					editText.setError("Username can contain lower case letters, digits or _");
				return false;
			}
		}
		return true;
	}
	
	public boolean isValidPassword(EditText editText,EditText editText1, int flag) throws NumberFormatException
	{
		if (editText.getText().toString().length() <= 0) {
			if(flag == 0)
				editText.setError("Password must not be empty.");
			return false;
		} else if (editText.getText().toString().length() > 0 && editText1.getText().toString().length() > 0){
			if(!editText.getText().toString().equals(editText1.getText().toString()))
			{
				if(flag == 0)
					editText.setError("Password and Verify Password does not match.");
				return false;
			}
		} else if (editText.getText().toString().length() > Constant.MAX_PASSWORD_LENGHT || editText.getText().toString().length() < Constant.MIN_PASSWORD_LENGHT) {
			if(flag == 0)
				editText.setError("Password must be between "+Constant.MIN_PASSWORD_LENGHT+" and "+Constant.MAX_PASSWORD_LENGHT+" characters.");
			return false;
		} else {
			pattern = Pattern.compile(Constant.PASSWORD_PATTERN);
			matcher = pattern.matcher(editText.getText().toString());
			if(! matcher.matches())
			{
				if(flag == 0)
					editText.setError("Password can contain lower case letters, digits or _");
				return false;
			}
		}
		return true;
	}
	
	public boolean isValidVerifyPassword(EditText editText,EditText editText1, int flag) throws NumberFormatException
	{
		if (editText.getText().toString().length() <= 0) {
			if(flag == 0)
				editText.setError("Verify Password must not be empty.");
			return false;
		} else if (editText.getText().toString().length() > 0 && editText1.getText().toString().length() > 0){
			if(!editText.getText().toString().equals(editText1.getText().toString()))
			{
				if(flag == 0)
					editText.setError("Password and Verify Password does not match.");
				return false;
			}
		} else if (editText.getText().toString().length() > Constant.MAX_PASSWORD_LENGHT || editText.getText().toString().length() < Constant.MIN_PASSWORD_LENGHT) {
			if(flag == 0)
				editText.setError("Password must be between "+Constant.MIN_PASSWORD_LENGHT+" and "+Constant.MAX_PASSWORD_LENGHT+" characters.");
			return false;
		} else {
			pattern = Pattern.compile(Constant.PASSWORD_PATTERN);
			matcher = pattern.matcher(editText.getText().toString());
			if(! matcher.matches())
			{
				if(flag == 0)
					editText.setError("Password can contain lower case letters, digits or _");
				return false;
			}
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
}
