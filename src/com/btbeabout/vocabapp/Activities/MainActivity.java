package com.btbeabout.vocabapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.btbeabout.vocabapp.R;
import com.btbeabout.vocabapp.SharedPrefs.SharedPrefsManager;

public class MainActivity extends Activity {
	SharedPrefsManager mPrefsManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mPrefsManager = new SharedPrefsManager(this);
		
		/*
		 *  Loads preference manager when app opens and either requests the user enter a username for Parse, or welcomes the user
		 *  in a toast with the username s/he has already entered.
		 *  
		 *  Plans for future: remove preference manager, create signup/login with Parse.
		 */
		
		mPrefsManager.loadPrefsOnAppOpen();
	}

	// Loads a 20-question quiz. May allow quizzes of different sizes in the future.
	
	public void StartQuizActivity(View v) {
		Intent i = new Intent(this, QuizScreenActivity.class);
		i.putExtra("quizSize", 20);
		startActivity(i);
	}
	
	public void StartStatisticsActivity(View v) {
		Intent i = new Intent(this, StatisticsScreenActivity.class);
		startActivity(i);
	}

	public void StartQuizHistory(View v) {
		Intent i = new Intent(this, QuizHistoryActivity.class);
		startActivity(i);
	}
	
	public void StartLeaderboardActivity (View v) {
		Intent i = new Intent(this, LeaderboardActivity.class);
		startActivity(i);
	}

}
