package com.example.mockupbasicdeneme;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.momolib.NativeLib;

import java.util.ArrayList;

public class FilterListActivity extends AppCompatActivity {

    Uri selectedImageUri;
    NativeLib nativeLib;
    Boolean isFacePhoto;
    ListView listView;
    ArrayList<Modules> moduleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_list);

        listView = findViewById(R.id.list_view);
        getIntentParams();

        ArrayList<String> filterList = getFilterList();
        moduleList = getModuleList();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filterList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener((adapterView, view, position, id) -> openBaseFilterActivity(position));
    }

    /**
     * Gets intent extras and sets passed parameters
     */
    private void getIntentParams() {
        Intent intent = getIntent();
        selectedImageUri = intent.getParcelableExtra("imageuri");
        nativeLib = intent.getParcelableExtra("nativelib");
        isFacePhoto = (Boolean) intent.getSerializableExtra("facephoto");
    }

    /**
     * Open base filter to test selected filter
     */
    private void openBaseFilterActivity(int position) {
        Intent navigateIntent = new Intent(this, BaseFilter.class);
        navigateIntent.putExtra("imageuri", selectedImageUri);
        navigateIntent.putExtra("id", position);
        navigateIntent.putExtra("nativelib", nativeLib);
        navigateIntent.putExtra("facephoto", isFacePhoto);
        navigateIntent.putExtra("module", moduleList.get(position));
        startActivity(navigateIntent);
    }

    private ArrayList<String> getFilterList() {
        return new ArrayList<String>() {
            {
                add("set eyes height"); // 0
                add("set mouth height");
                add("set eyebrow height");
                add("set brightness");
                add("set chin height");
                add("set black & white"); // 5
                add("set contrast");
                add("set chin size");
                add("set chin width");
                add("set eyebrow lifting");
                add("set eyebrow rotation"); // 10
                add("set eyebrow shape");
                add("set eye size");
                add("set eye width");
                add("set eye distance");
                add("set eyebrow single lift"); // 15
                add("set gamma");
                add("set vignette");
                add("set vibrance");
                add("set temperature");
                add("set structure"); // 20
                add("set smile severity");
                add("set sharpen");
                add("set saturation");
                add("set nose width");
                add("set nose tip"); // 25
                add("set nose size");
                add("set nose narrow");
                add("set nose height");
                add("set lighten");
                add("set mouth width"); // 30
                add("set mouth size");
                add("set eyes rotation");
                add("set glow");
                add("set grain");
                add("paint brush"); // 35
                add("paint glitter");
                add("acne remover");
                add("firm remover");
                add("smoothing");
                add("heal"); // 40
                add("cleanse");
                add("vanish");
            }
        };
    }

    private ArrayList<Modules> getModuleList() {
        return new ArrayList<Modules>() {
            {
                add(Modules.FACE_MORPHING_MODULE); // 0
                add(Modules.FACE_MORPHING_MODULE);
                add(Modules.FACE_MORPHING_MODULE);
                add(Modules.IMAGE_ENHANCEMENT_MODULE);
                add(Modules.FACE_MORPHING_MODULE);
                add(Modules.IMAGE_ENHANCEMENT_MODULE); // 5
                add(Modules.IMAGE_ENHANCEMENT_MODULE);
                add(Modules.FACE_MORPHING_MODULE);
                add(Modules.FACE_MORPHING_MODULE);
                add(Modules.FACE_MORPHING_MODULE);
                add(Modules.FACE_MORPHING_MODULE); // 10
                add(Modules.FACE_MORPHING_MODULE);
                add(Modules.FACE_MORPHING_MODULE);
                add(Modules.FACE_MORPHING_MODULE);
                add(Modules.FACE_MORPHING_MODULE);
                add(Modules.FACE_MORPHING_MODULE); // 15
                add(Modules.IMAGE_ENHANCEMENT_MODULE);
                add(Modules.IMAGE_ENHANCEMENT_MODULE);
                add(Modules.IMAGE_ENHANCEMENT_MODULE);
                add(Modules.IMAGE_ENHANCEMENT_MODULE);
                add(Modules.IMAGE_ENHANCEMENT_MODULE); // 20
                add(Modules.FACE_MORPHING_MODULE);
                add(Modules.IMAGE_ENHANCEMENT_MODULE);
                add(Modules.IMAGE_ENHANCEMENT_MODULE);
                add(Modules.FACE_MORPHING_MODULE);
                add(Modules.FACE_MORPHING_MODULE); // 25
                add(Modules.FACE_MORPHING_MODULE);
                add(Modules.FACE_MORPHING_MODULE);
                add(Modules.FACE_MORPHING_MODULE);
                add(Modules.IMAGE_ENHANCEMENT_MODULE);
                add(Modules.FACE_MORPHING_MODULE); // 30
                add(Modules.FACE_MORPHING_MODULE);
                add(Modules.FACE_MORPHING_MODULE);
                add(Modules.IMAGE_ENHANCEMENT_MODULE);
                add(Modules.IMAGE_ENHANCEMENT_MODULE);
                add(Modules.PAINT_MODULE); // 35
                add(Modules.PAINT_MODULE);
                add(Modules.HEAL_AND_SMOOTH_MODULE);
                add(Modules.HEAL_AND_SMOOTH_MODULE);
                add(Modules.HEAL_AND_SMOOTH_MODULE);
                add(Modules.HEAL_AND_SMOOTH_MODULE); // 40
                add(Modules.HEAL_AND_SMOOTH_MODULE);
                add(Modules.HEAL_AND_SMOOTH_MODULE);
            }
        };
    }
}
