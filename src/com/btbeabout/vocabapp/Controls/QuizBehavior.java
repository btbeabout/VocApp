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

	Context activityContext;
	
	SharedPrefsManager mPrefsManager;
	ParseManager parse;

	String[] wordArray;
	String[] definitionArray;

	Set<Integer> wordSet = new HashSet<Integer>();
	Set<Integer> definitionSet;
	List<String> definitionChoices;

	String radioButtonZero;
	String radioButtonOne;
	String radioButtonTwo;
	String radioButtonThree;
	
	boolean questionAnswered;
	
	int currentPlaceInWordList = 0;

	int currentWordInt = 0;
	int correctRadioButton;

	public QuizBehavior(Context context, int quizSize) {
		activityContext = context;
		newQuiz = new QuizObject(quizSize, activityContext.getResources()
				.getStringArray(R.array.word_array), activityContext
				.getResources().getStringArray(R.array.definition_array));
		
		mPrefsManager = new SharedPrefsManager(activityContext);
		parse = new ParseManager(retrieveUserName(), context);
		
		
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
		setRandomCorrectDefinition(currentWordInt, correctRadioButton);

	}

	public void chooseCurrentWord() {

		Random randomCurrentWord = new Random();
		boolean needNewWord = true;

		// Chooses unique, current word
		while (needNewWord) {
			int curWord = randomCurrentWord.nextInt(newQuiz.getWords().length);

			if (!wordSet.contains(curWord)) {
				wordSet.add(curWord);
				currentWordInt = curWord;
				needNewWord = false;
			}
		}
	}

	public void chooseCorrectRadio() {
		Random randomCurrentDefinitionRadio = new Random();
		correctRadioButton = randomCurrentDefinitionRadio.nextInt(4);
	}

	public void setRandomCorrectDefinition(int currentWordInteger,
			int correctRadio) {
		switch (correctRadio) {

		case 0:
			radioButtonZero = newQuiz.getDefinitions()[currentWordInteger];
			break;
		case 1:
			radioButtonOne = newQuiz.getDefinitions()[currentWordInteger];
			break;
		case 2:
			radioButtonTwo = newQuiz.getDefinitions()[currentWordInteger];
			break;
		case 3:
			radioButtonThree = newQuiz.getDefinitions()[currentWordInteger];
			break;
		default:
			break;
		}

	}

	public void setRandomDefinitions() {
		Random randDefinition = new Random();
		definitionSet = new HashSet<Integer>();
		definitionChoices = new ArrayList<String>();

		// Load definition set with unique integers with which to access
		// definition array
		while (definitionSet.size() < 5) {
			int defInt = randDefinition
					.nextInt(newQuiz.getDefinitions().length);

			if (defInt != currentWordInt) {
				definitionSet.add(defInt);
			}
		}

		for (int i : definitionSet) {
			definitionChoices.add(newQuiz.getDefinitions()[i]);
		}

		for (int i = 0; i < definitionChoices.size(); i++) {
			switch (i) {

			case 0:
				radioButtonZero = definitionChoices.get(i);
				break;
			case 1:
				radioButtonOne = definitionChoices.get(i);
				break;
			case 2:
				radioButtonTwo = definitionChoices.get(i);
				break;
			case 3:
				radioButtonThree = definitionChoices.get(i);
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
		currentPlaceInWordList++;
	}
	
	public int getCurrentPlaceInWordList() {
		return currentPlaceInWordList;
	}

	public String getCurrentWord() {
		return newQuiz.getWords()[currentWordInt];
	}

	public int getCorrectRadioButton() {
		return correctRadioButton;
	}

	public String getRadioZero() {
		return radioButtonZero;
	}

	public String getRadioOne() {
		return radioButtonOne;
	}

	public String getRadioTwo() {
		return radioButtonTwo;
	}

	public String getRadioThree() {
		return radioButtonThree;
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
		questionAnswered = response;
	}
	
	public boolean getQuestionAnswered() {
		return questionAnswered;
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
		parse.saveQuiz(newQuiz);
	}
	
	public void saveQuestionsMetaData() {
		parse.saveQuestionsData(newQuiz.getQuizSize(), newQuiz.getCorrectResponses(), newQuiz.getIncorrectResponses(), newQuiz.getSkippedWords());
	}

	public String retrieveUserName() {
		String userName = mPrefsManager.getUsername();
		return userName;
	}

}
