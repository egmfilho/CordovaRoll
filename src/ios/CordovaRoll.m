#import "CordovaRoll.h"

@implementation CordovaRoll
@synthesize callbackId;

- (void) saveToPhotoLibrary:(CDVInvokedUrlCommand*) command {

    NSLog(@"Cordova Roll - Save images to device's camera roll");

    self.callbackId = command.callbackId;
    NSString* dataURL = (NSString*)[command.arguments objectAtIndex:0];
    NSString* albumTitle = (NSString*)[command.arguments objectAtIndex:1];

    NSData* imageData = [[NSData alloc] initWithBase64EncodedString: dataURL options:NSDataBase64DecodingIgnoreUnknownCharacters];
    UIImage* image = [[UIImage alloc] initWithData:imageData];

    [self addNewAssetWithImage:image toAlbum: [self getAlbum:albumTitle]];
}

- (void) addNewAssetWithImage:(UIImage *)image toAlbum:(PHAssetCollection *)album {

    [[PHPhotoLibrary sharedPhotoLibrary] performChanges:^{

        // Request creating an asset from the image.
        PHAssetChangeRequest* createAssetRequest = [PHAssetChangeRequest creationRequestForAssetFromImage:image];

        // Request editing the album.
        PHAssetCollectionChangeRequest* albumChangeRequest = [PHAssetCollectionChangeRequest changeRequestForAssetCollection:album];

        // Get a placeholder for the new asset and add it to the album editing request.
        PHObjectPlaceholder* assetPlaceHolder = [createAssetRequest placeholderForCreatedAsset];
        [albumChangeRequest addAssets:@[ assetPlaceHolder ]];

    } completionHandler:^(BOOL success, NSError * _Nullable error) {
        NSLog(@"Finished adding asset. %@", (success ? @"Success" : error));

        if (success) {
            CDVPluginResult* result = [CDVPluginResult resultWithStatus: CDVCommandStatus_OK messageAsString:@"Image saved"];
            [self.commandDelegate sendPluginResult:result callbackId:callbackId];
        } else {
            CDVPluginResult* result = [CDVPluginResult resultWithStatus: CDVCommandStatus_ERROR messageAsString:error.description];
            [self.commandDelegate sendPluginResult:result callbackId:callbackId];
        }
    }];

}

- (PHAssetCollection*) getAlbum:(NSString *)title {

    __block PHAssetCollection* collection = nil;
    __block PHObjectPlaceholder *placeholder = nil;

    PHFetchOptions* fetchOptions = [[PHFetchOptions alloc] init];
    fetchOptions.predicate = [NSPredicate predicateWithFormat:@"title = %@", title];

    PHFetchResult* result =  [PHAssetCollection fetchAssetCollectionsWithType:PHAssetCollectionTypeAlbum
                                                                              subtype:PHAssetCollectionSubtypeAny
                                                                              options:fetchOptions];
    if ([result count] != 0)
        collection = [result objectAtIndex:0];

    if (!collection) {

        [[PHPhotoLibrary sharedPhotoLibrary] performChanges:^{

            PHAssetCollectionChangeRequest* createAlbum = [PHAssetCollectionChangeRequest creationRequestForAssetCollectionWithTitle:title];
            placeholder = [createAlbum placeholderForCreatedAssetCollection];
            [placeholder retain];

        } completionHandler:^(BOOL success, NSError * _Nullable error) {

            if (success) {
                PHFetchResult* result = [PHAssetCollection fetchAssetCollectionsWithLocalIdentifiers:@[placeholder.localIdentifier] options:nil];
                collection = [result objectAtIndex:0];
            }

        }];

    }

    return collection;
}

- (void) dealloc {

    [callbackId release];
    [super dealloc];

}

@end
