module.exports = {

    saveToPhotoLibrary: function(successCallback, failureCallback, dataURL, filename, albumTitle) {

        if (typeof successCallback != 'function') {
            console.log('CordovaRoll Error: successCallback is not a function!');
        } else if (typeof failureCallback != 'function') {
            console.log('CordovaRoll Error: failureCallback is not a function!');
        } else if (albumTitle == null || typeof albumTitle != 'string') {
            console.log('Invalid album title!');
        } else {
            var data = String(dataURL).replace(/^data:image\/(png|jpg|jpeg);base64,/, '');
            return cordova.exec(successCallback, failureCallback, 'CordovaRoll', 'saveToPhotoLibrary', [data, filename, albumTitle]);
        }

    }

}
