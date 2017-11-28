package com.example.mikezurawski.onyourmark.views;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mikezurawski.onyourmark.R;
import com.google.gson.Gson;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.HandwritingRecognitionOperation;
import com.microsoft.projectoxford.vision.contract.HandwritingRecognitionOperationResult;
import com.microsoft.projectoxford.vision.contract.HandwritingTextLine;
import com.microsoft.projectoxford.vision.contract.HandwritingTextWord;
import com.microsoft.projectoxford.vision.rest.VisionServiceException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isNumeric;

public class AddItemActivity extends AppCompatActivity {

    private Button buttonSelectImage;
    private Uri imagUrl;
    private Bitmap bitmap;
    private EditText editText;
    private VisionServiceClient client;
    private int retryCountThreshold = 30;
    private final static String MY_TAG = "myapp";

    // Flag to indicate the request of the next task to be performed
    private static final int REQUEST_TAKE_PHOTO = 0;
    private static final int REQUEST_SELECT_IMAGE_IN_ALBUM = 1;

    // The URI of photo taken from gallery
    private Uri mUriPhotoTaken;

    // File of the photo taken with camera
    private File mFilePhotoTaken;
    private String selectedImagePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        if (client == null) {
            client = new VisionServiceRestClient(getString(R.string.subscription_key),
                    "https://westcentralus.api.cognitive.microsoft.com/vision/v1.0");
        }

        buttonSelectImage = (Button) findViewById(R.id.buttonSelectImage);
        editText = (EditText) findViewById(R.id.editTextResult);

        // Two buttons
        editText.setText("");

        // Launch take photo
        Button takePhotoButton = (Button) findViewById(R.id.buttonTakePhoto);
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });

        // Get photo from the gallery
        Button selectFromAlbumButtom = (Button) findViewById(R.id.buttonAlbumPhoto);
        selectFromAlbumButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageInAlbum();
            }
        });
    }

    // Called when image selection is done.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("AnalyzeActivity", "onActivityResult");

        imagUrl = data.getData();

        bitmap = ImageHelper.loadSizeLimitedBitmapFromUri(
                imagUrl, getContentResolver());
        if (bitmap != null) {
            // Show the image on screen.
            ImageView imageView = (ImageView) findViewById(R.id.selectedImage);
            imageView.setImageBitmap(bitmap);

            // Add detection log.
            Log.d("AnalyzeActivity", "Image: " + imagUrl + " resized to " + bitmap.getWidth()
                    + "x" + bitmap.getHeight());

            doRecognize();
        }
    }

    public void doRecognize() {
        buttonSelectImage.setEnabled(false);
        editText.setText("Analyzing...");

        try {
            new doRequest(this).execute();
        } catch (Exception e) {
            editText.setText("Error encountered. Exception is: " + e.toString());
        }
    }

    private String process() throws VisionServiceException, IOException, InterruptedException {
        Gson gson = new Gson();

        // Put the image into an input stream for detection.
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray())) {
                //post image and got operation from API
                HandwritingRecognitionOperation operation = this.client.createHandwritingRecognitionOperationAsync(inputStream);

                HandwritingRecognitionOperationResult operationResult;
                //try to get recognition result until it finished.

                int retryCount = 0;
                do {
                    if (retryCount > retryCountThreshold) {
                        throw new InterruptedException("Can't get result after retry in time.");
                    }
                    Thread.sleep(1000);
                    operationResult = this.client.getHandwritingRecognitionOperationResultAsync(operation.Url());
                }
                while (operationResult.getStatus().equals("NotStarted") || operationResult.getStatus().equals("Running"));

                String result = gson.toJson(operationResult);
                Log.d("result", result);
                return result;

            } catch (Exception ex) {
                throw ex;
            }
        } catch (Exception ex) {
            throw ex;
        }

    }

    /**
     * Retrieves the path of the image URI
     */
    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            return null;
        }
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return uri.getPath();
    }

    // When the button of "Take a Photo with Camera" is pressed.
    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null) {
            // Save the photo taken to a temporary file.
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try {
                mFilePhotoTaken = File.createTempFile(
                        "IMG_",  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );

                // Create the File where the photo should go
                // Continue only if the File was successfully created
                if (mFilePhotoTaken != null) {
                    mUriPhotoTaken = FileProvider.getUriForFile(this,
                            "com.example.mikezurawski.onyourmark",
                            mFilePhotoTaken);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mUriPhotoTaken);

                    // Finally start camera activity
                    startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                }
            } catch (IOException e) {
                setInfo(e.getMessage());
            }
        }
    }

    // When the button of "Select a Photo in Album" is pressed.
    public void selectImageInAlbum() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), REQUEST_SELECT_IMAGE_IN_ALBUM);
    }

    // Set the information panel on screen.
    private void setInfo(String info) {
        TextView textView = (TextView) findViewById(R.id.info);
        textView.setText(info);
    }

    // Class
    private class doRequest extends AsyncTask<String, String, String> {
        // Store error message
        private Exception e = null;
        private String receiptTotal = "";

        private WeakReference<AddItemActivity> recognitionActivity;

        public doRequest(AddItemActivity activity) {
            recognitionActivity = new WeakReference<AddItemActivity>(activity);
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                if (recognitionActivity.get() != null) {
                    return recognitionActivity.get().process();
                }
            } catch (Exception e) {
                this.e = e;    // Store error
            }

            return null;
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);

            if (recognitionActivity.get() == null) {
                return;
            }
            // Display based on error existence
            if (e != null) {
                recognitionActivity.get().editText.setText("Error in onPostExecute: " + e.getMessage());
                this.e = null;
            } else {
                Gson gson = new Gson();
                HandwritingRecognitionOperationResult r = gson.fromJson(data, HandwritingRecognitionOperationResult.class);

                StringBuilder resultBuilder = new StringBuilder();
                //if recognition result status is failed. display failed
                if (r.getStatus().equals("Failed")) {
                    resultBuilder.append("Error: Recognition Failed");
                } else {
                    // Getting lines
                    List<String> myList = new ArrayList<String>();

                    // Parse from response
                    for (HandwritingTextLine line : r.getRecognitionResult().getLines()) {
                        // Getting words
                        for (HandwritingTextWord word : line.getWords()) {
                            resultBuilder.append(word.getText() + " ");
                            // Add words to list for parsing
                            myList.add(word.getText().toLowerCase());
                        }
                        resultBuilder.append("\n");
                    }
                    // print for testing
                    for (String w : myList) {
                        Log.v(MY_TAG, w);
                    }

                    // Get subtotal
                    int subtotalIndex = myList.indexOf("subtotal");
                    int totalIndex = myList.indexOf("total");
                    String strTotal = "";

                    if(subtotalIndex != -1 || totalIndex != -1){
                        // If we just find the total and not subTotal set that to subtotalIndex
                        if (totalIndex >= 0 && subtotalIndex == -1) {
                            subtotalIndex = totalIndex;
                        }
                        while (isNumeric(myList.get(subtotalIndex + 1))) {
                            strTotal += (myList.get(subtotalIndex + 1));
                            Log.v(MY_TAG, "Subtotal is: " + strTotal);
                            subtotalIndex++;
                        }

                        // Check if number has a decimal, if not add one!!!
                        if(!strTotal.contains(".")) {
                            strTotal = new StringBuilder(strTotal).insert(
                                    strTotal.length()-2, ".").toString();
                            Log.v(MY_TAG,"Decimal value is: " + strTotal);
                            receiptTotal = strTotal;
                        }
                    }
                    resultBuilder.append("\n");
                }

                recognitionActivity.get().editText.setText(resultBuilder);
            }
            recognitionActivity.get().buttonSelectImage.setEnabled(true);

            // Give EditReceipt Activity the total
            Intent i = new Intent(getApplicationContext(), EditReceipt.class);
            i.putExtra("total", receiptTotal);
            startActivity(i);
        }
    }
}