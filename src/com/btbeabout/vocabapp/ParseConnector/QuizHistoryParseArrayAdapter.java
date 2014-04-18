package com.btbeabout.vocabapp.ParseConnector;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.btbeabout.vocabapp.R;
import com.btbeabout.vocabapp.Model.QuizObject;

public class QuizHistoryParseArrayAdapter extends ArrayAdapter<QuizObject> {
	
	private Context mContext;
	private List<QuizObject> mList;
	
	public QuizHistoryParseArrayAdapter(Context context, List<QuizObject> objects) {
		super(context, R.layout.short_quiz_layout, objects);
		this.mContext = context;
		this.mList = objects;
		
		/*
		 * Takes list of quiz objects and presents that information in a listview format.
		 */
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
			convertView = mLayoutInflater.inflate(R.layout.short_quiz_layout, null);
		}
		
		QuizObject quiz = mList.get(position);
		
		TextView tv_quiz_date = (TextView) convertView.findViewById(R.id.tv_quiz_date);
		TextView tv_quiz_correct_answers = (TextView) convertView.findViewById(R.id.tv_quiz_correct_answers);
		TextView tv_quiz_incorrect_answers = (TextView) convertView.findViewById(R.id.tv_quiz_incorrect_answers);
		TextView tv_quiz_skipped_answers = (TextView) convertView.findViewById(R.id.tv_quiz_skipped_answers);
		
		tv_quiz_date.setText(quiz.getQuizDate());
		tv_quiz_correct_answers.setText(String.valueOf(quiz.getCorrectResponses()));
		tv_quiz_incorrect_answers.setText(String.valueOf(quiz.getIncorrectResponses()));
		tv_quiz_skipped_answers.setText(String.valueOf(quiz.getSkippedWords()));
		
		return convertView;
	}
}
