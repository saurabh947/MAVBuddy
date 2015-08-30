package edu.uta.mavbuddy.validation;

import android.widget.EditText;

public class ValidationLogin {

	public boolean isValidUsername(EditText editText, int flag) throws NumberFormatException
	{
		if (editText.getText().toString().length() <= 0) {
			if(flag == 0)
				editText.setError("Username must not be empty.");
			return false;
		}
		return true;
	}
	
	public boolean isValidPassword(EditText editText, int flag) throws NumberFormatException
	{
		if (editText.getText().toString().length() <= 0) {
			if(flag == 0)
				editText.setError("Password must not be empty.");
			return false;
		} 
		return true;
	}
}
