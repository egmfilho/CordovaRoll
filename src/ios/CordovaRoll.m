#import "CordovaRoll.h"

@implementation CordovaRoll
@synthesize callbackId;

- (void) saveToCameraRoll:(CDVInvokedUrlCommand*) command {

    self.callbackId = command.callbackId;

    NSString* dataURL = [command.arguments objectAtIndex:0];

    NSData* imageData = [[NSData alloc] initWithBase64EncodedString: dataURL options:0];

    UIImage* image = [[UIImage alloc] initWithData:imageData];

    UIImageWriteToSavedPhotosAlbum(image, self, @selector(callback:didFinishSavingWithError:contextInfo:), nil);
}

- (void) callback:(UIImage *)image didFinishSavingWithError:(NSError*)error contextInfo:(void*)contextInfo {

    CDVPluginResult* result;

    if (error != NULL) {
        NSLog(@"ERROR: %@", error);
        result = [CDVPluginResult resultWithStatus: CDVCommandStatus_ERROR messageAsString:error.description];
    } else {
        NSLog(@"IMAGE SAVED!");
        result = [CDVPluginResult resultWithStatus: CDVCommandStatus_OK messageAsString:@"Image saved"];
    }

    [self.commandDelegate sendPluginResult:result callbackId:callbackId];
}

- (void) dealloc {

    [callbackId release];
    [super dealloc];

}

@end
