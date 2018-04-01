package com.appsinventiv.classifiedads.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;

import com.appsinventiv.classifiedads.Adapter.HomePageMobileAdsAdapter;
import com.appsinventiv.classifiedads.Adapter.SelectedImagesAdapter;
import com.appsinventiv.classifiedads.Category.MainCategory;
import com.appsinventiv.classifiedads.Classes.GifSizeFilter;
import com.appsinventiv.classifiedads.Model.AdDetails;
import com.appsinventiv.classifiedads.Model.PicturesModel;
import com.appsinventiv.classifiedads.Model.SelectedAdImages;
import com.appsinventiv.classifiedads.R;
import com.appsinventiv.classifiedads.Utils.CommonUtils;
import com.appsinventiv.classifiedads.Utils.SharedPrefs;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class SubmitAd extends AppCompatActivity {
    StorageReference mStorageRef;
    Button pickPicture, submitAd;
    EditText title, price, description;
    DatabaseReference mDatabase;

    ArrayList<String> imageUrl;

    SharedPreferences userPref;
    String username, city, phonenumber;

    List<Uri> mSelected;
    ArrayList<SelectedAdImages> selectedAdImages;
    SelectedImagesAdapter adapter;


    ArrayList<String> adCover;
    private static final int REQUEST_CODE_CHOOSE = 23;

    FirebaseFirestore db;
    long time;
    EditText usernameField, phoneField, locationField;
    TextView chooseCategoryText;
    public static String mainCategory, childCategory;
    public static Activity fa;

    @Override
    protected void onPostResume() {
        if (mainCategory == null) {
            chooseCategoryText.setText("Choose category");
        } else {
            if (childCategory != null) {
//                if (subChild != null) {
//                    chooseCategoryText.setText(mainCategory + " > " + childCategory + " > " + subChild);
//                } else {
                chooseCategoryText.setText(mainCategory + " > " + childCategory);
//                }
            } else {
                chooseCategoryText.setText(mainCategory);
            }
        }
        super.onPostResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_ad);
        fa = this;
        db = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        imageUrl = new ArrayList<String>();
        adCover = new ArrayList<String>();


        pickPicture = (Button) findViewById(R.id.pickpicture);
        submitAd = (Button) findViewById(R.id.submit);

        title = (EditText) findViewById(R.id.title);
        price = (EditText) findViewById(R.id.price);
        description = (EditText) findViewById(R.id.description);

        usernameField = (EditText) findViewById(R.id.username);
        phoneField = (EditText) findViewById(R.id.phone);
        locationField = (EditText) findViewById(R.id.location);

        chooseCategoryText = (TextView) findViewById(R.id.choose_category);


        chooseCategoryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SubmitAd.this, MainCategory.class);
                startActivity(i);

            }
        });


        submitAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitAd();
            }
        });
        time = System.currentTimeMillis();

        userPref = getSharedPreferences("userDetails", Context.MODE_PRIVATE);

        username = userPref.getString("username", "");
        phonenumber = userPref.getString("phone", "");
        city = userPref.getString("city", "");


        usernameField.setText(username);
        phoneField.setText("" + phonenumber);
        locationField.setText(city);

        showPickedPictures();

        pickPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedAdImages.clear();
                Matisse.from(SubmitAd.this)
                        .choose(MimeType.allOf())
                        .countable(true)
                        .maxSelectable(5)
                        .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .forResult(REQUEST_CODE_CHOOSE);
            }
        });

    }

    private void showPickedPictures() {
        selectedAdImages = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.horizontal_recycler_view);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(SubmitAd.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        adapter = new SelectedImagesAdapter(SubmitAd.this, selectedAdImages);
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);

            adapter.notifyDataSetChanged();
            for (Uri img :
                    mSelected) {
                selectedAdImages.add(new SelectedAdImages("" + img));
                adapter.notifyDataSetChanged();
                imageUrl.add(compressImage("" + img));
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    void submitAd() {

        String Adtitle = title.getText().toString(),
                AdDescription = description.getText().toString();
        long AdPrice = Long.parseLong(price.getText().toString());

        String username = SharedPrefs.getUsername();
        mDatabase.child("users").child(username).child("adsPosted").child("" + time).setValue("" + time);

        mDatabase.child("ads").child("" + time).setValue(new AdDetails(Adtitle, AdDescription, username, "" + phonenumber, city, ""
                , "yes", mainCategory, childCategory,
                time, AdPrice, 0)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SubmitAd.this, "Error" + e, Toast.LENGTH_SHORT).show();
            }
        });

        int count = 0;
        for (String img : imageUrl) {

            putPictures(img, "" + time, count);
            count++;
        }

        Intent i = new Intent(SubmitAd.this, SuccessPage.class);
        startActivity(i);
        mainCategory = null;
//        subChild = null;
        childCategory = null;
        finish();
    }

    public void putPictures(String path, final String key, final int count) {
//         file = data.getData();
        String imgName = Long.toHexString(Double.doubleToLongBits(Math.random()));

        ;
        Uri file = Uri.fromFile(new File(path));


        StorageReference riversRef = mStorageRef.child("Photos").child(imgName);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    @SuppressWarnings("VisibleForTests")
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content

                        Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        mDatabase.child("ads").child("" + time).child("pictures")
                                .push()
                                .setValue(new PicturesModel("" + downloadUrl, count));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Toast.makeText(SubmitAd.this, "error", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 1500.0f;
        float maxWidth = 1300.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

}

