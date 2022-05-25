package com.example.mockupbasicdeneme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.momolib.NativeLib;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BaseFilter extends AppCompatActivity {

    private static final String TAG = "BaseFilter";
    private static final String LUT_FILE_NAME = "arabica12.CUBE";
    private static final int DEFAULT_FILTER_LEVEL = 50;

    SeekBar seekBar;
    ImageView imageView;
    Bitmap glitterBitmap;
    Bitmap darkCircleBitmap;
    Bitmap selectedBitmap;
    Bitmap resultBitmap;
    Uri selectedImageUri;
    int filterId;
    NativeLib nativeLib;
    Boolean isFacePhoto;
    File arabic12LutFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_filter);

        seekBar = findViewById(R.id.seekBar);
        imageView = findViewById(R.id.imageView2);
        glitterBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.glittertexture);
        darkCircleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.darkcircle);

        getIntentParams();
        initConfirmButton();
        setSelectedImage();
        setLutFile();
        setSeekbarListener();
        setMotionListener();
    }

    /**
     * Gets intent extras and sets passed parameters
     */
    private void getIntentParams() {
        Intent intent = getIntent();
        selectedImageUri = intent.getParcelableExtra("imageuri");
        filterId = intent.getIntExtra("id", 0);
        nativeLib = intent.getParcelableExtra("nativelib");
        isFacePhoto = (Boolean) intent.getSerializableExtra("facephoto");
    }

    /**
     * Sets click listener on confirm button
     */
    private void initConfirmButton() {
        Button confirmButton = findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(view -> {
            resultBitmap = nativeLib.confirm();
            imageView.setImageBitmap(resultBitmap);
        });
    }

    /**
     * Reads bitmap from selectedImageUri
     */
    private void setSelectedImage() {
        // Bitmap conversion for filter engine
        try {
            selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
            imageView.setImageBitmap(selectedBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setLutFile() {
        InputStream arabic12LutStream = getResources().openRawResource(R.raw.arabica12);
        File LutDir = getDir("Lutlib", Context.MODE_PRIVATE);
        arabic12LutFile = new File(LutDir, LUT_FILE_NAME);
        fileCopy(arabic12LutFile, arabic12LutStream);
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
                applyFilter(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // called when the user first touches the SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // applyFilter(seekBar.getProgress());
            }
        });
    }

    /**
     * Sets touch listener and applies filter with new position
     */
    @SuppressLint("ClickableViewAccessibility")
    private void setMotionListener() {
        imageView.setOnTouchListener((v, event) -> {
            float eventX = event.getX();
            float eventY = event.getY();
            float[] eventXY = new float[]{eventX, eventY};

            Matrix invertMatrix = new Matrix();
            ((ImageView) v).getImageMatrix().invert(invertMatrix);

            invertMatrix.mapPoints(eventXY);
            int x = (int) eventXY[0];
            int y = (int) eventXY[1];

            Bitmap imageViewBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            boolean isOutOfBounds = (x < 0 || x > imageViewBitmap.getWidth()) ||
                    (y < 0 || y > imageViewBitmap.getHeight());
            if (isOutOfBounds) {
                // TODO: Even when isOutOfBounds is true,
                //  must handle ACTION_UP to finish paint
                Log.e(TAG, "Touched outside of the image");
            } else {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "Touch down position -> x: " + x + " - y: " + y);
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "Touch up position -> x: " + x + " - y: " + y);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.d(TAG, "Drag position -> x: " + x + " - y: " + y);
                        applyFilter(x, y);
                        break;
                }
            }

            return true;
        });
    }

    /**
     * Applies selected slider filter with given value
     */
    private void applyFilter(int progress) {
        long startTime = Calendar.getInstance().getTimeInMillis();
        switch (filterId) {
            case 0:
                resultBitmap = nativeLib.setEyesHeight(progress);
                break;
            case 1:
                resultBitmap = nativeLib.setMouthHeight(progress);
                break;
            case 2:
                resultBitmap = nativeLib.setEyebrowsHeight(progress);
                break;
            case 3:
                resultBitmap = nativeLib.setBrightness(progress);
                break;
            case 4:
                resultBitmap = nativeLib.setChinHeight(progress);
                break;
            case 5:
                resultBitmap = nativeLib.setBlackAndWhite(progress);
                break;
            case 6:
                resultBitmap = nativeLib.setContrast(progress);
                break;
            case 7:
                resultBitmap = nativeLib.setChinSize(progress);
                break;
            case 8:
                resultBitmap = nativeLib.setChinWidth(progress);
                break;
            case 9:
                resultBitmap = nativeLib.setEyebrowsLifting(progress);
                break;
            case 10:
                resultBitmap = nativeLib.setEyebrowsRotation(progress);
                break;
            case 11:
                resultBitmap = nativeLib.setEyebrowsShape(progress);
                break;
            case 12:
                resultBitmap = nativeLib.setEyesSize(progress);
                break;
            case 13:
                resultBitmap = nativeLib.setEyesWidth(progress);
                break;
            case 14:
                resultBitmap = nativeLib.setEyesDistance(progress);
                break;
            case 15:
                resultBitmap = nativeLib.setEyebrowsSingleLifting(progress);
                break;
            case 16:
                resultBitmap = nativeLib.setGamma(progress);
                break;
            case 17:
                resultBitmap = nativeLib.setVignette(progress);
                break;
            case 18:
                resultBitmap = nativeLib.setVibrance(progress);
                break;
            case 19:
                resultBitmap = nativeLib.setTemperature(progress);
                break;
            case 20:
                resultBitmap = nativeLib.setStructure(progress);
                break;
            case 21:
                resultBitmap = nativeLib.setSmileSeverity(progress);
                break;
            case 22:
                resultBitmap = nativeLib.setSharpen(progress);
                break;
            case 23:
                resultBitmap = nativeLib.setSaturation(progress);
                break;
            case 24:
                resultBitmap = nativeLib.setNoseWidth(progress);
                break;
            case 25:
                resultBitmap = nativeLib.setNoseTip(progress);
                break;
            case 26:
                resultBitmap = nativeLib.setNoseSize(progress);
                break;
            case 27:
                resultBitmap = nativeLib.setNoseNarrow(progress);
                break;
            case 28:
                resultBitmap = nativeLib.setNoseHeight(progress);
                break;
            case 29:
                resultBitmap = nativeLib.setLighten(progress);
                break;
            case 30:
                resultBitmap = nativeLib.setMouthWidth(progress);
                break;
            case 31:
                resultBitmap = nativeLib.setMouthSize(progress);
                break;
            case 32:
                resultBitmap = nativeLib.setEyesRotation(progress);
                break;
            case 33:
                resultBitmap = nativeLib.setGlow(progress);
                break;
            case 34:
                resultBitmap = nativeLib.setGrain(progress);
                break;
            case 43:
                resultBitmap = nativeLib.setShadows(progress);
                break;
            case 48:
                resultBitmap = nativeLib.eyesWhitener(progress);
                break;
            case 49:
                resultBitmap = nativeLib.teethWhitener(progress);
                break;
            case 50:
                resultBitmap = nativeLib.darkCirclesRemoverV1(darkCircleBitmap);
                break;
            case 51:
                resultBitmap = nativeLib.darkCirclesRemoverV2(darkCircleBitmap);
                break;
            case 52:
                resultBitmap = nativeLib.redEyesRemover();
                break;
            case 54:
                resultBitmap = nativeLib.LUT(arabic12LutFile.getAbsolutePath());
                break;
            default:
                resultBitmap = null;
                break;
        }

        if (resultBitmap != null) {
            long endTime = Calendar.getInstance().getTimeInMillis();
            Log.d(TAG, "Filter execution time: " + (endTime - startTime));
            imageView.setImageBitmap(resultBitmap);
        }
    }

    /**
     * Applies selected brush filter with given position
     */
    private void applyFilter(int positionX, int positionY) {
        long startTime = Calendar.getInstance().getTimeInMillis();
        switch (filterId) {
            case 35:
                resultBitmap = nativeLib.paintBrush(positionX, positionY, 255, 0, 0, 4, 0.5f);
                break;
            case 36:
                resultBitmap = nativeLib.paintGlitter(positionX, positionY, 255, 0, 0, 4, 0.5f, glitterBitmap);
                break;
            case 37:
                resultBitmap = nativeLib.acneRemover(positionX, positionY, 4);
                break;
            case 38:
                resultBitmap = nativeLib.firmRemover(positionX, positionY, 4);
                break;
            case 39:
                resultBitmap = nativeLib.smoothing(positionX, positionY, 4, 0.5F);
                break;
            case 40:
                Log.d(TAG, "Heal done");
                nativeLib.heal(positionX, positionY, 4, false);
                resultBitmap = nativeLib.heal(positionX, positionY, 4, true);
                break;
            case 41:
                Log.d(TAG, "Cleanse done");
                nativeLib.cleanse(positionX, positionY, 4, false);
                resultBitmap = nativeLib.cleanse(positionX, positionY, 4, true);
                break;
            case 42:
                Log.d(TAG, "Vanish done");
                nativeLib.vanish(positionX, positionY, 4, false);
                resultBitmap = nativeLib.vanish(positionX, positionY, 4, true);
                break;
            case 44:
                resultBitmap = nativeLib.paintSkin(positionX, positionY, 255, 0, 0, 4);
                break;
            case 45:
                resultBitmap = nativeLib.paintTone(positionX, positionY, 255, 0, 0, 4);
                break;
            case 46:
                resultBitmap = nativeLib.select2copy(positionX, positionY, positionX - 100, positionY - 100, 10, true, false);
                break;
            case 47:
                resultBitmap = nativeLib.textureChanger(positionX, positionY, 4, 0.5F, glitterBitmap);
                break;
            case 53:
                resultBitmap = nativeLib.reshape(positionX, positionY, 4, true);
                break;
            default:
                resultBitmap = null;
                break;
        }

        if (resultBitmap != null) {
            long endTime = Calendar.getInstance().getTimeInMillis();
            Log.d(TAG, "Filter execution time: " + (endTime - startTime));
            imageView.setImageBitmap(resultBitmap);
        }
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
