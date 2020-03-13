package com.chivorn.demojava;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chivorn.resourcemodule.MainApp;
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;

import java.util.List;


public class RuntimeRenderViewActivity<T> extends MainApp<T> {
    private LinearLayout layoutContainer;
    private SparseArray<List<String>> itemListMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runtime_render_view_activity);

        initViewObject();
        initItemList();
        initItemListMap();
        renderView();
    }

    private void initViewObject() {
        layoutContainer = findViewById(R.id.dynamic_fields_layout);
    }

    private void initItemListMap() {
        itemListMap = new SparseArray<>();
        itemListMap.put(0, countryList);
        itemListMap.put(1, countryList.subList(0, (countryList.size() - 1) / 2));
        itemListMap.put(2, countryList.subList(0, 200));
        itemListMap.put(3, countryList.subList(0, 100));
        itemListMap.put(4, countryList.subList(0, 50));
        itemListMap.put(5, countryList.subList(0, 1));
    }

    private void renderView() {
        for (int i = 0; i < 6; i++) {
            final SmartMaterialSpinner<String> smsp = new SmartMaterialSpinner<>(this);
            smsp.setReSelectable(true);
            smsp.setHintColor(Color.GRAY);
            smsp.setUnderlineColor(Color.GRAY);
            smsp.setErrorText("This is error text. Hello Cambodia Kingdom of Wonder. This is error text. Hello Cambodia Kingdom of Wonder. This is error text. Hello Cambodia Kingdom of Wonder. This is error text. Hello Cambodia Kingdom of Wonder. ");
            smsp.setItem(itemListMap.get(i));
            smsp.setHint("Spinner " + (i + 1));

            if (smsp.getCount() >= 100) {
                smsp.setSearchable(true);
            }

            smsp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getApplicationContext(), "You selected on " + smsp.getSelectedItem(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Log.i("AAA", "onNothingSelected: ");
                }
            });
            layoutContainer.addView(smsp);
        }

        for (int i = 0; i < 6; i++) {
            final SmartMaterialSpinner<String> smsp = new SmartMaterialSpinner<>(this);
            smsp.setReSelectable(true);
            smsp.setHintColor(Color.GRAY);
            smsp.setUnderlineColor(Color.GRAY);
            smsp.setErrorText("This is error text. Hello Cambodia Kingdom of Wonder. This is error text. Hello Cambodia Kingdom of Wonder. This is error text. Hello Cambodia Kingdom of Wonder. This is error text. Hello Cambodia Kingdom of Wonder. ");
            smsp.setItem(itemListMap.get(i));
            smsp.setHint("Normal Spinner " + (i + 1));
            layoutContainer.addView(smsp);
        }

        for (int i = 0; i < 6; i++) {
            final SmartMaterialSpinner<String> smsp = new SmartMaterialSpinner<>(this);
            smsp.setReSelectable(true);
            smsp.setHintColor(Color.GRAY);
            smsp.setUnderlineColor(Color.GRAY);
            smsp.setErrorText("This is error text. Hello Cambodia Kingdom of Wonder. This is error text. Hello Cambodia Kingdom of Wonder. This is error text. Hello Cambodia Kingdom of Wonder. This is error text. Hello Cambodia Kingdom of Wonder. ");
            smsp.setMultilineError(false);
            smsp.setItem(itemListMap.get(i));
            smsp.setHint("Single Line Error Spinner " + (i + 1));
            layoutContainer.addView(smsp);
        }
    }
}
