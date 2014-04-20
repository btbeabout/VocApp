package com.btbeabout.vocabapp.Model;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class QuizObject {

	int mQuizSize;
	int mCorrectResponses = 0;
	int mIncorrectResponses = 0;
	int mSkippedWords = 0;
	String mQuizDate;
	Set<Integer> mWordSet = new HashSet<Integer>();
	Set<Integer> mDefinitionSet;
	String[] mWords;
	String[] mDefinitions;

	public QuizObject(int mQuizSize, String[] inputWords, String[] inputDefinitions) {
		this.mQuizSize = mQuizSize;
		mWords = new String[mQuizSize];
		mDefinitions = new String[mQuizSize];

		while (mWordSet.size() < mQuizSize) {
			Random randomNum = new Random();
			// Chooses unique, current word
			int curWord = randomNum.nextInt(inputWords.length);
			if (!mWordSet.contains(curWord)) {
				mWordSet.add(curWord);
				System.out.println("ADDING");
			}

		}
		// one-to-one transfer from set to words array
		int wordPlace = 0;

		for (int i : mWordSet) {
			mWords[wordPlace] = inputWords[i];
			mDefinitions[wordPlace] = inputDefinitions[i];
			wordPlace++;
		}
	}

	public String[] getWords() {
		return mWords;
	}

	public String[] getDefinitions() {
		return mDefinitions;
	}

	public int getQuizSize() {
		return mQuizSize;
	}

	public int getCorrectResponses() {
		return mCorrectResponses;
	}

	public void incrementCorrectResponse() {
		this.mCorrectResponses = mCorrectResponses + 1;
	}

	public int getIncorrectResponses() {
		return mIncorrectResponses;
	}

	public void incrementIncorrectResponse() {
		this.mIncorrectResponses = mIncorrectResponses + 1;
	}

	public int getSkippedWords() {
		return mSkippedWords;
	}

	public void incrementSkippedWords() {
		this.mSkippedWords = mSkippedWords + 1;
	}
	
	public void setQuizDate(String dateQuizFinished) {
		this.mQuizDate = dateQuizFinished;  
	}
	
	public String getQuizDate() {
		return mQuizDate;
	}

}
