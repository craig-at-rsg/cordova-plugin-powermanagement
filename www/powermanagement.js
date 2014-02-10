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
 * @param acquire If true, acquires a wake-lock; else releases a previously-acquired lock. Defaults to true.
 * @param allowDimming If true, let's the screen/keyboard dim on devices which support this feature. Defaults to false. Ignored when releasing a lock.
 * @param successCallback Function called when the wake-lock was acquired successfully.
 * @param errorCallback Function called when there was a problem with acquiring the wake-lock.
 */
PowerManagement.prototype.wakeLock = function(acquire, allowDimming, successCallback, failureCallback) {
	if( typeof acquire === "undefined" ) acquire = true;
	if( typeof allowDimming === "undefined" ) allowDimming = false;

    cordova.exec(successCallback, failureCallback, 'PowerManagement', 'wakeLock', [acquire, allowDimming]);
}

PowerManagement.prototype.dimScreen = function(brightness, successCallback, failureCallback) {
    cordova.exec(successCallback, failureCallback, 'PowerManagement', 'dimScreen', [brightness]);
}

module.exports = new PowerManagement();