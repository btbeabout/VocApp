package com.btbeabout.vocabapp.SharedPrefs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.btbeabout.vocabapp.R;

public class SharedPrefsManager {

	Context mContext;
	SharedPreferences nameSettings;

	public static final String PREFS_NAME = "VocAppPrefsFile";

	/*
	 * Currently, this PrefsManager exists to store a username the user enters
	 * when they first start the app. This is important because the username is
	 * used to tag all of the quiz data stored to Parse, allowing it to be
	 * identified and retrieved on a user-basis.
	 * 
	 * User cannot currently switch usernames once saved to preferences. May also result in duplicate usernames.
	 * 
	 * Plans for the future: Remove PrefsManager, replace it with signup/login on Parse. Enforce unique usernames.
	 *
	 */

	public SharedPrefsManager(Context context) {
		mContext = context;
		nameSettings = mContext.getSharedPreferences(PREFS_NAME, 0);
	}

	public void loadPrefsOnAppOpen() {
		boolean firstRun = nameSettings.getBoolean("firstRun", true);

		if (firstRun) {
			firstRunWelcome();
		} else {
			returnGreetingToast(mContext);
		}
	}

	public void firstRunWelcome() {
		toastGreeting(mContext);
		nameChoosingDialog(mContext);
	}

	public void toastGreeting(Context context) {
		Toast.makeText(context, "Welcome to VocApp!", Toast.LENGTH_SHORT)
				.show();
	}

	public void toastNameRequest(Context context) {
		Toast.makeText(context,
				"Please enter a username of at least three characters.",
				Toast.LENGTH_SHORT).show();
	}

	public void toastThankYou(Context context) {
		Toast.makeText(context, "Thank you!", Toast.LENGTH_SHORT).show();
	}

	public void returnGreetingToast(Context context) {
		String userName = nameSettings.getString("userName", "no name entered");
		Toast.makeText(context, "Welcome, " + userName, Toast.LENGTH_SHORT)
				.show();
	}

	public String getUsername() {
		System.out.println("RETRIEVING USERNAME...");
		System.out.println(nameSettings
				.getString("userName", "no name entered"));
		return nameSettings.getString("userName", "no name entered");
	}

	public void nameChoosingDialog(Context context) {
		final Dialog welcomeDialog = new Dialog(context);
		welcomeDialog.setContentView(R.layout.welcome_dialog);
		welcomeDialog.setTitle("Welcome To VocApp!");
		welcomeDialog.setCancelable(false);

		TextView welcomeText = (TextView) welcomeDialog
				.findViewById(R.id.tvWelcomeDialog);
		welcomeText.setText("Please enter a username!");
		final EditText edit = (EditText) welcomeDialog
				.findViewById(R.id.etWelcomeDialog);
		Button dialogButton = (Button) welcomeDialog
				.findViewById(R.id.bWelcomeDialog);

		dialogButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				if (edit.getText().toString().length() < 3) {
					toastNameRequest(mContext);
				} else {
					toastThankYou(mContext);
					String userName = edit.getText().toString();

					SharedPreferences.Editor nameSettingsEditor = nameSettings
							.edit();
					nameSettingsEditor.putString("userName", userName);
					nameSettingsEditor.putBoolean("firstRun", false);
					nameSettingsEditor.commit();
					welcomeDialog.dismiss();
				}
			}
		});

		welcomeDialog.show();
	}
}
