#import "CordovaRoll.h"

@implementation CordovaRoll
@synthesize callbackId;

- (void) saveToCameraRoll:(CDVInvokedUrlCommand*) command {

    NSLog(@"CordovaRoll: Preparing to save photo");
    
    self.callbackId = command.callbackId;

    NSData* imageData = [NSData dataFromBase64String: [command.arguments objectAtIndex:0]];

    UIImage* image = [[[UIImage alloc] initWithData:imageData] autorelease];
    UIImageWriteToSavedPhotosAlbum(image, self, @selector(callback:didFinishSavingWithError:contextInfo:), nil);
}

- (void) callback:(UIImage *)image didFinishSavingWithError:(NSError*)error contextInfo:(void*)contextInfo {

    if (error != NULL) {
        NSLog(@"ERROR: %@", error);
        CDVPluginResult* result = [CDVPluginResult resultWithStatus: CDVCommandStatus_ERROR messageAsString:error.description];
        [self.webview stringByEvaluatingJavascriptFromString:[result toErrorCallbackString: self.callbackId]]
    } else {
        NSLog(@"IMAGE SAVED!");
        CDVPluginResult* result = [CDVPluginResult resultWithStatus: CDVCommandStatus_OK messageAsString:@"Image saved"];
        [self.webview stringbyEvaluatingJavascriptFromString:[result toSuccessCallbackString: self.callbackId]];
    }

}

- (void) dealloc {

    [callbackId release];
    [super dealloc];

}
