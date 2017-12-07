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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mikezurawski.onyourmark.R;
import com.google.gson.Gson;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.HandwritingRecognitionOperation;
import com.microsoft.projectoxford.vision.contract.HandwritingRecognitionOperationResult;
import com.microsoft.projectoxford.vision.contract.HandwritingTextLine;
import com.microsoft.projectoxford.vision.contract.HandwritingTextWord;
import com.microsoft.projectoxford.vision.rest.VisionServiceException;

import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang.StringUtils.isNumeric;

public class AddItemActivity extends AppCompatActivity {

    private Button takePhotoButton;
    private Button selectFromAlbumBottom;
    private Button jumpToEditReceipt;

    private ProgressBar progressBar;

    private Uri imagUrl;
    private Bitmap bitmap;
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

        new HamburgerMenuHandler(this, R.id.toolbar_add_item, "Add New Item").init_subpage(false);

        if (client == null) {
            client = new VisionServiceRestClient(getString(R.string.subscription_key),
                    "https://westcentralus.api.cognitive.microsoft.com/vision/v1.0");
        }

        progressBar = findViewById(R.id.progress_spinner);

        // Launch take photo
        takePhotoButton = findViewById(R.id.buttonTakePhoto);
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });

        // Get photo from the gallery
        selectFromAlbumBottom = findViewById(R.id.buttonAlbumPhoto);
        selectFromAlbumBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageInAlbum();
            }
        });

        jumpToEditReceipt = findViewById(R.id.buttonEditReceipt);
        jumpToEditReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddItemActivity.this, EditReceipt.class);
                startActivity(intent);
            }
        });
    }

    // Called when image selection is done.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent();
                    intent.setData(Uri.fromFile(mFilePhotoTaken));
                    setResult(RESULT_OK, intent);

                    // Set the photo taken to be analyzed
                    bitmap = ImageHelper.loadSizeLimitedBitmapFromUri(
                            Uri.fromFile(mFilePhotoTaken), getContentResolver());

                    // Start analyzing
                    doRecognize();
                }
                break;
            case REQUEST_SELECT_IMAGE_IN_ALBUM:
                if (resultCode == RESULT_OK) {
                    if (requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM) {
                        Uri selectedImageUri = data.getData();
                        selectedImagePath = getPath(selectedImageUri);
                        Intent intent = new Intent();
                        intent.setData(selectedImageUri);
                        setResult(RESULT_OK, intent);

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
                }
                break;
            default:
                break;
        }
    }

    public void doRecognize() {
        selectFromAlbumBottom.setEnabled(false);
        takePhotoButton.setEnabled(false);
        jumpToEditReceipt.setEnabled(false);

        progressBar.setVisibility(View.VISIBLE);

        try {
            new doRequest(this).execute();
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
            //cursor.close(); // The app doesn't like this line
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

            Pattern datePattern = Pattern.compile("(\\d{2})/(\\d{2})/(\\d{4})");
            Date date = null;

            Pattern priceIndicatorPattern = Pattern.compile("((?<!SUB)TOTAL|BALANCEDUE|GRANDTOTAL|CASHDUE)");
            //Pattern pricePattern = Pattern.compile("\\d*(\\.\\d{2})?$");
            Pattern pricePattern = Pattern.compile("[0-9]*(\\.[0-9][0-9])?$");
            String price = null;

            if (recognitionActivity.get() == null) {
                return;
            }
            // Display based on error existence
            if (e != null) {
                //recognitionActivity.get().editText.setText("Error in onPostExecute: " + e.getMessage());
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
                    boolean PRICE_ON_NEXT_LINE = false;

                    // Parse from response
                    for (HandwritingTextLine line : r.getRecognitionResult().getLines()) {

                        Matcher m = datePattern.matcher(line.getText().replaceAll("\\s", ""));
                        if (date == null && m.find()) {
                            Calendar cal = Calendar.getInstance();
                            cal.set(Integer.parseInt(m.group(3)), Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
                            date = cal.getTime();
                        }

                        String upperCaseLine = line.getText().toUpperCase().replaceAll("\\s","");
                        m = priceIndicatorPattern.matcher(upperCaseLine);
                        if ((price == null && m.find()) || PRICE_ON_NEXT_LINE) {
                            Matcher a = pricePattern.matcher(upperCaseLine);

                            if (a.find() && !a.group(0).isEmpty()) {
                                price = a.group(0);
                                PRICE_ON_NEXT_LINE = false;
                            } else {
                                PRICE_ON_NEXT_LINE = !PRICE_ON_NEXT_LINE;
                            }
                        }
                    }
                }
            }
            recognitionActivity.get().takePhotoButton.setEnabled(true);
            recognitionActivity.get().selectFromAlbumBottom.setEnabled(true);
            recognitionActivity.get().jumpToEditReceipt.setEnabled(true);

            progressBar.setVisibility(View.GONE);

            // Give EditReceipt Activity the total
            Intent i = new Intent(getApplicationContext(), EditReceipt.class);

            if (price != null && !price.isEmpty()) {
                price = addDecimal(price);
                i.putExtra("total", price);
            }

            if (date != null) {
                i.putExtra("date",
                        String.format("%d/%d/%d",
                                getFromDate(date, Calendar.MONTH),
                                getFromDate(date, Calendar.DAY_OF_WEEK),
                                getFromDate(date, Calendar.YEAR)));
            }

            startActivity(i);
        }

        private Integer getFromDate(final Date date, final int type) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(type);
        }

        /**
         * Extract numbers from total/subtotal/tax
         * @param myList
         * @param index
         * @return
         */
        public String getNumbers(List<String> myList, int index) {
            String numbers = "";
            for(int i = index + 1; i < myList.size(); i++) {
                if(isNumeric(myList.get(i))) {
                    numbers += (myList.get(i));
                    Log.v(MY_TAG, "Total/Subtotal/Tax is: " + numbers);
                } else {
                    break;
                }
            }
            return numbers;
        }

        /**
         * Add decimal to string
         * @param value
         * @return
         */
        public String addDecimal (String value) {
            // Check if number has a decimal, if not add one!!!
            if(!value.contains(".")) {
                value = new StringBuilder(value).insert(
                        value.length()-2, ".").toString();
                Log.v(MY_TAG,"Decimal value is: " + value);
                return value;
            } else {
                return value;
            }
        }
    }
}