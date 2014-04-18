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
	RadioButton rDefinition0;
	RadioButton rDefinition1;
	RadioButton rDefinition2;
	RadioButton rDefinition3;
	RadioButton rbAnswerAnimation;
	RadioButton correctChoice;
	RadioGroup rgDefinitionChoice;
	Button bAnswer;
	Button bSkip;
	QuizBehavior quizBehavior;
	String[] wordArray;
	String[] definitionArray;
	int quizSize = 0;
	int currentWordInt = 0;
	int randomDefinitionInt = 0;
	int correctRadioButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.quiz_layout);

		Intent intent = getIntent();
		quizSize = intent.getIntExtra("quizSize", 20);

		initializeClasses();
		initializeViews();
		setQuiz();
		
		/*
		 *  Integrates with quizBehavior class to a) create a new quiz, b) control flow of the quiz, c) allower user to interact with
		 *  quiz through views, and d) save the quiz to Parse.
		 *  
		 *  Plans for the future: avoid issue where tilting screen resets progress. May do this by locking into portrait mode, or, preferably,
		 *  saving information in a bundle to be re-loaded when activity is re-created.
		 */
		

	}

	public void initializeClasses() {
		quizBehavior = new QuizBehavior(this, quizSize);
	}

	public void initializeViews() {
		tvWord = (TextView) findViewById(R.id.tvWord);
		tvCorrectTally = (TextView) findViewById(R.id.tvCorrectTallyLabel);
		tvWrongAnswers = (TextView) findViewById(R.id.tvWrongAnswers);
		tvSkippedAnswers = (TextView) findViewById(R.id.tvSkippedAnswers);

		rDefinition0 = (RadioButton) findViewById(R.id.rDefinition0);
		rDefinition1 = (RadioButton) findViewById(R.id.rDefinition1);
		rDefinition2 = (RadioButton) findViewById(R.id.rDefinition2);
		rDefinition3 = (RadioButton) findViewById(R.id.rDefinition3);
		rgDefinitionChoice = (RadioGroup) findViewById(R.id.rgDefinitionChoice);
		bAnswer = (Button) findViewById(R.id.bAnswer);
		bSkip = (Button) findViewById(R.id.bSkip);
	}

	public void setQuiz() {
		rgDefinitionChoice.clearCheck();
		quizBehavior.setQuiz();
		setCurrentWord();
		setRadioButtons();
		setCorrectRadioButton();

	}

	public void setCurrentWord() {
		tvWord.setText(quizBehavior.getCurrentWord());
	}

	public void setRadioButtons() {
		rDefinition0.setText(quizBehavior.getRadioZero());
		rDefinition1.setText(quizBehavior.getRadioOne());
		rDefinition2.setText(quizBehavior.getRadioTwo());
		rDefinition3.setText(quizBehavior.getRadioThree());
	}

	public void setCorrectRadioButton() {
		switch (quizBehavior.getCorrectRadioButton()) {
		case 0:
			correctChoice = (RadioButton) findViewById(R.id.rDefinition0);
			break;
		case 1:
			correctChoice = (RadioButton) findViewById(R.id.rDefinition1);
			break;
		case 2:
			correctChoice = (RadioButton) findViewById(R.id.rDefinition2);
			break;
		case 3:
			correctChoice = (RadioButton) findViewById(R.id.rDefinition3);
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

		if (quizBehavior.getQuestionAnswered()) {
			if (quizBehavior.getCurrentPlaceInWordList() < quizBehavior
					.getQuizSize() - 1) {
				quizBehavior.incrementCurrentPlaceInWordList();
				quizBehavior.setQuestionAnswered(false);
				setQuiz();
			} else {
				Toast.makeText(this, "Quiz Over!", Toast.LENGTH_SHORT).show();
				quizBehavior.setQuizDate();
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
		i.putExtra("correctAnswers", quizBehavior.getCorrectTally());
		i.putExtra("incorrectAnswers", quizBehavior.getIncorrectTally());
		i.putExtra("skippedAnswers", quizBehavior.getSkipTally());
		i.putExtra("wordList", quizBehavior.getWordList());
		i.putExtra("definitionList", quizBehavior.getDefinitionList());
		startActivity(i);
	}

	public void answerQuestion(View v) {
		
		// Checks whether question is correct or incorrect.
		
		try {
			if (checkResponse()) {
				answerAnimation(0xFF458B00);
				if (!quizBehavior.getQuestionAnswered()) {
					quizBehavior.setCorrectResponse();
					incrementCorrectTally();
					quizBehavior.setQuestionAnswered(true);
				}
			} else {
				answerAnimation(0xFFFF3333);
				if (!quizBehavior.getQuestionAnswered()) {
					quizBehavior.setIncorrectResponse();
					quizBehavior.setQuestionAnswered(true);
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
		tvCorrectTally.setText("Correct: " + quizBehavior.getCorrectTally()
				+ "/" + quizBehavior.getQuizSize());
	}

	public void incrementWrongTally() {
		tvWrongAnswers.setText("Incorrect: " + quizBehavior.getIncorrectTally()
				+ "/" + quizBehavior.getQuizSize());
	}

	public void incrementSkippedTally() {
		tvSkippedAnswers.setText("Skipped: " + quizBehavior.getSkipTally()
				+ "/" + quizBehavior.getQuizSize());
	}

	// Essentially same function as nextQuestion(), but doesn't requires that user has NOT answered the question prior to skipping.
	
	public void skipWord(View v) {
		if (!quizBehavior.getQuestionAnswered()) {
			if (quizBehavior.getCurrentPlaceInWordList() < quizBehavior
					.getQuizSize() - 1) {
				quizBehavior.setIncrementSkips();
				quizBehavior.incrementCurrentPlaceInWordList();
				incrementSkippedTally();
				quizBehavior.setQuestionAnswered(false);
				setQuiz();
			} else {
				Toast.makeText(this, "Quiz Over!", Toast.LENGTH_LONG).show();
				quizBehavior.setQuizDate();
				quizBehavior.setIncrementSkips();
				quizOver();
				finish();
			}
		}

	}
	
	public void saveQuizToParse() {
		quizBehavior.saveQuestionsMetaData();
		quizBehavior.saveQuizToParse();
	}

	public boolean checkResponse() {

		if (rgDefinitionChoice.getCheckedRadioButtonId() == correctChoice
				.getId()) {
			return true;
		} else {
			return false;
		}
	}
}
