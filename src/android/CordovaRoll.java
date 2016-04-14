package org.apache.cordova.cordovaroll;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.OutputStream;

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

        title = title.isEmpty() ? "" : title;
        description = description.isEmpty() ? "" : description;

        try {
            insertImage(context.getContentResolver(), decodeBitmap(data), title, description);
        } catch (Exception e) {
            callbackContext.error("Error!");
        } finally {
            callbackContext.success("Saved!");
        }
    }

    public String insertImage(ContentResolver cr, Bitmap source, String title, String description) {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        // Add the date meta data to ensure the image is added at the front of the gallery
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());

        Uri url = null;
        String stringUrl = null;    /* value to be returned */

        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if (source != null) {
                OutputStream imageOut = cr.openOutputStream(url);
                try {
                    source.compress(Bitmap.CompressFormat.JPEG, 100, imageOut);
                } finally {
                    imageOut.close();
                }

                // long id = ContentUris.parseId(url);
                // // Wait until MINI_KIND thumbnail is generated.
                // Bitmap miniThumb = MediaStore.Images.Thumbnails.getThumbnail(cr, id, MediaStore.Images.Thumbnails.MINI_KIND, null);
                // // This is for backward compatibility.
                // storeThumbnail(cr, miniThumb, id, 50F, 50F, MediaStore.Images.Thumbnails.MICRO_KIND);
            } else {
                cr.delete(url, null, null);
                url = null;
            }
        } catch (Exception e) {
            if (url != null) {
                cr.delete(url, null, null);
                url = null;
            }
        }

        if (url != null) {
            stringUrl = url.toString();
        }

        return stringUrl;
    }

}
