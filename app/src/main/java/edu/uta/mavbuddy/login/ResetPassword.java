package edu.uta.mavbuddy.login;

import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import edu.uta.mavbuddy.R;

public class ResetPassword extends Activity {

	EditText username;
	private String pwdsalt = "*%S6ge#9nt";
	int Pwdhash;
	String Username;
	String randomPwd;
	String userPwd;
	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private String emailTo;
	private String emailFrom = "mavbuddy.uta@gmail.com";
	private String password = "team6mavbuddy";
	private String mailhost = "smtp.gmail.com";
	static Random rnd = new Random();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_password);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reset_password, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void sendTempPassword(View view)
	{
		Intent intent = new Intent(ResetPassword.this, HomeScreen.class);

		username = (EditText) findViewById(R.id.resetUsername);
		Username = username.getText().toString();

		ParseQuery<ParseObject> query = ParseQuery.getQuery("userDetails");
		query.whereEqualTo("username", Username);

		try {
			ParseObject object = query.getFirst();
			// Valid user found
			emailTo = object.getString("emailId");
		}
		catch(ParseException e) {

		}

		randomPwd = "";
		StringBuilder sb = new StringBuilder(8);
		for(int i = 0; i < 8; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		randomPwd = sb.toString();
		userPwd = randomPwd;

		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					Properties properties = System.getProperties();
					// Setup mail server
					properties.put("mail.smtp.starttls.enable", "true");
					properties.put("mail.smtp.host", mailhost);
					properties.put("mail.smtp.user", emailFrom);
					properties.put("mail.smtp.password", password);
					properties.put("mail.smtp.port", "587");
					properties.put("mail.smtp.auth", "true");

					// Get the default Session object.
					Session session = Session.getDefaultInstance(properties);


					// Create a default MimeMessage object.
					MimeMessage message = new MimeMessage(session);
					// Set From: header field of the header.
					message.setFrom(new InternetAddress(emailFrom));
					// Set To: header field of the header.
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
					// Set Subject: header field
					message.setSubject("MavBuddy Support for Username: " + Username);
					// Now set the actual message
					message.setText("Hello!\nThis is MavBuddy Support.\nYour Password has been reset to: " + userPwd + 
							"\nPlease use this temporary password as your old password and-\n\t-Login in the app\n\t-Change your password"
							+ "\nThank you!");
					// Send message
					Transport transport = session.getTransport("smtp");
					transport.connect(mailhost, emailFrom, password);
					transport.sendMessage(message, message.getAllRecipients());
					transport.close();

				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();

		randomPwd = randomPwd + pwdsalt;
		Pwdhash = randomPwd.hashCode();

		ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("userDetails");
		query1.whereEqualTo("username", Username);
		try {
			List<ParseObject> postList = query1.find();
			for (ParseObject user : postList)
			{
				user.put("password", Pwdhash);
				user.put("forgot_password", 1);
				user.saveInBackground();
			}
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		startActivity(intent);
		Context context = getApplicationContext();
		CharSequence text = "E-Mail sent successfully!";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();

	}
}
