package com.chivorn.demojava;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.chivorn.resourcemodule.MainApp;

import java.util.ArrayList;

public class MainActivity extends MainApp {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smart_material_spinner_sample_layout);

        initBaseView();
        initSpinnerInJava();
        onGithubRepoClickListenter();
    }

    private void initSpinnerInJava() {
        provinceList = new ArrayList<>();
        provinceList.add("Banteay Meanchey");
        provinceList.add("Battambang");
        provinceList.add("Kampong Cham");
        provinceList.add("Kampong Chhnang");
        provinceList.add("Kampong Speu");
        provinceList.add("Kampong Thom");
        provinceList.add("Kampot");
        provinceList.add("Kandal");
        provinceList.add("Kep");
        provinceList.add("Koh Kong");
        provinceList.add("Kratie");
        provinceList.add("Mondulkiri");
        provinceList.add("Oddar Meanchey");
        provinceList.add("Pailin");
        provinceList.add("Phnom Penh");
        provinceList.add("Preah Vihear");
        provinceList.add("Prey Veng");
        provinceList.add("Pursat");
        provinceList.add("Ratanakiri");
        provinceList.add("Siem Reap");
        provinceList.add("Sihanoukville");
        provinceList.add("Stung Treng");
        provinceList.add("Svay Rieng");
        provinceList.add("Takeo");
        provinceList.add("Tbong Khmum");

        spProvince.setItems(provinceList);
        spProvinceDialog.setItems(provinceList);

        spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(MainActivity.this, provinceList.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spProvinceDialog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(MainActivity.this, provinceList.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spEmptyItem.setShowEmptyDropdown(false);
        // Use this method instead of OnClicklistener() to handle touch even.
        spEmptyItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Toast.makeText(MainActivity.this, getString(R.string.empty_item_spinner_click_msg), Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        // TODO: Set underline color
        spProvince.setUnderlineColor(ContextCompat.getColor(getApplicationContext(), R.color.underline_color));
        spProvinceDialog.setUnderlineColor(ContextCompat.getColor(getApplicationContext(), R.color.underline_color));
    }
}
