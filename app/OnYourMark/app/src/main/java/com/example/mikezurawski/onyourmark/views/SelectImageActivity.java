//
// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license.
//
// Microsoft Cognitive Services (formerly Project Oxford): https://www.microsoft.com/cognitive-services
//
// Microsoft Cognitive Services (formerly Project Oxford) GitHub:
// https://github.com/Microsoft/Cognitive-Vision-Android
//
// Copyright (c) Microsoft Corporation
// All rights reserved.
//
// MIT License:
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to
// the following conditions:
//
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED ""AS IS"", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//
package com.example.mikezurawski.onyourmark.views;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.mikezurawski.onyourmark.R;

import java.io.File;
import java.io.IOException;

// The activity for the user to select a image and to detect faces in the image.
public class SelectImageActivity extends AppCompatActivity {

    // Flag to indicate the request of the next task to be performed
    private static final int REQUEST_TAKE_PHOTO = 0;
    private static final int REQUEST_SELECT_IMAGE_IN_ALBUM = 1;

    // The URI of photo taken from gallery
    private Uri mUriPhotoTaken;

    // File of the photo taken with camera
    private File mFilePhotoTaken;
    private String selectedImagePath;
//
//    /**
//     * Retrieves the path of the image URI
//     */
//    public String getPath(Uri uri) {
//        // just some safety built in
//        if( uri == null ) {
//            return null;
//        }
//        String[] projection = { MediaStore.Images.Media.DATA };
//        Cursor cursor = managedQuery(uri, projection, null, null, null);
//        if( cursor != null ){
//            int column_index = cursor
//                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            String path = cursor.getString(column_index);
//            cursor.close();
//            return path;
//        }
//        return uri.getPath();
//    }

    // When the activity is created, set all the member variables to initial state.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image);
    }
    //
    //    // Save the activity state when it's going to stop.
    //    @Override
    //    protected void onSaveInstanceState(Bundle outState) {
    //        super.onSaveInstanceState(outState);
    //        outState.putParcelable("ImageUri", mUriPhotoTaken);
    //    }
    //
    //    // Recover the saved state when the activity is recreated.
    //    @Override
    //    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
    //        super.onRestoreInstanceState(savedInstanceState);
    //        mUriPhotoTaken = savedInstanceState.getParcelable("ImageUri");
    //    }

    // Deal with the result of selection of the photos and faces.
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case REQUEST_TAKE_PHOTO:
//                if (resultCode == RESULT_OK) {
//                    Intent intent = new Intent();
//                    intent.setData(Uri.fromFile(mFilePhotoTaken));
//                    setResult(RESULT_OK, intent);
//                    finish();
//                }
//                break;
//            case REQUEST_SELECT_IMAGE_IN_ALBUM:
//                if (resultCode == RESULT_OK) {
//                    if (requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM) {
//                        Uri selectedImageUri = data.getData();
//                        selectedImagePath = getPath(selectedImageUri);
//                        Intent intent = new Intent();
//                        intent.setData(selectedImageUri);
//                        setResult(RESULT_OK, intent);
//                        finish();
//                    }
//                }
//                break;
//            default:
//                break;
//        }
//    }
//
//    // When the button of "Take a Photo with Camera" is pressed.
//    public void takePhoto(View view) {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if(intent.resolveActivity(getPackageManager()) != null) {
//            // Save the photo taken to a temporary file.
//            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//            try {
//                mFilePhotoTaken = File.createTempFile(
//                        "IMG_",  /* prefix */
//                        ".jpg",         /* suffix */
//                        storageDir      /* directory */
//                );
//
//                // Create the File where the photo should go
//                // Continue only if the File was successfully created
//                if (mFilePhotoTaken != null) {
//                    mUriPhotoTaken = FileProvider.getUriForFile(this,
//                            "com.example.mikezurawski.onyourmark",
//                            mFilePhotoTaken);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriPhotoTaken);
//
//                    // Finally start camera activity
//                    startActivityForResult(intent, REQUEST_TAKE_PHOTO);
//                }
//            } catch (IOException e) {
//                setInfo(e.getMessage());
//            }
//        }
//    }

//    // When the button of "Select a Photo in Album" is pressed.
//    public void selectImageInAlbum(View view) {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent,
//                "Select Picture"), REQUEST_SELECT_IMAGE_IN_ALBUM);
//    }
//
//    // Set the information panel on screen.
//    private void setInfo(String info) {
//        TextView textView = (TextView) findViewById(R.id.info);
//        textView.setText(info);
//    }
}
