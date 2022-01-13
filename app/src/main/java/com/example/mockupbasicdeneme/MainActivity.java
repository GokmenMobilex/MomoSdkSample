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

    ImageView imageView;
    Uri selectedImageUri;

    private final ActivityResultLauncher<String> selectFile = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
        selectedImageUri = uri;
        imageView.setImageURI(selectedImageUri);
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
}
