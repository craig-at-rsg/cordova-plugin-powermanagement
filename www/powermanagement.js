/*
 * Copyright 2013 Wolfgang Koller
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
var PowerManagement = function() {};

/**
 * Acquire or release a wake-lock (prevents the screen and CPU of the device from switching off).
 *
 * Additionally, for platforms that support it, this method specifies whether the screen can be dimmed
 * on user idle after a wake-lock is acquired (according to the normal dimming behaviour for the platform).
 *
 * @param {bool} acquire - If true, acquires a wake-lock; if false, releases a previously-acquired lock.
 * @param {bool} [allowDimming=false] - Valid only on platforms which support this feature. If true, lets the 
 *			screen/keyboard dim according to the platform's normal dimming behaviour on idle after acquiring a wake-lock. 
 *			Defaults to false. 
 *			This parameter has no effect when the lock is being released, or where platforms don't support it.
 * @param successCallback - Function called when the wake-lock was acquired successfully.
 * @param errorCallback - Function called when there was a problem with acquiring the wake-lock.
 */
PowerManagement.prototype.setWakeLock = function(acquire, allowDimming, successCallback, failureCallback) {
	if(typeof allowDimming === "undefined") allowDimming = false;

	cordova.exec(successCallback, failureCallback, 'PowerManagement', 'setWakeLock', [acquire, allowDimming]);
}

/**
 * Manually sets the brightness level of the screen on a scale of 0.0 to 1.0.
 *
 * It is the responsibility of the caller (or OS) to restore brightness levels at the appropriate times 
 * (e.g.when quitting the app). [Note: though iOS documentation states that system brightness will be 
 * automatically restored, it appears that due to buggy implementation this does not in fact happen.]
 *
 * @param {float} The level of brightness to set the screen to, where 0.0 is the dimmest possible 
			level supported on the device (both at hardware level and with software overlay), and 1.0
			is the brightest level supported on the device.
 * @param successCallback Function called when the brightness level has been successfully set.
 * @param errorCallback Function called when there was a problem setting the brightness level.
 * @returns {float} The brightness level setting before the specified value was applied.
 */
PowerManagement.prototype.setBrightness = function (brightness, successCallback, failureCallback) {
	cordova.exec(successCallback, failureCallback, 'PowerManagement', 'setBrightness', [brightness]);
}

module.exports = new PowerManagement();