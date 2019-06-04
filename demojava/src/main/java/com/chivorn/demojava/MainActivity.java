package com.chivorn.demojava;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.chivorn.resourcemodule.MainApp;
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;

public class MainActivity extends MainApp {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smart_material_spinner_sample_layout);

        initBaseView();
        initItemList();
        initSpinnerInJava();
        onClickListener();
    }

    private void initSpinnerInJava() {
        spProvince.setItem(provinceList);
        spProvinceDialog.setItem(provinceList);
        spCustomColor.setItem(provinceList);
        spSearchable.setItem(provinceList);

        spCustomColor.setItemColor(ContextCompat.getColor(this, R.color.custom_item_color));
        spCustomColor.setSelectedItemListColor(ContextCompat.getColor(this, R.color.custom_selected_item_color));
        spCustomColor.setItemListColor(ContextCompat.getColor(this, R.color.custom_item_list_color));

        spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                spProvince.setErrorText("You are selecting on spinner item -> \"" + spProvince.getItem().get(position) + "\" . You can show it both in XML and programmatically and you can display as single line or multiple lines");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spProvince.setErrorText("On Nothing Selected");
            }
        });

        spProvinceDialog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                spProvinceDialog.setErrorText("You are selecting on spinner item -> \"" + spProvinceDialog.getItem().get(position) + "\" . You can show it both in XML and programmatically and you can display as single line or multiple lines");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spProvinceDialog.setErrorText("On Nothing Selected");
            }
        });

        spCustomColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                spCustomColor.setErrorText("You are selecting on spinner item -> \"" + spCustomColor.getItem().get(position) + "\" . You can show it both in XML and programmatically and you can display as single line or multiple lines");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spCustomColor.setErrorText("On Nothing Selected");
            }
        });

        spSearchable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                spSearchable.setErrorText("Your selected item is \"" + spSearchable.getItem().get(position) + "\" .");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spSearchable.setErrorText("On Nothing Selected");
            }
        });

        spEmptyItem.setOnEmptySpinnerClicked(new SmartMaterialSpinner.OnEmptySpinnerClickListener() {
            @Override
            public void onEmptySpinnerClicked() {
                Toast.makeText(MainActivity.this, getString(R.string.empty_item_spinner_click_msg), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
