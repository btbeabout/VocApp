package com.btbeabout.vocabapp.Activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.btbeabout.vocabapp.R;
import com.btbeabout.vocabapp.Connectivity.ConnectionChecker;
import com.btbeabout.vocabapp.Model.QuizObject;
import com.btbeabout.vocabapp.ParseConnector.ParseManager;
import com.btbeabout.vocabapp.ParseConnector.ProgressIndicator;
import com.btbeabout.vocabapp.ParseConnector.QuizHistoryParseArrayAdapter;
import com.btbeabout.vocabapp.SharedPrefs.SharedPrefsManager;
import com.google.gson.Gson;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class QuizHistoryActivity extends Activity {

	ListView quizListView;

	QuizHistoryParseArrayAdapter mAdapter;
	SharedPrefsManager mPrefsManager;
	ParseManager mParse;
	ProgressIndicator mDownloadProgress;
	ConnectionChecker mConCheck;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quiz_history_layout);
		
		mPrefsManager = new SharedPrefsManager(this);
		mParse = new ParseManager(retrieveUserName(), this);
		
		quizListView = (ListView) findViewById(R.id.quizHistoryList);
		mAdapter = new QuizHistoryParseArrayAdapter(this, new ArrayList<QuizObject>());
		
		updateList();
		
		/*
		 * Pulls user's quiz history from Parse. Inflates progress dialog to show that information is being downloaded from Parse.
		 * 
		 * Plan for future: allow user to click on each quiz, load new activity showing details of the quiz.
		 * May allow user to retake quiz and update his/her score.
		 * 
		 */

	}
	
	
   public void updateData() {
		mDownloadProgress = new ProgressIndicator(this);
		mDownloadProgress.showProgressRing();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("savedQuiz");
		query.orderByDescending("updatedAt");
		query.whereEqualTo("playerName", retrieveUserName());
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					Gson gson = new Gson();

					System.out.println("Working");

					for (int i = 0; i < objects.size(); i++) {
						QuizObject newDemObj = gson.fromJson(objects.get(i)
								.getString("quizObject").toString(),
								QuizObject.class);
						mAdapter.add(newDemObj);
						System.out.println("Quiz size: "
								+ newDemObj.getQuizSize());
					}
					mDownloadProgress.cancelProgressRing();

				} else {
					System.out.println("Whoops! Exception in Quiz History Updating!");
				}
				
			}

		});
	}
   
	public void updateList() {
		mConCheck = new ConnectionChecker(this);
		if (mConCheck.isConnected()) {
			quizListView.setAdapter(mAdapter);
			updateData();
		} else {
			Toast.makeText(this, "Error retrieving quiz data. Check your internet connection.", Toast.LENGTH_SHORT).show();
		}
	}
   
	public String retrieveUserName() {
		String userName = mPrefsManager.getUsername();
		return userName;
	}
}
