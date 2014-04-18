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
	
	ListView results_word_list;
	
	ArrayAdapter<String> word_list_adapter;

	int correctAnswers;
	int incorrectAnswers;
	int skippedAnswers;
	
	String[] words;
	String[] definitions;
	String[] combinedList;
	
	String currentDate;

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
		correctAnswers = intent.getIntExtra("correctAnswers", 0);
		incorrectAnswers = intent.getIntExtra("incorrectAnswers", 0);
		skippedAnswers = intent.getIntExtra("skippedAnswers", 0);
		words = intent.getStringArrayExtra("wordList");
		definitions = intent.getStringArrayExtra("definitionList");
		
	}
	
	public void setCurrentDate() {
		
		Time now = new Time(Time.getCurrentTimezone());
		now.setToNow();
		currentDate = now.month + 1 + "/" + now.monthDay + "/" + now.year;
		
	}

	public void initializeViews() {

		tvCorrectAnswers = (TextView) findViewById(R.id.tvCorrectAnswers);
		tvIncorrectAnswers = (TextView) findViewById(R.id.tvIncorrectAnswers);
		tvSkippedAnswers = (TextView) findViewById(R.id.tvSkippedAnswers);
		tvQuizDate = (TextView) findViewById(R.id.tvQuizDate);
		results_word_list = (ListView) findViewById(R.id.results_word_list);
	
	}

	public void applyValuesToViews() {
		tvCorrectAnswers.setText(Integer.toString(correctAnswers));
		tvIncorrectAnswers.setText(Integer.toString(incorrectAnswers));
		tvSkippedAnswers.setText(Integer.toString(skippedAnswers));
		tvQuizDate.setText(currentDate);
		
	}
	
	public void setupArrayAdapter() {
		word_list_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, combineWordsAndDefinitions(words, definitions));
		results_word_list.setAdapter(word_list_adapter);
	}
	
	public String[] combineWordsAndDefinitions(String[] wordlist, String[] definitionlist) {
		combinedList = new String[wordlist.length];
		for (int i = 0; i < wordlist.length; i++) {
			combinedList[i] = wordlist[i] + " = " + definitionlist[i];
		}
		
		return combinedList;
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
