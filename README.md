PowerManagement
===============
Plugin for Cordova (3.0+)

The PowerManagement plugin offers access to the power management functionality of the device, particularly to maintain wakefulness and screen brightness.
It would be useful, for example, for applications which run for a long time without any user interaction.

Tested on Android and iOS.

For details on power functionality see:

* Android: [PowerManager](http://developer.android.com/reference/android/os/PowerManager.html)
* iOS: [idleTimerDisabled](http://developer.apple.com/library/ios/documentation/UIKit/Reference/UIApplication_Class/Reference/Reference.html#//apple_ref/occ/instp/UIApplication/idleTimerDisabled)

Installation
---------
Install the plugin using the cordova command line utility:

`$ cordova plugin add https://github.com/mystinen/cordova-plugin-powermanagement.git`

Using the plugin
---------
```
// prevent device from sleeping
window.powerManagement.wakeLock();

// allow device to sleep
window.powerManagement.wakeLock(false);

// prevent device from sleeping, but allow dimming of screen/keyboard (if supported by device)
// and call the 'success' function once lock is set
window.powerManagement.wakeLock(true, true, success);

// set the brightness level of the screen
window.powerManagement.dimScreen(0.4);
```

License
---------
Copyright 2013 Wolfgang Koller

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
