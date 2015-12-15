#import <Photos/Photos.h>
#import <Cordova/CDVPlugin.h>

@interface CordovaRoll : CDVPlugin {
    NSString* callbackId;
}

@property (nonatomic, copy) NSString* callbackId;

- (void) saveToPhotoLibrary:(CDVInvokedUrlCommand*) command;

@end
