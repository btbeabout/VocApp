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
	
	ArrayAdapter<String> word_list_adapter;

	int correctAnswers;
	int incorrectAnswers;
	int skippedAnswers;
	
	String[] wordsArray;
	String[] definitionsArray;
	String[] combinedList;
	
	String currentDate;
	String words;
	String definitions;

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
		wordsArray = words.split(",");
		definitionsArray = definitions.split(",");
		
		combinedList = new String[wordsArray.length];
		for (int i = 0; i < wordsArray.length; i++) {
			combinedList[i] = wordsArray[i] + " = " + definitionsArray[i];
		}
		
		return combinedList;
		
	}
	
	public void setupArrayAdapter() {
		word_list_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, combineWordsAndDefinitions(words, definitions));
		results_word_list.setAdapter(word_list_adapter);
	}
	
	public void updateViews() {
		tvQuizDate.setText(currentDate);
		tvCorrectAnswers.setText(Integer.toString(correctAnswers));
		tvIncorrectAnswers.setText(Integer.toString(incorrectAnswers));
		tvSkippedAnswers.setText(Integer.toString(skippedAnswers));
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
		currentDate = intent.getStringExtra("quizDate");
		correctAnswers = intent.getIntExtra("correctAnswers", 1);
		incorrectAnswers = intent.getIntExtra("incorrectAnswers", 2);
		skippedAnswers = intent.getIntExtra("skippedAnswers", 3);
		words = intent.getStringExtra("wordList");
		definitions = intent.getStringExtra("definitionList");
		
	}

}
