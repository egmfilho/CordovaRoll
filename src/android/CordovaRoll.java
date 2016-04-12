package com.egmfilho.CordovaRoll;

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
        if ("saveToGallery".equals(action)) {
            this.saveToGallery(args.getLong(0), args.getLong(1), callbackContext);
            return true;
        }

        return false;  // Returning false results in a "MethodNotFound" error.
    }

    Bitmap decodeBitmap(String data) {

        byte[] b = Base64.decode(data, Base64.DEFAULT);

        return BitmapFactory.decodeByteArray(b, 0, b.length);

    }

    public void saveToGallery(Bitmap bmp, String title, CallbackContext callbackContext) {

        Context context = this.cordova.getActivity().getApplicationContext();

        // description = description.isEmpty() ? "" : description;
        String description = "";

        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), bmp, title, description);
        } catch (Exception e) {
            callbackContext.error("");
        } finally {
            callbackContext.success("");
        }

    }

}
