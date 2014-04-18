package com.btbeabout.vocabapp.Connectivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

// Checks whether a connection is available prior to connecting with Parse.

public class ConnectionChecker {
	
	ConnectivityManager cm;
	
	public ConnectionChecker(Context context) {
		cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	}
	
	public boolean isConnected() {
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnected() && activeNetwork.isAvailable();
	}
}
