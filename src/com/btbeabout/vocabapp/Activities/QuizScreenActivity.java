package com.btbeabout.vocabapp.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.btbeabout.vocabapp.R;
import com.btbeabout.vocabapp.Controls.QuizBehavior;

public class QuizScreenActivity extends Activity {

	static final String TAG = "QuizActivity";

	TextView tvWord;
	TextView tvCorrectTally;
	TextView tvWrongAnswers;
	TextView tvSkippedAnswers;
	RadioButton rbDefinition0;
	RadioButton rbDefinition1;
	RadioButton rbDefinition2;
	RadioButton rbDefinition3;
	RadioButton rbAnswerAnimation;
	RadioButton rbCorrectChoice;
	RadioGroup rgDefinitionChoice;
	Button bAnswer;
	Button bSkip;
	QuizBehavior mQuizBehavior;
	String[] mWordArray;
	String[] mDefinitionArray;
	int mQuizSize = 0;
	int mCurrentWordInt = 0;
	int mRandomDefinitionInt = 0;
	int mCorrectRadioButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quiz_layout);

		Intent intent = getIntent();
		mQuizSize = intent.getIntExtra("mQuizSize", 20);

		initializeClasses();
		initializeViews();
		setQuiz();
		
		/*
		 *  Integrates with mQuizBehavior class to a) create a new quiz, b) control flow of the quiz, c) allower user to interact with
		 *  quiz through views, and d) save the quiz to Parse.
		 *  
		 *  Plans for the future: avoid issue where tilting screen resets progress. May do this by locking into portrait mode, or, preferably,
		 *  saving information in a bundle to be re-loaded when activity is re-created.
		 */
		

	}

	public void initializeClasses() {
		mQuizBehavior = new QuizBehavior(this, mQuizSize);
	}

	public void initializeViews() {
		tvWord = (TextView) findViewById(R.id.tvWord);
		tvCorrectTally = (TextView) findViewById(R.id.tvCorrectTallyLabel);
		tvWrongAnswers = (TextView) findViewById(R.id.tvWrongAnswers);
		tvSkippedAnswers = (TextView) findViewById(R.id.tvSkippedAnswers);

		rbDefinition0 = (RadioButton) findViewById(R.id.rDefinition0);
		rbDefinition1 = (RadioButton) findViewById(R.id.rDefinition1);
		rbDefinition2 = (RadioButton) findViewById(R.id.rDefinition2);
		rbDefinition3 = (RadioButton) findViewById(R.id.rDefinition3);
		rgDefinitionChoice = (RadioGroup) findViewById(R.id.rgDefinitionChoice);
		bAnswer = (Button) findViewById(R.id.bAnswer);
		bSkip = (Button) findViewById(R.id.bSkip);
	}

	public void setQuiz() {
		rgDefinitionChoice.clearCheck();
		mQuizBehavior.setQuiz();
		setCurrentWord();
		setRadioButtons();
		setCorrectRadioButton();

	}

	public void setCurrentWord() {
		tvWord.setText(mQuizBehavior.getCurrentWord());
	}

	public void setRadioButtons() {
		rbDefinition0.setText(mQuizBehavior.getRadioZero());
		rbDefinition1.setText(mQuizBehavior.getRadioOne());
		rbDefinition2.setText(mQuizBehavior.getRadioTwo());
		rbDefinition3.setText(mQuizBehavior.getRadioThree());
	}

	public void setCorrectRadioButton() {
		switch (mQuizBehavior.getCorrectRadioButton()) {
		case 0:
			rbCorrectChoice = (RadioButton) findViewById(R.id.rDefinition0);
			break;
		case 1:
			rbCorrectChoice = (RadioButton) findViewById(R.id.rDefinition1);
			break;
		case 2:
			rbCorrectChoice = (RadioButton) findViewById(R.id.rDefinition2);
			break;
		case 3:
			rbCorrectChoice = (RadioButton) findViewById(R.id.rDefinition3);
			break;
		default:
			break;
		}
	}

	/*
	 *  If user has made a choice, correct or incorrect, will move to next question. If at end of quiz, saves quiz and loads quiz results activity.
	 *  If user hasn't made a choice, prompts user to do so (or skip the question) with a toast.
	 */
	
	public void nextQuestion(View v) {

		if (mQuizBehavior.getQuestionAnswered()) {
			if (mQuizBehavior.getCurrentPlaceInWordList() < mQuizBehavior
					.getQuizSize() - 1) {
				mQuizBehavior.incrementCurrentPlaceInWordList();
				mQuizBehavior.setQuestionAnswered(false);
				setQuiz();
			} else {
				Toast.makeText(this, "Quiz Over!", Toast.LENGTH_SHORT).show();
				mQuizBehavior.setQuizDate();
				quizOver();
				finish();

			}
		} else {
			Toast.makeText(this, "Please answer or skip the current question!",
					Toast.LENGTH_SHORT).show();
		}

	}

	// When the quiz is over, saves it to Parse and loads a quiz results activity with all of the accrued information transferred to it.
	
	public void quizOver() {
		saveQuizToParse();
		Intent i = new Intent(this, QuizResultsActivity.class);
		i.putExtra("correctAnswers", mQuizBehavior.getCorrectTally());
		i.putExtra("incorrectAnswers", mQuizBehavior.getIncorrectTally());
		i.putExtra("skippedAnswers", mQuizBehavior.getSkipTally());
		i.putExtra("wordList", mQuizBehavior.getWordList());
		i.putExtra("definitionList", mQuizBehavior.getDefinitionList());
		startActivity(i);
	}

	public void answerQuestion(View v) {
		
		// Checks whether question is correct or incorrect.
		
		try {
			if (checkResponse()) {
				answerAnimation(0xFF458B00);
				if (!mQuizBehavior.getQuestionAnswered()) {
					mQuizBehavior.setCorrectResponse();
					incrementCorrectTally();
					mQuizBehavior.setQuestionAnswered(true);
				}
			} else {
				answerAnimation(0xFFFF3333);
				if (!mQuizBehavior.getQuestionAnswered()) {
					mQuizBehavior.setIncorrectResponse();
					mQuizBehavior.setQuestionAnswered(true);
					incrementWrongTally();
				}
			}
		} catch (Exception e) {
			Toast.makeText(this, "Please select an answer!", Toast.LENGTH_SHORT)
					.show();

		}
	}
	
	// Animations! Wiggles and turns green if correct, red if incorrect.

	public void answerAnimation(int zeroForCorrect) {
		rbAnswerAnimation = (RadioButton) findViewById(rgDefinitionChoice
				.getCheckedRadioButtonId());
		rbAnswerAnimation.setBackgroundColor(zeroForCorrect);
		new Handler().postDelayed(new Runnable() {

			public void run() {
				rbAnswerAnimation.setBackgroundColor(Color.TRANSPARENT);
			}
		}, 500);

		Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		rbAnswerAnimation.startAnimation(shake);

	}
	
	// Increments quiz data.

	public void incrementCorrectTally() {
		tvCorrectTally.setText("Correct: " + mQuizBehavior.getCorrectTally()
				+ "/" + mQuizBehavior.getQuizSize());
	}

	public void incrementWrongTally() {
		tvWrongAnswers.setText("Incorrect: " + mQuizBehavior.getIncorrectTally()
				+ "/" + mQuizBehavior.getQuizSize());
	}

	public void incrementSkippedTally() {
		tvSkippedAnswers.setText("Skipped: " + mQuizBehavior.getSkipTally()
				+ "/" + mQuizBehavior.getQuizSize());
	}

	// Essentially same function as nextQuestion(), but doesn't requires that user has NOT answered the question prior to skipping.
	
	public void skipWord(View v) {
		if (!mQuizBehavior.getQuestionAnswered()) {
			if (mQuizBehavior.getCurrentPlaceInWordList() < mQuizBehavior
					.getQuizSize() - 1) {
				mQuizBehavior.setIncrementSkips();
				mQuizBehavior.incrementCurrentPlaceInWordList();
				incrementSkippedTally();
				mQuizBehavior.setQuestionAnswered(false);
				setQuiz();
			} else {
				Toast.makeText(this, "Quiz Over!", Toast.LENGTH_LONG).show();
				mQuizBehavior.setQuizDate();
				mQuizBehavior.setIncrementSkips();
				quizOver();
				finish();
			}
		}

	}
	
	public void saveQuizToParse() {
		mQuizBehavior.saveQuestionsMetaData();
		mQuizBehavior.saveQuizToParse();
	}

	public boolean checkResponse() {

		if (rgDefinitionChoice.getCheckedRadioButtonId() == rbCorrectChoice
				.getId()) {
			return true;
		} else {
			return false;
		}
	}
}
