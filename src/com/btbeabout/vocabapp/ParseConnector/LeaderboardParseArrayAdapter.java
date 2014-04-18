package com.btbeabout.vocabapp.ParseConnector;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.btbeabout.vocabapp.R;
import com.parse.ParseObject;

public class LeaderboardParseArrayAdapter  extends ArrayAdapter<ParseObject> {
	
	private Context mContext;
	private List<ParseObject> mList;
	private String mQuestionDataCriteria;
	TextView tvLeaderboardCriteriaLabel;
	TextView tvLeaderboardCriteriaValue;
	TextView tvLeaderboardPlayerName;
	ImageView ivLeaderboardTrophy;
	
	/*
	 * Loads user information downloaded from Parse. Also gives "medals" to the 3 highest-ranked users.
	 * 
	 * Plans for future: instead of total questions correct/incorrect/skipped, convert to percentages. Possibly weighted percentages.
	 */

	public LeaderboardParseArrayAdapter(Context context, List<ParseObject> objects, String questionDataCriteria) {
		super(context, R.layout.leaderboard_list_layout, objects);
		this.mContext = context;
		this.mList = objects;
		this.mQuestionDataCriteria = questionDataCriteria;

	}
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
			convertView = mLayoutInflater.inflate(R.layout.leaderboard_list_layout, null);
		}
		
		ParseObject metaData = mList.get(position);

		tvLeaderboardCriteriaLabel = (TextView) convertView.findViewById(R.id.tvLeaderboardCriteriaLabel);
		tvLeaderboardCriteriaValue  = (TextView) convertView.findViewById(R.id.tvLeaderboardCriteriaValue);
		tvLeaderboardPlayerName = (TextView) convertView.findViewById(R.id.tvLeaderboardPlayerName);
		ivLeaderboardTrophy = (ImageView) convertView.findViewById(R.id.ivLeaderboardTrophy);
	
		tvLeaderboardPlayerName.setText(metaData.getString("playerName"));
		QuestionDataCriteria criteriaSelector = QuestionDataCriteria.valueOf(mQuestionDataCriteria);
		
		updateViews(criteriaSelector, metaData);
		trophySetter(position);
		return convertView;
	}
	
	private void updateViews(QuestionDataCriteria criteriaSelector, ParseObject metaData) {
		System.out.println("Switch triggered.");
		System.out.println("Criteria: " + mQuestionDataCriteria);
		switch (criteriaSelector) {
		case totalCorrectAnswersAmount:
			tvLeaderboardCriteriaLabel.setText("Correct Answers: ");
			tvLeaderboardCriteriaValue.setText(String.valueOf(metaData.getInt(mQuestionDataCriteria)));
			break;
		case totalIncorrectAnswersAmount:
			tvLeaderboardCriteriaLabel.setText("Incorrect Answers: ");
			tvLeaderboardCriteriaValue.setText(String.valueOf(metaData.getInt(mQuestionDataCriteria)));
			break;
		case totalQuestionsAmount:
			tvLeaderboardCriteriaLabel.setText("Total Questions: ");
			tvLeaderboardCriteriaValue.setText(String.valueOf(metaData.getInt(mQuestionDataCriteria)));
			break;
		case totalSkippedQuestionsAmount:
			tvLeaderboardCriteriaLabel.setText("Skipped Questions: ");
			tvLeaderboardCriteriaValue.setText(String.valueOf(metaData.getInt(mQuestionDataCriteria)));
			break;
		}
	}
	
	private void trophySetter(int position) {
		
		switch (position) {
		case 0:
			ivLeaderboardTrophy.setVisibility(View.VISIBLE);
			ivLeaderboardTrophy.setImageResource(R.drawable.goldmedal);
			break;
		case 1:
			ivLeaderboardTrophy.setVisibility(View.VISIBLE);
			ivLeaderboardTrophy.setImageResource(R.drawable.silvermedal);
			break;
		case 2:
			ivLeaderboardTrophy.setVisibility(View.VISIBLE);
			ivLeaderboardTrophy.setImageResource(R.drawable.bronzemedal);
			break;
		}	
	}
	
	private enum QuestionDataCriteria {
		totalCorrectAnswersAmount,
		totalIncorrectAnswersAmount,
		totalQuestionsAmount,
		totalSkippedQuestionsAmount
	}

}