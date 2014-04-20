package com.btbeabout.vocabapp.Activities;

import com.btbeabout.vocabapp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class QuizDetailsActivity extends Activity {
	
	TextView tvQuizDate;
	TextView tvCorrectAnswers;
	TextView tvIncorrectAnswers;
	TextView tvSkippedAnswers;
	
	ListView results_word_list;
	
	ArrayAdapter<String> mWordListAdapter;

	int mCorrectAnswers;
	int mIncorrectAnswers;
	int mSkippedAnswers;
	
	String[] mWordsArray;
	String[] mDefinitionsArray;
	String[] mCombinedList;
	
	String mCurrentDate;
	String mWords;
	String mDefinitions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quiz_details_layout);
		
		receiveIntent();
		initializeViews();
		updateViews();
		setupArrayAdapter();

	}
	
	// Pairs each words with its correct definitions to form one String each for the listview.
	
	public String[] combineWordsAndDefinitions(String words, String definitions) {
		mWordsArray = words.split(",");
		mDefinitionsArray = definitions.split(",");
		
		mCombinedList = new String[mWordsArray.length];
		for (int i = 0; i < mWordsArray.length; i++) {
			mCombinedList[i] = mWordsArray[i] + " = " + mDefinitionsArray[i];
		}
		
		return mCombinedList;
		
	}
	
	public void setupArrayAdapter() {
		mWordListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, combineWordsAndDefinitions(mWords, mDefinitions));
		results_word_list.setAdapter(mWordListAdapter);
	}
	
	public void updateViews() {
		tvQuizDate.setText(mCurrentDate);
		tvCorrectAnswers.setText(Integer.toString(mCorrectAnswers));
		tvIncorrectAnswers.setText(Integer.toString(mIncorrectAnswers));
		tvSkippedAnswers.setText(Integer.toString(mSkippedAnswers));
	}
	
	public void initializeViews() {
		
		tvQuizDate = (TextView) findViewById(R.id.tv_quiz_date);
		tvCorrectAnswers = (TextView) findViewById(R.id.tv_quiz_correct_answers);
		tvIncorrectAnswers = (TextView) findViewById(R.id.tv_quiz_incorrect_answers);
		tvSkippedAnswers = (TextView) findViewById(R.id.tv_quiz_skipped_answers);
		
		results_word_list = (ListView) findViewById(R.id.results_word_list);
		
	}
	
	
	public void receiveIntent() {
		Intent intent = getIntent();
		mCurrentDate = intent.getStringExtra("quizDate");
		mCorrectAnswers = intent.getIntExtra("correctAnswers", 1);
		mIncorrectAnswers = intent.getIntExtra("incorrectAnswers", 2);
		mSkippedAnswers = intent.getIntExtra("skippedAnswers", 3);
		mWords = intent.getStringExtra("wordList");
		mDefinitions = intent.getStringExtra("definitionList");
		
	}

}
