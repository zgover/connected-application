// Zachary Gover
// JAV1 - 1608
// NetworkConnectivity

package com.example.zachary.connectedapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkConnectivity {

	/**
	 * MARK: Static Methods
	 */

	public static boolean online(Context context) {
		ConnectivityManager man = (ConnectivityManager) context.getSystemService(
			context.CONNECTIVITY_SERVICE
		);
		NetworkInfo info = man.getActiveNetworkInfo();

		// Check if we're connected
		if (info != null && info.isConnectedOrConnecting()) {
			return true;
		}

		return false;
	}

}
