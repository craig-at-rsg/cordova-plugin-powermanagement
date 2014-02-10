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

/**
 * Cordova (iOS) plugin for accessing the power-management functions of the device
 */
#import "PowerManagement.h"

/**
 * Actual implementation of the interface
 */
@implementation PowerManagement
- (void) wakeLock:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result = nil;
    NSString* jsString = nil;
    NSString* callbackId = command.callbackId;
    
    // Acquire a reference to the local UIApplication singleton
    UIApplication* app = [UIApplication sharedApplication];
    
    // first command argument is whether to acquire or release the wake lock;
    // second argument is whether to dim the screen on wake-lock - not directly supported by iOS
    BOOL acquireLock = [[command.arguments objectAtIndex:0] boolValue];
    
    if (acquireLock) {
        if(![app isIdleTimerDisabled]) {
            [app setIdleTimerDisabled:true];
            
            result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
            jsString = [result toSuccessCallbackString:callbackId];
        }
        else {
            result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ILLEGAL_ACCESS_EXCEPTION messageAsString:@"IdleTimer already disabled"];
            jsString = [result toErrorCallbackString:callbackId];
        }
    }
    else {
        if([app isIdleTimerDisabled]) {
            [app setIdleTimerDisabled:false];

            result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
            jsString = [result toSuccessCallbackString:callbackId];
        }
        else {
            result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ILLEGAL_ACCESS_EXCEPTION messageAsString:@"IdleTimer not disabled"];
            jsString = [result toErrorCallbackString:callbackId];
        }
    }
    
    [self writeJavascript:jsString];
}

- (void) dimScreen:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* result = nil;
    NSString* callbackId = command.callbackId;

    // enable "software dimming" so we can go as dark as possible
    [[UIScreen mainScreen] setWantsSoftwareDimming:YES];
    
    // save the current brightness level to report to caller
    CGFloat currentBrightness = [[UIScreen mainScreen] brightness];
    
    // dim to the specified brightness value
    CGFloat newBrightness = [[command.arguments objectAtIndex:0] doubleValue];
    [[UIScreen mainScreen] setBrightness: newBrightness];
    
    // return the brightness level pre-dimming
    result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDouble:currentBrightness];
    
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}
@end
