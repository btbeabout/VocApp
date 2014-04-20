package com.btbeabout.vocabapp.Activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Toast;

import com.btbeabout.vocabapp.R;
import com.btbeabout.vocabapp.Connectivity.ConnectionChecker;
import com.btbeabout.vocabapp.ParseConnector.LeaderboardParseArrayAdapter;
import com.btbeabout.vocabapp.ParseConnector.ParseManager;
import com.btbeabout.vocabapp.ParseConnector.ProgressIndicator;
import com.btbeabout.vocabapp.SharedPrefs.SharedPrefsManager;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class LeaderboardActivity extends Activity implements OnClickListener,
		OnMenuItemClickListener {

	LeaderboardParseArrayAdapter mAdapter;
	SharedPrefsManager mPrefsManager;
	Button mCriteriaMenuOpener;
	PopupMenu mPopup;
	ListView mLeaderboardListView;
	ParseManager mParse;
	ProgressIndicator mDownloadProgress;
	ConnectionChecker mConCheck;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.leaderboard_layout);

		mPrefsManager = new SharedPrefsManager(this);
		mParse = new ParseManager(retrieveUserName(), this);

		mLeaderboardListView = (ListView) findViewById(R.id.leaderboardListView);

		/*
		 *  Creates menu which allows user to sort leaderboard by various criteria, detailed below:
		 *  Questions answered, questions skipped, correct, incorrect, etc.
		 *  
		 *  On each load, a dialog box is inflated to show the user that information is being downloaded from Parse.
		 *  If no connection is available, prompts user with a toast to try again later when a connection is established.
		 */
		
		mCriteriaMenuOpener = (Button) findViewById(R.id.button3);
		mCriteriaMenuOpener.setOnClickListener(this);
		mPopup = new PopupMenu(this, mCriteriaMenuOpener);
		MenuInflater mInflater1 = mPopup.getMenuInflater();
		mInflater1.inflate(R.menu.sorting_menu, mPopup.getMenu());
		mPopup.setOnMenuItemClickListener(this);

		
		updateList("totalCorrectAnswersAmount");
	}

	public void setAdapter(String questionDataQuery) {
		mAdapter = new LeaderboardParseArrayAdapter(this,
				new ArrayList<ParseObject>(), questionDataQuery);
		mLeaderboardListView.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button3:
			mPopup.show();
		}
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.criteriaSortCorrectAnswers:
			updateList("totalCorrectAnswersAmount");
			mCriteriaMenuOpener.setText("Sort By: Correct Answers");
			return true;
		case R.id.criteriaSortInorrectAnswers:
			updateList("totalIncorrectAnswersAmount");
			mCriteriaMenuOpener.setText("Sort By: Incorrect Answers");
			return true;
		case R.id.criteriaSortSkippedQuestions:
			updateList("totalSkippedQuestionsAmount");
			mCriteriaMenuOpener.setText("Sort By: Skipped Questions");
			return true;
		case R.id.criteriaSortTotalQuestions:
			updateList("totalQuestionsAmount");
			mCriteriaMenuOpener.setText("Sort By: Total Questions");
			return true;
		default:
			return false;
		}
	}

	public void updateData(String questionDataQuery) {
		mDownloadProgress = new ProgressIndicator(this);
		mDownloadProgress.showProgressRing();
		ParseQuery<ParseObject> query = ParseQuery
				.getQuery("QuestionsMetaData");
		query.setLimit(5);
		query.orderByDescending(questionDataQuery);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					for (ParseObject obj : objects) {
						mAdapter.add(obj);
					}
					mDownloadProgress.cancelProgressRing();

				} else {
					System.out
							.println("Whoops! Exception in Quiz History Updating!");
				}
			}

		});
	}

	public void updateList(String criteria) {
		mConCheck = new ConnectionChecker(this);
		if (mConCheck.isConnected()) {
			setAdapter(criteria);
			updateData(criteria);
		} else {
			Toast.makeText(this, "Error retrieving quiz data. Check your internet connection.", Toast.LENGTH_SHORT).show();
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