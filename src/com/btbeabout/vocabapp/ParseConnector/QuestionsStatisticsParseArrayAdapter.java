package com.btbeabout.vocabapp.ParseConnector;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.btbeabout.vocabapp.R;
import com.btbeabout.vocabapp.Model.QuizObject;
import com.btbeabout.vocabapp.SharedPrefs.SharedPrefsManager;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class QuestionsStatisticsParseArrayAdapter extends ArrayAdapter<ParseObject> {
	
	private Context mContext;
	private List<ParseObject> mList;

	public QuestionsStatisticsParseArrayAdapter(Context context, List<ParseObject> objects) {
		super(context, R.layout.statistics_list_layout, objects);
		this.mContext = context;
		this.mList = objects;
		
		
		/*
		 * Shows user his/her own statistics: total questions answered, correct, incorrect, etc.
		 */

	}
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
			convertView = mLayoutInflater.inflate(R.layout.statistics_list_layout, null);
		}
		
		ParseObject metaData = mList.get(position);

		TextView totalQuestions = (TextView) convertView.findViewById(R.id.tvStatsTotalQuestions);
		TextView correctAnswers = (TextView) convertView.findViewById(R.id.tvStatsTotalQuestionsCorrect);
		TextView incorrectAnswers = (TextView) convertView.findViewById(R.id.tvStatsTotalQuestionsIncorrect);
		TextView skippedQuestions = (TextView) convertView.findViewById(R.id.tvStatsTotalQuestionsSkipped);

		 totalQuestions.setText(String.valueOf(metaData.getInt("totalQuestionsAmount")));
		 correctAnswers.setText(String.valueOf(metaData.getInt("totalCorrectAnswersAmount")));
		 incorrectAnswers.setText(String.valueOf(metaData.getInt("totalIncorrectAnswersAmount")));
		 skippedQuestions.setText(String.valueOf(metaData.getInt("totalSkippedQuestionsAmount")));

		return convertView;
	}

}
