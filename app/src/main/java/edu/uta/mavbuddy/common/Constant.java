package edu.uta.mavbuddy.common;

public class Constant {

	public static String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	public static String USERNAME_PATTERN = "^[a-z0-9_-]{3,15}$";
	
	public static String PASSWORD_PATTERN = "^[a-z0-9_-]{3,15}$";
	
	public static String DATE_FORMAT = "MM-dd-yyyy";
	public static String DATE_TIME_FORMAT = "MM-dd-yyyy hh:mm";
	public static String TIME_FORMAT = "hh:mm";
	
	public static int USERNAME_LENGHT = 15;
	public static int MAX_PASSWORD_LENGHT = 15;
	public static int MIN_PASSWORD_LENGHT = 5;
	public static int NAME_LENGHT = 20;
	public static int TITLE_LENGHT = 20;
	public static int APT_NAME_LENGHT = 20;
	public static int NO_OF_PERSON_LENGHT = 2;
	public static int RENT_LENGHT = 4;
	public static int MOB_NO_LENGHT = 10;
	public static int EMAIL_LENGHT = 30;
	public static int COMMENTS_LENGHT = 100;
	public static int DESTINATION_LENGHT = 20;
	public static int LOCATION_LENGHT = 20;
	public static int DESCRIPTION_LENGHT = 100;
	public static int COST_LENGHT = 5;
}
