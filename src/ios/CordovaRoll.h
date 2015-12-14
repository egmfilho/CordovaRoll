#import <Cordova/CDVPlugin.h>

@interface CordovaRoll : CDVPlugin {
    - (void) saveToCameraRoll:(CDVInvokedUrlCommand*) command;
}

@property (nonatomic, copy) NSString* callbackId;

@end
