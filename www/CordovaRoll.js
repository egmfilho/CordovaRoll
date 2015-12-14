module.exports = {

    saveToCameraRoll: function(successCallback, failureCallback, dataURL) {

        if (typeof successCallback != 'function') {
            console.log('CordovaRoll Error: successCallback is not a function!');
        } else if (typeof failureCallback != 'function') {
            console.log('CordovaRoll Error: failureCallback is not a function!');
        } else {
            var data = String(dataURL).replace(/data:image\/png;base64,/,'');

            return cordova.exec(successCallback, failureCallback, 'CordovaRoll', 'saveToCameraRoll', [data]);
        }

    }

}
