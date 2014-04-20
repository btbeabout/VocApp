package com.btbeabout.vocabapp.Controls;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import android.content.Context;
import android.text.format.Time;

import com.btbeabout.vocabapp.R;
import com.btbeabout.vocabapp.Model.QuizObject;
import com.btbeabout.vocabapp.ParseConnector.ParseManager;
import com.btbeabout.vocabapp.SharedPrefs.SharedPrefsManager;

public class QuizBehavior {

	QuizObject newQuiz;

	Context mActivityContext;
	
	SharedPrefsManager mPrefsManager;
	ParseManager mParse;

	Set<Integer> mWordSet = new HashSet<Integer>();
	Set<Integer> mDefinitionSet;
	List<String> mDefinitionChoices;

	String mRadioButtonZero;
	String mRadioButtonOne;
	String mRadioButtonTwo;
	String mRadioButtonThree;
	
	boolean mQuestionAnswered;
	
	int mCurrentPlaceInWordList = 0;

	int mCurrentWordInt = 0;
	int mCorrectRadioButton;

	public QuizBehavior(Context context, int quizSize) {
		mActivityContext = context;
		newQuiz = new QuizObject(quizSize, mActivityContext.getResources()
				.getStringArray(R.array.word_array), mActivityContext
				.getResources().getStringArray(R.array.definition_array));
		
		mPrefsManager = new SharedPrefsManager(mActivityContext);
		mParse = new ParseManager(retrieveUserName(), context);
		
		
		/*
		 * This class integrates with the QuizScreenActivity to create a quiz for the user.
		 * It creates a new quiz object.
		 * After that quiz is created, it adds those words and definitions to a new array in a random order.
		 * It uses a set to ensure that there are no duplicates.
		 * When each new question is created, it randomizes the order of the definitions, but retains information on which choice is correct.
		 * It keeps track of total questions and correct/incorrect/skipped questions.
		 * Takes note of the date when the quiz was completed.
		 * Saves quiz/quiz information to Parse.
		 */
	}

	public void setQuiz() {

		chooseCurrentWord();
		chooseCorrectRadio();
		setRandomDefinitions();
		setRandomCorrectDefinition(mCurrentWordInt, mCorrectRadioButton);

	}

	public void chooseCurrentWord() {

		Random randomCurrentWord = new Random();
		boolean needNewWord = true;

		// Chooses unique, current word
		while (needNewWord) {
			int curWord = randomCurrentWord.nextInt(newQuiz.getWords().length);

			if (!mWordSet.contains(curWord)) {
				mWordSet.add(curWord);
				mCurrentWordInt = curWord;
				needNewWord = false;
			}
		}
	}

	public void chooseCorrectRadio() {
		Random randomCurrentDefinitionRadio = new Random();
		mCorrectRadioButton = randomCurrentDefinitionRadio.nextInt(4);
	}

	public void setRandomCorrectDefinition(int currentWordInteger,
			int correctRadio) {
		switch (correctRadio) {

		case 0:
			mRadioButtonZero = newQuiz.getDefinitions()[currentWordInteger];
			break;
		case 1:
			mRadioButtonOne = newQuiz.getDefinitions()[currentWordInteger];
			break;
		case 2:
			mRadioButtonTwo = newQuiz.getDefinitions()[currentWordInteger];
			break;
		case 3:
			mRadioButtonThree = newQuiz.getDefinitions()[currentWordInteger];
			break;
		default:
			break;
		}

	}

	public void setRandomDefinitions() {
		Random randDefinition = new Random();
		mDefinitionSet = new HashSet<Integer>();
		mDefinitionChoices = new ArrayList<String>();

		// Load definition set with unique integers with which to access
		// definition array
		while (mDefinitionSet.size() < 5) {
			int defInt = randDefinition
					.nextInt(newQuiz.getDefinitions().length);

			if (defInt != mCurrentWordInt) {
				mDefinitionSet.add(defInt);
			}
		}

		for (int i : mDefinitionSet) {
			mDefinitionChoices.add(newQuiz.getDefinitions()[i]);
		}

		for (int i = 0; i < mDefinitionChoices.size(); i++) {
			switch (i) {

			case 0:
				mRadioButtonZero = mDefinitionChoices.get(i);
				break;
			case 1:
				mRadioButtonOne = mDefinitionChoices.get(i);
				break;
			case 2:
				mRadioButtonTwo = mDefinitionChoices.get(i);
				break;
			case 3:
				mRadioButtonThree = mDefinitionChoices.get(i);
				break;
			default:
				break;
			}
		}
	}

	public boolean checkResponse(int checkedButton, int correctButton) {
		if (checkedButton == correctButton) {
			return true;
		} else {
			return false;
		}
	}
	
	public void incrementCurrentPlaceInWordList() {
		mCurrentPlaceInWordList++;
	}
	
	public int getCurrentPlaceInWordList() {
		return mCurrentPlaceInWordList;
	}

	public String getCurrentWord() {
		return newQuiz.getWords()[mCurrentWordInt];
	}

	public int getCorrectRadioButton() {
		return mCorrectRadioButton;
	}

	public String getRadioZero() {
		return mRadioButtonZero;
	}

	public String getRadioOne() {
		return mRadioButtonOne;
	}

	public String getRadioTwo() {
		return mRadioButtonTwo;
	}

	public String getRadioThree() {
		return mRadioButtonThree;
	}
	
	public int getQuizSize() {
		return newQuiz.getQuizSize();
	}

	public void setCorrectResponse() {
		newQuiz.incrementCorrectResponse();
	}

	public int getCorrectTally() {
		return newQuiz.getCorrectResponses();
	}

	public void setIncorrectResponse() {
		newQuiz.incrementIncorrectResponse();
	}

	public int getIncorrectTally() {
		return newQuiz.getIncorrectResponses();
	}

	public void setIncrementSkips() {
		newQuiz.incrementSkippedWords();
	}

	public int getSkipTally() {
		return newQuiz.getSkippedWords();
	}
	
	public void setQuestionAnswered(boolean response) {
		mQuestionAnswered = response;
	}
	
	public boolean getQuestionAnswered() {
		return mQuestionAnswered;
	}
	
	public String[] getWordList() {
		return newQuiz.getWords();
	}
	
	public String[] getDefinitionList() {
		return newQuiz.getDefinitions();
	}
	
	public String getQuizDate() {
		return newQuiz.getQuizDate();
	}
	
	public void setQuizDate() {
		Time now = new Time(Time.getCurrentTimezone());
		now.setToNow();
		newQuiz.setQuizDate(now.month + 1 + "/" + now.monthDay + "/" + now.year);
	}
	
	public void saveQuizToParse() {
		mParse.saveQuiz(newQuiz);
	}
	
	public void saveQuestionsMetaData() {
		mParse.saveQuestionsData(newQuiz.getQuizSize(), newQuiz.getCorrectResponses(), newQuiz.getIncorrectResponses(), newQuiz.getSkippedWords());
	}

	public String retrieveUserName() {
		String userName = mPrefsManager.getUsername();
		return userName;
	}

}
