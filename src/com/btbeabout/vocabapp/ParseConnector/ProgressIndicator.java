package com.btbeabout.vocabapp.ParseConnector;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.ProgressBar;

public class ProgressIndicator {

	private Context mContext;
	ProgressDialog mDownloadRingDialog;

	// This allows user to see that information is being downloaded from Parse
	// since, otherwise, would just result in a temporarily empty listview.

	public ProgressIndicator(Context context) {
		this.mContext = context;
	}

	public void showProgressRing() {
		mDownloadRingDialog = new ProgressDialog(mContext);
		mDownloadRingDialog.setMessage("Downloading quiz data...");
		mDownloadRingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mDownloadRingDialog.setIndeterminate(true);
		mDownloadRingDialog.show();
	}

	public void cancelProgressRing() {
		mDownloadRingDialog.cancel();
	}

}
