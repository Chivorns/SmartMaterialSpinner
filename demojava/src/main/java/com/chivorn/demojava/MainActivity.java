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
        spProvince.setItems(provinceList);
        spProvinceDialog.setItems(provinceList);
        spCustomColor.setItems(provinceList);
        spSearchable.setItems(provinceList);

        spCustomColor.setItemColor(ContextCompat.getColor(this, R.color.custom_item_color));
        spCustomColor.setSelectedItemColor(ContextCompat.getColor(this, R.color.custom_selected_item_color));
        spCustomColor.setItemListColor(ContextCompat.getColor(this, R.color.custom_item_list_color));

        spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                spProvince.setError("You are selecting on spinner item -> \"" + spProvince.getItems().get(position) + "\" . You can show it both in XML and programmatically and you can display as single line or multiple lines");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(MainActivity.this, "On Nothing Selected", Toast.LENGTH_SHORT).show();
            }
        });

        spProvinceDialog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                spProvinceDialog.setError("You are selecting on spinner item -> \"" + spProvinceDialog.getItems().get(position) + "\" . You can show it both in XML and programmatically and you can display as single line or multiple lines");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(MainActivity.this, "On Nothing Selected", Toast.LENGTH_SHORT).show();
            }
        });

        spCustomColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                spCustomColor.setError("You are selecting on spinner item -> \"" + spCustomColor.getItems().get(position) + "\" . You can show it both in XML and programmatically and you can display as single line or multiple lines");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(MainActivity.this, "On Nothing Selected", Toast.LENGTH_SHORT).show();
            }
        });

        spSearchable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                spSearchable.setError("Your selected item is \"" + spSearchable.getItems().get(position) + "\" .");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(MainActivity.this, "On Nothing Selected", Toast.LENGTH_SHORT).show();
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
