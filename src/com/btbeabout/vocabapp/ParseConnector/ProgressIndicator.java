package com.btbeabout.vocabapp.ParseConnector;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.ProgressBar;

public class ProgressIndicator {

	private Context mContext;
	ProgressDialog downloadRingDialog;

	// This allows user to see that information is being downloaded from Parse
	// since, otherwise, would just result in a temporarily empty listview.

	public ProgressIndicator(Context context) {
		this.mContext = context;
	}

	public void showProgressRing() {
		downloadRingDialog = new ProgressDialog(mContext);
		downloadRingDialog.setMessage("Downloading quiz data...");
		downloadRingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		downloadRingDialog.setIndeterminate(true);
		downloadRingDialog.show();
	}

	public void cancelProgressRing() {
		downloadRingDialog.cancel();
	}

}
