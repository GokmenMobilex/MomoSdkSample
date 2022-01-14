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
        seekBar.setProgress(DEFAULT_FILTER_LEVEL);
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
                case 0: // Filter.EyesHeight
                    resultBitmap = nativeLib.setEyesHeight(progress);
                    break;
                case 1: // Filter.MouthHeight
                    resultBitmap = nativeLib.setMouthHeight(progress);
                    break;
                case 2: // Filter.EyebrowHeight
                    resultBitmap = nativeLib.setEyebrowsHeight(progress);
                    break;
                case 3: // Filter.Brightness
                    resultBitmap = nativeLib.setBrightness(progress);
                    break;
                case 4: // Filter.ChinHeight
                    resultBitmap = nativeLib.setChinHeight(progress);
                    break;
                case 5: // Filter.BlackNWhite
                    resultBitmap = nativeLib.setBlackAndWhite(progress);
                    break;
                case 6: // Filter.Contrast
                    resultBitmap = nativeLib.setContrast(progress);
                    break;
                case 7: // Filter.ChinSize
                    resultBitmap = nativeLib.setChinSize(progress);
                    break;
                case 8: // Filter.ChinWidth
                    resultBitmap = nativeLib.setChinWidth(progress);
                    break;
                case 9: // Filter.EyebrowLifting
                    resultBitmap = nativeLib.setEyebrowsLifting(progress);
                    break;
                case 10: // Filter.EyebrowRotation
                    resultBitmap = nativeLib.setEyebrowsRotation(progress);
                    break;
                case 11: // Filter.EyebrowShape
                    resultBitmap = nativeLib.setEyebrowsShape(progress);
                    break;
                case 12: // Filter.EyeSize
                    resultBitmap = nativeLib.setEyesSize(progress);
                    break;
                case 13: // Filter.EyeWidth
                    resultBitmap = nativeLib.setEyesWidth(progress);
                    break;
                case 14: // Filter.EyeDistance
                    resultBitmap = nativeLib.setEyesDistance(progress);
                    break;
                case 15: // Filter.EyebrowSingleLift
                    resultBitmap = nativeLib.setEyebrowsSingleLifting(progress);
                    break;
                case 16: // Filter.Gamma
                    resultBitmap = nativeLib.setGamma(progress);
                    break;
                case 17: // Filter.Vignette
                    resultBitmap = nativeLib.setVignette(progress);
                    break;
                case 18: // Filter.Vibrance
                    resultBitmap = nativeLib.setVibrance(progress);
                    break;
                case 19: // Filter.Temperature
                    resultBitmap = nativeLib.setTemperature(progress);
                    break;
                case 20: // Filter.Structure
                    resultBitmap = nativeLib.setStructure(progress);
                    break;
                case 21: // Filter.Smile
                    resultBitmap = nativeLib.setSmileSeverity(progress);
                    break;
                case 22: // Filter.Sharpen
                    resultBitmap = nativeLib.setSharpen(progress);
                    break;
                case 23: // Filter.Saturation
                    resultBitmap = nativeLib.setSaturation(progress);
                    break;
                case 24: // Filter.NoseWidth
                    resultBitmap = nativeLib.setNoseWidth(progress);
                    break;
                case 25: // Filter.NoseTip
                    resultBitmap = nativeLib.setNoseTip(progress);
                    break;
                case 26: // Filter.NoseSize
                    resultBitmap = nativeLib.setNoseSize(progress);
                    break;
                case 27: // Filter.NoseNarrow
                    resultBitmap = nativeLib.setNoseNarrow(progress);
                    break;
                case 28: // Filter.NoseHeight
                    resultBitmap = nativeLib.setNoseHeight(progress);
                    break;
                case 29: // Filter.Lighten
                    resultBitmap = nativeLib.setLighten(progress);
                    break;
                default:
                    resultBitmap = null;
                    break;
            }

            if (resultBitmap != null) {
                imageView.setImageBitmap(resultBitmap);
            }
        } else {
            Toast.makeText(this, "Face is not detected", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Face is not detected");
        }
    }
}
