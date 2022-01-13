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

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filterList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener((adapterView, view, position, id) -> openBaseFilterActivity(position));
    }

    /**
     * Open base filter to test selected filter
     */
    private void openBaseFilterActivity(int position) {
        String selectedFilterDesc = filterList.get(position);
        Filter selectedFilter = Filter.getFilterByDesc(selectedFilterDesc);
        if (selectedFilter == null) {
            Toast.makeText(this, "Unknown filter type", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Unknown filter type");
            return;
        }

        Intent navigateIntent = new Intent(this, BaseFilter.class);
        navigateIntent.putExtra("imageuri", selectedImageUri);
        navigateIntent.putExtra("id", selectedFilter.filterId);
        startActivity(navigateIntent);
    }

    private final ArrayList<String> filterList = new ArrayList<String>() {
        {
            add(Filter.EyesHeight.description);
            add(Filter.MouthHeight.description);
            add(Filter.EyebrowHeight.description);
            add(Filter.Brightness.description);
            add(Filter.ChinHeight.description);
            add(Filter.BlackNWhite.description);
            add(Filter.Contrast.description);
            add(Filter.ChinSize.description);
            add(Filter.ChinWidth.description);
            add(Filter.EyebrowLifting.description);
            add(Filter.EyebrowRotation.description);
            add(Filter.EyebrowShape.description);
            add(Filter.EyeSize.description);
            add(Filter.EyeWidth.description);
            add(Filter.EyeDistance.description);
            add(Filter.EyebrowSingleLift.description);
            add(Filter.Gamma.description);
            add(Filter.Vignette.description);
            add(Filter.Vibrance.description);
            add(Filter.Temperature.description);
            add(Filter.Structure.description);
            add(Filter.Smile.description);
            add(Filter.Sharpen.description);
            add(Filter.Saturation.description);
            add(Filter.NoseWidth.description);
            add(Filter.NoseTip.description);
            add(Filter.NoseSize.description);
            add(Filter.NoseNarrow.description);
            add(Filter.NoseHeight.description);
            add(Filter.Lighten.description);
        }
    };
}