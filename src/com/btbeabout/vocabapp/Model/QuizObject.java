package com.btbeabout.vocabapp.Model;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class QuizObject {

	int quizSize;
	int correctResponses = 0;
	int incorrectResponses = 0;
	int skippedWords = 0;
	String quizDate;
	Set<Integer> wordSet = new HashSet<Integer>();
	Set<Integer> definitionSet;
	String[] words;
	String[] definitions;

	public QuizObject(int quizSize, String[] inputWords, String[] inputDefinitions) {
		this.quizSize = quizSize;
		words = new String[quizSize];
		definitions = new String[quizSize];

		while (wordSet.size() < quizSize) {
			Random randomNum = new Random();
			// Chooses unique, current word
			int curWord = randomNum.nextInt(inputWords.length);
			if (!wordSet.contains(curWord)) {
				wordSet.add(curWord);
				System.out.println("ADDING");
			}

		}
		// one-to-one transfer from set to words array
		int wordPlace = 0;

		for (int i : wordSet) {
			words[wordPlace] = inputWords[i];
			definitions[wordPlace] = inputDefinitions[i];
			wordPlace++;
		}
	}

	public String[] getWords() {
		return words;
	}

	public String[] getDefinitions() {
		return definitions;
	}

	public int getQuizSize() {
		return quizSize;
	}

	public int getCorrectResponses() {
		return correctResponses;
	}

	public void incrementCorrectResponse() {
		this.correctResponses = correctResponses + 1;
	}

	public int getIncorrectResponses() {
		return incorrectResponses;
	}

	public void incrementIncorrectResponse() {
		this.incorrectResponses = incorrectResponses + 1;
	}

	public int getSkippedWords() {
		return skippedWords;
	}

	public void incrementSkippedWords() {
		this.skippedWords = skippedWords + 1;
	}
	
	public void setQuizDate(String dateQuizFinished) {
		this.quizDate = dateQuizFinished;  
	}
	
	public String getQuizDate() {
		return quizDate;
	}

}
