package com.example.mockupbasicdeneme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    //private static final String FACEMARK_MODEL = "facemark_model.onnx";
    //private static final String FACEMODEL_CUSTOM = "facemodel_custom.onnx";

    ImageView imageView;
    Uri selectedImageUri;

    private final ActivityResultLauncher<String> selectFile = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
        selectedImageUri = uri;
        imageView.setImageURI(selectedImageUri);

        /*try {
            selectedImageUri = data.getData();
            imageView.setImageURI(selectedImageUri);
            //BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            //Bitmap image = drawable.getBitmap();

            //NativeLib nativeLib = new NativeLib(); // initialize class instance from  .aar file

            //InputStream fmInputStream = getResources().openRawResource(R.raw.facemark_model);
            //InputStream inputStream = getResources().openRawResource(R.raw.facemodel_custom);

            //File faceDir = getDir("facelib", Context.MODE_PRIVATE);
            //File faceModel = new File(faceDir,FACEMODEL_CUSTOM);
            //File fmModel = new File(faceDir, FACEMARK_MODEL);
            //fileCopy(faceModel,inputStream);
            //fileCopy(fmModel,fmInputStream);

            //nativeLib.initLib(faceModel.getAbsolutePath(),fmModel.getAbsolutePath());

            // if (nativeLib.faceDetection(image)){
            //Bitmap resultBitmap = nativeLib.getBitmapWithLandmarks();
            //    Bitmap resultBitmap = nativeLib.setEyesHeight(100);

            //    imageView.setImageBitmap(resultBitmap);
            // }else{
            //     imageView.setImageResource(R.drawable.default_image);
            //  }

            //  break;
        } catch (Exception e) {
            System.out.println("error on image from gallery switch case " + e);
        }*/
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
     * Open file picker to select image
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
            startActivity(intent);
        } else {
            Log.e(TAG, "Image is not selected");
            Toast.makeText(this, "Please select an image first", Toast.LENGTH_LONG).show();
        }
    }

    /*private void fileCopy(File mFile, InputStream mIs) {
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
            System.out.println("Error" + e);
        }
    }*/
}
