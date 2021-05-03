package com.chivorn.demojava;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.chivorn.resourcemodule.MainApp;
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;

public class MainActivity<T> extends MainApp<T> {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smart_material_spinner_sample_layout);

        initBaseView();
        initItemList();
        initSpinner();
        onClickListener();
    }

    private void initSpinner() {
        spSearchable.setItem(provinceList);
        spReSelectable.setItem(provinceList);
        spProvince.setItem(provinceList);
        spProvinceOutlinedStyle.setItem(provinceList);
        spProvinceDialog.setItem(provinceList);
        spCustomColor.setItem(provinceList);
        spCustomView.setItem(provinceList);
        spNoHint.setItem(provinceList);

        spSearchable.setSearchDialogGravity(Gravity.TOP);
        spSearchable.setErrorTextAlignment(SmartMaterialSpinner.TextAlignment.ALIGN_LEFT);
        spSearchable.setArrowPaddingRight(19);
        spCustomColor.setItemColor(ContextCompat.getColor(this, R.color.custom_item_color));
        spCustomColor.setSelectedItemListColor(ContextCompat.getColor(this, R.color.custom_selected_item_color));
        spCustomColor.setItemListColor(ContextCompat.getColor(this, R.color.custom_item_list_color));

        setOnEmptySpinnerClickListener(spEmptyItem);
        setOnItemSelectedListener(spSearchable, spReSelectable, spProvince, spProvinceOutlinedStyle, spProvinceDialog, spCustomColor, spEmptyItem, spVisibilityChanged, spCustomView, spNoHint);
        setOnSpinnerEventListener(spSearchable, spReSelectable, spProvince, spProvinceOutlinedStyle, spProvinceDialog, spCustomColor, spEmptyItem);
        // setItemTextSize(90, spSearchable, spProvince, spProvinceNoHint, spProvinceDialog, spCustomColor, spEmptyItem);
        //  setErrorTextSize(90, spSearchable, spProvince, spProvinceNoHint, spProvinceDialog, spCustomColor, spEmptyItem);
        //  setSelection(3, spSearchable, spProvince, spProvinceNoHint, spProvinceDialog, spCustomColor);
        // clearSelection(spSearchable, spProvince, spProvinceNoHint, spProvinceDialog, spCustomColor);

        // Get font from assets
        Typeface celloSanLFont = Typeface.createFromAsset(this.getAssets(), "fonts/hinted_cello_sans_light.ttf");
        Typeface celloSanLIFont = Typeface.createFromAsset(this.getAssets(), "fonts/hinted_cello_sans_light_italic.ttf");
        // Get font from resource
        Typeface celloSanMIFont = ResourcesCompat.getFont(this, R.font.hinted_cello_sans_medium_italic);
        spSearchable.setTypeface(celloSanLFont);
        spProvince.setTypeface(celloSanLIFont);
        spReSelectable.setTypeface(celloSanMIFont);

        // To custom dismiss button on search dialog
    /*    spSearchable.setEnableDismissSearch(true);
        spSearchable.setDismissSearchText("Hello");
        spSearchable.setDismissSearchColor(Color.parseColor("#4E61CC"));*/
    }

    private void setOnItemSelectedListener(SmartMaterialSpinner... spinners) {
        for (final SmartMaterialSpinner spinner : spinners) {
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    if (spinner.getId() == R.id.sp_searchable) {
                        spinner.setErrorText("Your selected item is \"" + spinner.getItem().get(position) + "\" .");
                    } else if (spinner.getId() == R.id.sp_reselectable || spinner.getId() == R.id.sp_visibility_changed) {
                        Toast.makeText(MainActivity.this, "Selected item is: " + spinner.getSelectedItem(), Toast.LENGTH_SHORT).show();
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

    private void setItemTextSize(float size, SmartMaterialSpinner... spinners) {
        for (SmartMaterialSpinner spinner : spinners) {
            spinner.setItemSize(size);
        }
    }

    private void setErrorTextSize(int size, SmartMaterialSpinner... spinners) {
        for (SmartMaterialSpinner spinner : spinners) {
            spinner.setErrorTextSize(size);
        }
    }

    private void setSelection(int index, SmartMaterialSpinner... spinners) {
        for (SmartMaterialSpinner spinner : spinners) {
            spinner.setSelection(index);
        }
    }

    private void clearSelection(SmartMaterialSpinner... spinners) {
        for (SmartMaterialSpinner spinner : spinners) {
            spinner.clearSelection();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == com.chivorn.resourcemodule.R.id.btn_goto_runtime_render) {
            intent = new Intent(getApplicationContext(), RuntimeRenderViewActivity.class);
            startActivity(intent);
        }
    }
}