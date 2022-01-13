package com.example.mockupbasicdeneme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FilterListActivity extends AppCompatActivity {

    private static final String TAG = "FilterListActivity";

    Uri selectedImageUri;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_list);

        listView = findViewById(R.id.list_view);

        selectedImageUri = getIntent().getParcelableExtra("imageuri");
        if (selectedImageUri == null) {
            Toast.makeText(this, "Image is not found", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Image is not found");
        }

        ArrayList<String> filterList = new ArrayList<String>() {
            {
                add("set eyes height");
                add("set mouth height");
                add("set eyebrow height");
                add("set brightness");
                add("set chin height");
                add("set black & white");
                add("set contrast ");
                add("set chin size");
                add("set chin width ");
                add("set eyebrow lifting");
                add("set eyebrow rotation");
                add("set eyebrow shape");
                add("set eye size");
                add("set eye width");
                add("set eye distance");
                add("set eyebrow single lift");
                add("set gamma");
                add("set vignette");
                add("set vibrance");
                add("set temperature");
                add("set structure");
                add("set smile");
                add("set sharpen");
                add("set saturation");
                add("set nose width");
                add("set nose tip");
                add("set nose size");
                add("set nose narrow");
                add("set nose height");
                add("set lighten");
            }
        };
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filterList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener((adapterView, view, position, id) -> openBaseFilterActivity(position));
    }

    /**
     * Open base filter to test selected filter
     */
    private void openBaseFilterActivity(int position) {
        Intent navigateIntent = new Intent(this, BaseFilter.class);
        navigateIntent.putExtra("imageuri", selectedImageUri);
        navigateIntent.putExtra("id", position);
        startActivity(navigateIntent);
    }
}