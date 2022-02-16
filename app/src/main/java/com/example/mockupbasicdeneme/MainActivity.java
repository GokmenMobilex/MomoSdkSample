package com.example.mockupbasicdeneme;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.momolib.NativeLib;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String FACEMARK_MODEL = "wo_dsnt_skn36_mltp2_98points_add_layer_dsnt_rmse_fm_12x12x288_grayimg_shape_192x192_data_300wv_43720plus_waug_20052.onnx";
    private static final String FACEMODEL_CUSTOM = "fdet_128_r_fbepoch_50_loss_3.onnx";

    ImageView imageView;
    Uri selectedImageUri;
    NativeLib nativeLib;
    Boolean isFacePhoto;

    private final ActivityResultLauncher<String> selectFile = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
        selectedImageUri = uri;
        imageView.setImageURI(selectedImageUri);

        try {
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap image = drawable.getBitmap();
            initializeNativeLib(image);
            isFacePhoto = nativeLib.isFaceDetected();
        } catch (Exception e) {
            Log.e(TAG, "Error on image from gallery switch case", e);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        Button galleryButton = findViewById(R.id.gallery_button);
        Button selectFilterButton = findViewById(R.id.select_filter_button);

        galleryButton.setOnClickListener(view -> selectImageFromGallery());
        selectFilterButton.setOnClickListener(view -> openFilterListActivity());
    }

    /**
     * SELECT IMAGE FROM GALLERY FUNCTION
     */
    private void selectImageFromGallery() {
        selectFile.launch("image/*");
    }

    /**
     * Open filter list to select filter
     */
    private void openFilterListActivity() {
        if (selectedImageUri != null) {
            Intent intent = new Intent(this, FilterListActivity.class);
            intent.putExtra("imageuri", selectedImageUri);
            intent.putExtra("nativelib", nativeLib);
            intent.putExtra("facephoto", isFacePhoto);
            startActivity(intent);
        } else {
            Log.e(TAG, "Image is not selected");
            Toast.makeText(this, "Please select an image first", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Initializes NativeLib with onnx models
     */
    private void initializeNativeLib(Bitmap bitmap) {
        InputStream inputStream = getResources().openRawResource(R.raw.fdet_128_r_fbepoch_50_loss_3);
        InputStream fmInputStream = getResources().openRawResource(R.raw.wo_dsnt_skn36_mltp2_98points_add_layer_dsnt_rmse_fm_12x12x288_grayimg_shape_192x192_data_300wv_43720plus_waug_20052);

        File faceDir = getDir("facelib", Context.MODE_PRIVATE);
        File faceModel = new File(faceDir, FACEMODEL_CUSTOM);
        File faceMarkModel = new File(faceDir, FACEMARK_MODEL);
        fileCopy(faceModel, inputStream);
        fileCopy(faceMarkModel, fmInputStream);

        nativeLib = new NativeLib();
        nativeLib.initLib(faceModel.getAbsolutePath(), faceMarkModel.getAbsolutePath());
        nativeLib.setImage(bitmap);
    }

    private void fileCopy(File mFile, InputStream mIs) {
        try {
            FileOutputStream mOs = new FileOutputStream(mFile);
            byte[] buffer = new byte[4096];
            int byteRead = mIs.read(buffer);

            while (byteRead != -1) {
                mOs.write(buffer, 0, byteRead);
                byteRead = mIs.read(buffer);
            }

            mIs.close();
            mOs.close();
        } catch (Exception e) {
            Log.e(TAG, "File copy error", e);
        }
    }
}
