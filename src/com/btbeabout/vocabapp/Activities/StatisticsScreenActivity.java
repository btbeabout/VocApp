package com.btbeabout.vocabapp.Activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.btbeabout.vocabapp.R;
import com.btbeabout.vocabapp.Connectivity.ConnectionChecker;
import com.btbeabout.vocabapp.Model.QuizObject;
import com.btbeabout.vocabapp.ParseConnector.ParseManager;
import com.btbeabout.vocabapp.ParseConnector.ProgressIndicator;
import com.btbeabout.vocabapp.ParseConnector.QuestionsStatisticsParseArrayAdapter;
import com.btbeabout.vocabapp.SharedPrefs.SharedPrefsManager;
import com.google.gson.Gson;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class StatisticsScreenActivity extends Activity {

	ParseManager mParse;
	QuestionsStatisticsParseArrayAdapter mAdapter;
	SharedPrefsManager mPrefsManager;
	ProgressIndicator mDownloadProgress;
	ConnectionChecker mConCheck;
	ListView quizDataListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statistics_layout);

		mPrefsManager = new SharedPrefsManager(this);
		mParse = new ParseManager(retrieveUserName(), this);

		quizDataListView = (ListView) findViewById(R.id.quizStatisticsListView);

		mAdapter = new QuestionsStatisticsParseArrayAdapter(this,
				new ArrayList<ParseObject>());

		updateList();

		/*
		 * Downloads "meta" statistical data about the user's quiz activity from
		 * Parse.
		 * 
		 * Plans for future: record how long each quiz takes, provide an average
		 * for how long user took to do quiz, total time taking quizzes, etc.
		 */

	}

	public void updateData() {
		mDownloadProgress = new ProgressIndicator(this);
		mDownloadProgress.showProgressRing();
		ParseQuery<ParseObject> query = ParseQuery
				.getQuery("QuestionsMetaData");
		query.whereEqualTo("playerName", retrieveUserName());
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					System.out.println("Pulling questionsmetadata...");

					mAdapter.add(objects.get(0));
					mDownloadProgress.cancelProgressRing();

				} else {
					System.out
							.println("Whoops! Exception in Quiz History Updating!");
				}
			}

		});
	}

	public void updateList() {
		mConCheck = new ConnectionChecker(this);
		if (mConCheck.isConnected()) {
			quizDataListView.setAdapter(mAdapter);
			updateData();
		} else {
			Toast.makeText(
					this,
					"Error retrieving quiz data. Check your internet connection.",
					Toast.LENGTH_SHORT).show();
		}
	}

	public String retrieveUserName() {
		String userName = mPrefsManager.getUsername();
		System.out.println("PrefsManager: " + userName);
		return userName;
	}

	public void startNewQuiz(View v) {

		Intent i = new Intent(this, QuizScreenActivity.class);
		i.putExtra("quizSize", 20);
		startActivity(i);
		finish();

	}

	public void goToMainMenu(View v) {
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
		finish();
	}

}
