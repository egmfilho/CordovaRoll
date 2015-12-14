#import <Cordova/CDVPlugin.h>

@interface CordovaRoll : CDVPlugin {
    NSString* callbackId;
}

@property (nonatomic, copy) NSString* callbackId;

- (void) saveToCameraRoll:(CDVInvokedUrlCommand*) command;

@end
