package com.btbeabout.vocabapp.ParseConnector;

import java.util.List;

import android.app.Application;
import android.content.Context;

import com.btbeabout.vocabapp.Connectivity.ConnectionChecker;
import com.btbeabout.vocabapp.Model.QuizObject;
import com.google.gson.Gson;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ParseManager extends Application {
	
	/*
	 * Quiz database! Uses online Parse data manager. This class is the mechanism for saving information to Parse.
	 * However, retrieving and viewing data is done via arraylist adapters.
	 */

	ConnectionChecker conCheck;
	String playerName;
	List<Integer> mQuestionsMetaDataList;

	public ParseManager(String playerName, Context context) {

		Parse.initialize(context, "tp3iUq1xaMnlo1bY38vlmvX8YpTpyyH4JyuL0BIQ",
				"6lzvfC38Tqm7PilACMICn8MykoES3RVHPTqh0aGQ");

		ParseUser.enableAutomaticUser();
		conCheck = new ConnectionChecker(context);
		this.playerName = playerName;
	}

	public void saveQuiz(QuizObject quiz) {
		
		// Converts quiz to JSON object, saves it to Parse.

		Gson gson = new Gson();
		String convertedQuizObject = gson.toJson(quiz);

		ParseObject quizWithUserObject = new ParseObject("savedQuiz");
		quizWithUserObject.put("playerName", playerName);
		quizWithUserObject.put("quizObject", convertedQuizObject);
		saveObject(quizWithUserObject);
	}
	
	// Saves quiz "meta" data. Checks whether such an object exists in Parse for the user. If not, creates one, otherwise it updates.

	public void saveQuestionsData(final int totalQuestions,
			final int totalCorrectAnswers, final int totalIncorrectAnswers,
			final int TotalSkippedQuestions) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("QuestionsMetaData");
		query.whereEqualTo("playerName", playerName);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (objects.size() > 0) {
					String objectID = objects.get(0).getObjectId();
					updateExistingQuestionsData(objectID, totalQuestions, totalCorrectAnswers, totalIncorrectAnswers, TotalSkippedQuestions,
							"QuestionsMetaData");
				} else {
					createNewQuestionsDataObject(totalQuestions, totalCorrectAnswers, totalIncorrectAnswers, TotalSkippedQuestions, "QuestionsMetaData");
				}
			}
		});
	}
	
	public void updateExistingQuestionsData(final String objectID, final int totalQuestions,
			final int totalCorrectAnswers, final int totalIncorrectAnswers,
			final int totalSkippedQuestions, final String parseObjectName) {
		ParseQuery<ParseObject> newQuery = ParseQuery.getQuery(parseObjectName);
		newQuery.getInBackground(objectID, new GetCallback<ParseObject>() {

			@Override
			public void done(ParseObject object, ParseException e) {

				if (e == null) {
					System.out.println("Object retrieved!");
					System.out.println("ObjectID: " + object.getObjectId());
					object.increment("totalQuestionsAmount", totalQuestions);
					object.increment("totalCorrectAnswersAmount", totalCorrectAnswers);
					object.increment("totalIncorrectAnswersAmount", totalIncorrectAnswers);
					object.increment("totalSkippedQuestionsAmount", totalSkippedQuestions);
					object.increment("quizzesCompleted", 1);
					System.out.println("Update amount: " + totalQuestions);
					saveObject(object);
				}
			}
		});
	}
	
	public void createNewQuestionsDataObject(int totalQuestions,
			int totalCorrectAnswers, int totalIncorrectAnswers,
			int totalSkippedQuestions, String parseObjectName) {
		ParseObject newQuestionsDataObject = new ParseObject(parseObjectName);
		newQuestionsDataObject.put("playerName", playerName);
		newQuestionsDataObject.put("totalQuestionsAmount", totalQuestions);
		newQuestionsDataObject.put("totalCorrectAnswersAmount", totalCorrectAnswers);
		newQuestionsDataObject.put("totalIncorrectAnswersAmount", totalIncorrectAnswers);
		newQuestionsDataObject.put("totalSkippedQuestionsAmount", totalSkippedQuestions);		
		newQuestionsDataObject.put("quizzesCompleted", 1);
		saveObject(newQuestionsDataObject);
	}

	public void saveObject(ParseObject object) {
		if (conCheck.isConnected()) {
			object.saveInBackground();
		} else {
			object.saveEventually();
		}
	}
}
