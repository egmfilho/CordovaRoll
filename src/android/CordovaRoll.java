package org.apache.cordova.cordovaroll;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.util.Base64;

/**
 * Created by egmfilho on 12/04/16.
 */
public class CordovaRoll extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if ("saveToPhotoLibrary".equals(action)) {
            this.saveToPhotoLibrary(args.getString(0), args.getString(1), args.getString(2), callbackContext);
            return true;
        }
        return false;  // Returning false results in a "MethodNotFound" error.
    }

    private Bitmap decodeBitmap(String data) {
        byte[] b = Base64.decode(data, Base64.DEFAULT);

        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    private void saveToPhotoLibrary(String data, String title, String description, CallbackContext callbackContext) {
        Context context = this.cordova.getActivity().getApplicationContext();
        String filepath;

        title = title.isEmpty() ? "" : title;
        description = description.isEmpty() ? "" : description;

        try {
            filepath = MediaStore.Images.Media.insertImage(context.getContentResolver(), decodeBitmap(data), title, description);
        } catch (Exception e) {
            callbackContext.error("Error!");
        } finally {
            new SingleMediaScanner(context, filepath);
            callbackContext.success("Saved!");
        }
    }

}
