package com.example.mockupbasicdeneme;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.momolib.NativeLib;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BaseFilter extends AppCompatActivity {

    private static final String TAG = "BaseFilter";
    private static final String FACEMARK_MODEL = "facemark_model.onnx";
    private static final String FACEMODEL_CUSTOM = "facemodel_custom.onnx";
    private static final int DEFAULT_FILTER_LEVEL = 50;

    ImageView imageView;
    SeekBar seekBar;
    NativeLib nativeLib;
    Bitmap bitmap;
    Bitmap resultBitmap;
    Uri selectedImageUri;
    int filterId;
    boolean isFaceDetected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_filter);

        seekBar = findViewById(R.id.seekBar);
        imageView = findViewById(R.id.imageView2);

        // Gets params from intent extras
        Intent intent = getIntent();
        selectedImageUri = intent.getParcelableExtra("imageuri");
        filterId = intent.getIntExtra("id", 0);
        if (selectedImageUri == null) {
            Toast.makeText(this, "Image is not found", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Image is not found");
        }

        setSelectedImage();
        initializeNativeLib();
        setSeekbarListener();
        // Applies default filter level
        applyFilter(DEFAULT_FILTER_LEVEL);
    }

    /**
     * Sets selected image on the image view
     */
    private void setSelectedImage() {
        // Bitmap conversion for filter engine
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes NativeLib with onnx models
     */
    private void initializeNativeLib() {
        File faceDir = getDir("facelib", Context.MODE_PRIVATE);
        File faceMarkModel = new File(faceDir, FACEMARK_MODEL);
        File faceModel = new File(faceDir, FACEMODEL_CUSTOM);

        InputStream fmInputStream = getResources().openRawResource(R.raw.facemark_model);
        InputStream inputStream = getResources().openRawResource(R.raw.facemodel_custom);

        fileCopy(faceMarkModel, fmInputStream);
        fileCopy(faceModel, inputStream);

        nativeLib = new NativeLib(); // initialize class instance from .aar file
        nativeLib.initLib(faceModel.getAbsolutePath(), faceMarkModel.getAbsolutePath());
        isFaceDetected = nativeLib.faceDetection(bitmap);
    }

    /**
     * Copies input stream as given file
     */
    private void fileCopy(File file, InputStream inputStream) {
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            int byteRead = inputStream.read(buffer);

            while (byteRead != -1) {
                outputStream.write(buffer, 0, byteRead);
                byteRead = inputStream.read(buffer);
            }

            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            Log.e(TAG, "Error while reading onnx models", e);
        }
    }

    /**
     * Sets seekbar change listener and applies filter with new value
     */
    private void setSeekbarListener() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // updated continuously as the user slides the thumb
                // applyFilter(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // called when the user first touches the SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                applyFilter(seekBar.getProgress());
            }
        });
    }

    /**
     * Applies selected filter with given value or warns about face not found
     */
    private void applyFilter(int progress) {
        if (isFaceDetected) {
            switch (filterId) {
                case 0:
                    resultBitmap = nativeLib.setEyesHeight(progress);
                    imageView.setImageBitmap(resultBitmap);
                    break;
                case 1:
                    resultBitmap = nativeLib.setMouthHeight(progress);
                    imageView.setImageBitmap(resultBitmap);
                case 2:
                    resultBitmap = nativeLib.setEyebrowsHeight(progress);
                    imageView.setImageBitmap(resultBitmap);
                    break;
                case 3:
                    resultBitmap = nativeLib.setBrightness(progress);
                    imageView.setImageBitmap(resultBitmap);
                    break;
                case 4:
                    resultBitmap = nativeLib.setChinHeight(progress);
                    imageView.setImageBitmap(resultBitmap);
                    break;
                case 5:
                    resultBitmap = nativeLib.setBlackAndWhite(progress);
                    imageView.setImageBitmap(resultBitmap);
                    break;
                case 6:
                    resultBitmap = nativeLib.setContrast(progress);
                    imageView.setImageBitmap(resultBitmap);
                    break;
                case 7:
                    resultBitmap = nativeLib.setChinSize(progress);
                    imageView.setImageBitmap(resultBitmap);
                    break;
                case 8:
                    resultBitmap = nativeLib.setChinWidth(progress);
                    imageView.setImageBitmap(resultBitmap);
                    break;
                case 9:
                    resultBitmap = nativeLib.setEyebrowsLifting(progress);
                    imageView.setImageBitmap(resultBitmap);
                    break;
                case 10:
                    resultBitmap = nativeLib.setEyebrowsRotation(progress);
                    imageView.setImageBitmap(resultBitmap);
                    break;
                case 11:
                    resultBitmap = nativeLib.setEyebrowsShape(progress);
                    imageView.setImageBitmap(resultBitmap);
                    break;
                case 12:
                    resultBitmap = nativeLib.setEyesSize(progress);
                    imageView.setImageBitmap(resultBitmap);
                    break;
                case 13:
                    resultBitmap = nativeLib.setEyesWidth(progress);
                    imageView.setImageBitmap(resultBitmap);
                    break;
                case 14:
                    resultBitmap = nativeLib.setEyebrowsSingleLifting(progress);
                    imageView.setImageBitmap(resultBitmap);
                    break;
                case 15:
                    resultBitmap = nativeLib.setEyesDistance(progress);
                    imageView.setImageBitmap(resultBitmap);
                    break;
                case 16:
                    resultBitmap = nativeLib.setGamma(progress);
                    imageView.setImageBitmap(resultBitmap);
                    break;
                case 17:
                    resultBitmap = nativeLib.setVignette(progress);
                    imageView.setImageBitmap(resultBitmap);
                    break;
                case 18:
                    resultBitmap = nativeLib.setVibrance(progress);
                    imageView.setImageBitmap(resultBitmap);
                    break;
                case 19:
                    resultBitmap = nativeLib.setTemperature(progress);
                    imageView.setImageBitmap(resultBitmap);
                    break;
                case 20:
                    resultBitmap = nativeLib.setStructure(progress);
                    imageView.setImageBitmap(resultBitmap);
                    break;
                case 21:
                    resultBitmap = nativeLib.setSmileSeverity(progress);
                    imageView.setImageBitmap(resultBitmap);
                    break;
                case 22:
                    resultBitmap = nativeLib.setSharpen(progress);
                    imageView.setImageBitmap(resultBitmap);
                    break;
                case 23:
                    resultBitmap = nativeLib.setSaturation(progress);
                    imageView.setImageBitmap(resultBitmap);
                    break;
                case 24:
                    resultBitmap = nativeLib.setNoseWidth(progress);
                    imageView.setImageBitmap(resultBitmap);
                    break;
                case 25:
                    resultBitmap = nativeLib.setNoseTip(progress);
                    imageView.setImageBitmap(resultBitmap);
                    break;
                case 26:
                    resultBitmap = nativeLib.setNoseSize(progress);
                    imageView.setImageBitmap(resultBitmap);
                    break;
                case 27:
                    resultBitmap = nativeLib.setNoseNarrow(progress);
                    imageView.setImageBitmap(resultBitmap);
                    break;
                case 28:
                    resultBitmap = nativeLib.setNoseHeight(progress);
                    imageView.setImageBitmap(resultBitmap);
                    break;
                case 29:
                    resultBitmap = nativeLib.setLighten(progress);
                    imageView.setImageBitmap(resultBitmap);
                    break;
                default:
                    break;
            }
        } else {
            Toast.makeText(this, "Face is not detected", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Face is not detected");
        }
    }
}
