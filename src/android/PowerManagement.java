/*
 * Copyright 2013-2014 Wolfgang Koller
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Cordova (Android) plugin for accessing the power-management functions of the device
 * @author Wolfgang Koller <viras@users.sourceforge.net>
 */
package org.eazy.cordova.plugin;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;

/**
 * Plugin class which does the actual handling
 */
public class PowerManagement extends CordovaPlugin {
	// As we only allow one wake-lock, we keep a reference to it here
	private PowerManager.WakeLock wakeLock = null;
	private PowerManager powerManager = null;

	/**
	 * Fetch a reference to the power-service when the plugin is initialized
	 */
	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);

		this.powerManager = (PowerManager) cordova.getActivity().getSystemService(Context.POWER_SERVICE);
	}

	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {

		PluginResult result = null;
		Log.d("PowerManagementPlugin", "Plugin execute called: " + this.toString());
		Log.d("PowerManagementPlugin", "Action is: " + action);

		try {
			if (action.equals("setWakeLock")) {
				if (args.getBoolean(0)) { 		// first parameter determines if we're acquiring or releasing
					if(args.getBoolean(1)) {	// second parameter determines if we should get a dim-lock (instead of full-wake)
						Log.d("PowerManagementPlugin", "Acquiring dim lock");
						result = this.acquire(PowerManager.SCREEN_DIM_WAKE_LOCK);
					}
					else {
						Log.d("PowerManagementPlugin", "Acquiring full wake lock");
						result = this.acquire(PowerManager.FULL_WAKE_LOCK);
					}
				}
				else {
					Log.d("PowerManagementPlugin", "Releasing wake/dim lock");
					result = this.release();
				}
			}
			else if (action.equals("setBrightness")) {
				// to do...
				result = new PluginResult(PluginResult.Status.INVALID_ACTION, "Not yet implemented...");
			}
		}
		catch(JSONException e) {
			result = new PluginResult(Status.JSON_EXCEPTION, e.getMessage());
		}

		callbackContext.sendPluginResult(result);
		return true;
	}

	/**
	 * Acquire a wake-lock
	 * @param p_flags Type of wake-lock to acquire
	 * @return PluginResult containing the status of the acquire process
	 */
	private PluginResult acquire(int p_flags) {
		PluginResult result = null;

		if (this.wakeLock == null) {
			this.wakeLock = this.powerManager.newWakeLock(p_flags, "PowerManagementPlugin");

			try {
				this.wakeLock.acquire();
				result = new PluginResult(PluginResult.Status.OK);
			}
			catch(Exception e) {
				this.wakeLock = null;
				result = new PluginResult(PluginResult.Status.ERROR, "Couldn't acquire wake lock - check your permissions!");
			}
		}
		else {
			result = new PluginResult(PluginResult.Status.ILLEGAL_ACCESS_EXCEPTION, "Wake lock already acquired - release first");
		}

		return result;
	}

	/**
	 * Release an active wake-lock
	 * @return PluginResult containing the status of the release process
	 */
	private PluginResult release() {
		PluginResult result = null;

		if(this.wakeLock != null) {
			this.wakeLock.release();
			this.wakeLock = null;

			result = new PluginResult(PluginResult.Status.OK, "OK");
		}
		else {
			result = new PluginResult(PluginResult.Status.ILLEGAL_ACCESS_EXCEPTION, "No wake lock acquired - acquire first");
		}

		return result;
	}

	/**
	 * Make sure any wake lock is released if the app goes into pause.
	 */
	@Override
	public void onPause(boolean multitasking) {
		if(this.wakeLock != null) this.wakeLock.release();

		super.onPause(multitasking);
	}

	/**
	 * Make sure any wakelock is re-acquired when the app resumes.
	 */
	@Override
	public void onResume(boolean multitasking) {
		if(this.wakeLock != null) this.wakeLock.acquire();

		super.onResume(multitasking);
	}
}
