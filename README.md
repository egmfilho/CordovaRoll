#Cordova Roll

This plugins allows you to save base64 images into device's Gallery/Library.

This plugin defines global cordovaRoll object.

Although in the global scope, it is not available until after the `deviceready` event.

```javascript
document.addEventListener("deviceready", onDeviceReady, false);
function onDeviceReady() {
    console.log(cordovaRoll);
}
```

##Installation
```
cordova plugin add https://github.com/egmfilho/CordovaRoll.git
```

##Supported Platforms
* Android
* iOS

##Usage
```javascript
cordovaRoll.saveToPhotoLibrary(success, error, base64Image, filename, albumName);
```
