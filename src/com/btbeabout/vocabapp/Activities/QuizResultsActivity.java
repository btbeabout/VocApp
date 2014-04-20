package com.btbeabout.vocabapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.btbeabout.vocabapp.R;

public class QuizResultsActivity extends Activity {

	TextView tvCorrectAnswers;
	TextView tvIncorrectAnswers;
	TextView tvSkippedAnswers;
	TextView tvQuizDate;
	
	ListView resultsWordList;
	
	ArrayAdapter<String> mWordListAdapter;

	int mCorrectAnswers;
	int mIncorrectAnswers;
	int mSkippedAnswers;
	
	String[] mWords;
	String[] mDefinitions;
	String[] mCombinedList;
	
	String mCurrentDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quiz_results_layout);

		setCurrentDate();
		initializeViews();
		receiveIntent();
		applyValuesToViews();
		setupArrayAdapter();
		
		/*
		 * Takes all information from the QuizScreenActivity, loads details for user's review.
		 */
		
	}

	public void receiveIntent() {
		Intent intent = getIntent();
		mCorrectAnswers = intent.getIntExtra("correctAnswers", 0);
		mIncorrectAnswers = intent.getIntExtra("incorrectAnswers", 0);
		mSkippedAnswers = intent.getIntExtra("skippedAnswers", 0);
		mWords = intent.getStringArrayExtra("wordList");
		mDefinitions = intent.getStringArrayExtra("definitionList");
		
	}
	
	public void setCurrentDate() {
		
		Time now = new Time(Time.getCurrentTimezone());
		now.setToNow();
		mCurrentDate = now.month + 1 + "/" + now.monthDay + "/" + now.year;
		
	}

	public void initializeViews() {

		tvCorrectAnswers = (TextView) findViewById(R.id.tvCorrectAnswers);
		tvIncorrectAnswers = (TextView) findViewById(R.id.tvIncorrectAnswers);
		tvSkippedAnswers = (TextView) findViewById(R.id.tvSkippedAnswers);
		tvQuizDate = (TextView) findViewById(R.id.tvQuizDate);
		resultsWordList = (ListView) findViewById(R.id.results_word_list);
	
	}

	public void applyValuesToViews() {
		tvCorrectAnswers.setText(Integer.toString(mCorrectAnswers));
		tvIncorrectAnswers.setText(Integer.toString(mIncorrectAnswers));
		tvSkippedAnswers.setText(Integer.toString(mSkippedAnswers));
		tvQuizDate.setText(mCurrentDate);
		
	}
	
	public void setupArrayAdapter() {
		mWordListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, combineWordsAndDefinitions(mWords, mDefinitions));
		resultsWordList.setAdapter(mWordListAdapter);
	}
	
	public String[] combineWordsAndDefinitions(String[] wordlist, String[] definitionlist) {
		mCombinedList = new String[wordlist.length];
		for (int i = 0; i < wordlist.length; i++) {
			mCombinedList[i] = wordlist[i] + " = " + definitionlist[i];
		}
		
		return mCombinedList;
	}

	public void goToMainMenu(View v) {
		
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
		finish();

	}

	public void startNewQuiz(View v) {
		Intent i = new Intent(this, QuizScreenActivity.class);
		i.putExtra("quizSize", 20);
		startActivity(i);
		finish();
	}

}
