package com.chivorn.demojava;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.chivorn.resourcemodule.MainApp;
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;

public class MainActivity<T> extends MainApp<T> {
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
        spSearchable.setItem(provinceList);
        spProvince.setItem(provinceList);
        spProvinceNoHint.setItem(provinceList);
        spProvinceDialog.setItem(provinceList);
        spCustomColor.setItem(provinceList);

        spSearchable.setSearchDialogGravity(Gravity.TOP);
        spSearchable.setArrowMarginRight(19);
        spCustomColor.setItemColor(ContextCompat.getColor(this, R.color.custom_item_color));
        spCustomColor.setSelectedItemListColor(ContextCompat.getColor(this, R.color.custom_selected_item_color));
        spCustomColor.setItemListColor(ContextCompat.getColor(this, R.color.custom_item_list_color));

        setOnEmptySpinnerClickListener(spEmptyItem);
        setOnItemSelectedListener(spSearchable, spProvince, spProvinceNoHint, spProvinceDialog, spCustomColor, spEmptyItem);
        setOnSpinnerEventListener(spSearchable, spProvince, spProvinceNoHint, spProvinceDialog, spCustomColor, spEmptyItem);
    }

    private void setOnItemSelectedListener(SmartMaterialSpinner... spinners) {
        for (final SmartMaterialSpinner spinner : spinners) {
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    if (spinner.getId() == R.id.sp_searchable) {
                        spinner.setErrorText("Your selected item is \"" + spinner.getItem().get(position) + "\" .");
                    } else {
                        spinner.setErrorText("You are selecting on spinner item -> \"" + spinner.getItem().get(position) + "\" . You can show it both in XML and programmatically and you can display as single line or multiple lines");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    spinner.setErrorText("On Nothing Selected");
                }
            });
        }
    }

    private void setOnSpinnerEventListener(SmartMaterialSpinner... spinners) {
        for (SmartMaterialSpinner spinner : spinners) {
            spinner.setOnSpinnerEventListener(new SmartMaterialSpinner.OnSpinnerEventListener() {
                @Override
                public void onSpinnerOpened(SmartMaterialSpinner spinner) {
                    Log.i("SpinnerEventListener", "onSpinnerOpened: ");
                }

                @Override
                public void onSpinnerClosed(SmartMaterialSpinner spinner) {
                    Log.i("SpinnerEventListener", "onSpinnerClosed: ");
                }
            });
        }
    }

    private void setOnEmptySpinnerClickListener(SmartMaterialSpinner... spinners) {
        for (SmartMaterialSpinner spinner : spinners) {
            spinner.setOnEmptySpinnerClickListener(new SmartMaterialSpinner.OnEmptySpinnerClickListener() {
                @Override
                public void onEmptySpinnerClicked() {
                    Toast.makeText(MainActivity.this, getString(R.string.empty_item_spinner_click_msg), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}