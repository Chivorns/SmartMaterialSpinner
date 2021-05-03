package com.chivorn.demokotlin

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.chivorn.resourcemodule.MainApp
import com.chivorn.resourcemodule.R
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner.OnSpinnerEventListener

class MainActivity<T> : MainApp<T>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.smart_material_spinner_sample_layout)
        initBaseView()
        initItemList()
        initSpinner()
        onClickListener()
    }

    private fun initSpinner() {
        spSearchable.item = provinceList
        spReSelectable.item = provinceList
        spProvince.item = provinceList
        spProvinceOutlinedStyle.item = provinceList
        spProvinceDialog.item = provinceList
        spCustomColor.item = provinceList
        spCustomView.item = provinceList
        spNoHint.item = provinceList
        spSearchable.setSearchDialogGravity(Gravity.TOP)
        spSearchable.errorTextAlignment = SmartMaterialSpinner.TextAlignment.ALIGN_LEFT
        spSearchable.arrowPaddingRight = 19
        spCustomColor.itemColor = ContextCompat.getColor(this, R.color.custom_item_color)
        spCustomColor.selectedItemListColor = ContextCompat.getColor(this, R.color.custom_selected_item_color)
        spCustomColor.itemListColor = ContextCompat.getColor(this, R.color.custom_item_list_color)
        setOnEmptySpinnerClickListener(spEmptyItem)
        setOnItemSelectedListener(spSearchable, spReSelectable, spProvince, spProvinceOutlinedStyle, spProvinceDialog, spCustomColor, spEmptyItem, spVisibilityChanged, spCustomView, spNoHint)
        setOnSpinnerEventListener(spSearchable, spReSelectable, spProvince, spProvinceOutlinedStyle, spProvinceDialog, spCustomColor, spEmptyItem)
        // setItemTextSize(90, spSearchable, spProvince, spProvinceNoHint, spProvinceDialog, spCustomColor, spEmptyItem);
        //  setErrorTextSize(90, spSearchable, spProvince, spProvinceNoHint, spProvinceDialog, spCustomColor, spEmptyItem);
        //  setSelection(3, spSearchable, spProvince, spProvinceNoHint, spProvinceDialog, spCustomColor);
        // clearSelection(spSearchable, spProvince, spProvinceNoHint, spProvinceDialog, spCustomColor);

        // Get font from assets
        val celloSanLFont = Typeface.createFromAsset(this.assets, "fonts/hinted_cello_sans_light.ttf")
        val celloSanLIFont = Typeface.createFromAsset(this.assets, "fonts/hinted_cello_sans_light_italic.ttf")
        // Get font from resource
        val celloSanMIFont = ResourcesCompat.getFont(this, R.font.hinted_cello_sans_medium_italic)
        spSearchable.typeface = celloSanLFont
        spProvince.typeface = celloSanLIFont
        spReSelectable.typeface = celloSanMIFont

        // To custom dismiss button on search dialog
        /*    spSearchable.setEnableDismissSearch(true);
        spSearchable.setDismissSearchText("Hello");
        spSearchable.setDismissSearchColor(Color.parseColor("#4E61CC"));*/
    }

    private fun setOnItemSelectedListener(vararg spinners: SmartMaterialSpinner<*>) {
        for (spinner in spinners) {
            spinner.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(adapterView: AdapterView<*>?, view: View, position: Int, id: Long) {
                    if (spinner.id == R.id.sp_searchable) {
                        spinner.setErrorText("Your selected item is \"" + spinner.item[position] + "\" .")
                    } else if (spinner.id == R.id.sp_reselectable || spinner.id == R.id.sp_visibility_changed) {
                        Toast.makeText(this@MainActivity, "Selected item is: " + spinner.selectedItem, Toast.LENGTH_SHORT).show()
                    } else {
                        spinner.setErrorText("You are selecting on spinner item -> \"" + spinner.item[position] + "\" . You can show it both in XML and programmatically and you can display as single line or multiple lines")
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {
                    spinner.errorText = "On Nothing Selected"
                }
            }
        }
    }

    private fun setOnSpinnerEventListener(vararg spinners: SmartMaterialSpinner<*>) {
        for (spinner in spinners) {
            spinner.setOnSpinnerEventListener(object : OnSpinnerEventListener {
                override fun onSpinnerOpened(spinner: SmartMaterialSpinner<*>?) {
                    Log.i("SpinnerEventListener", "onSpinnerOpened: ")
                }

                override fun onSpinnerClosed(spinner: SmartMaterialSpinner<*>?) {
                    Log.i("SpinnerEventListener", "onSpinnerClosed: ")
                }
            })
        }
    }

    private fun setOnEmptySpinnerClickListener(vararg spinners: SmartMaterialSpinner<*>) {
        for (spinner in spinners) {
            spinner.setOnEmptySpinnerClickListener { Toast.makeText(this@MainActivity, getString(R.string.empty_item_spinner_click_msg), Toast.LENGTH_SHORT).show() }
        }
    }

    private fun setItemTextSize(size: Float, vararg spinners: SmartMaterialSpinner<*>) {
        for (spinner in spinners) {
            spinner.itemSize = size
        }
    }

    private fun setErrorTextSize(size: Int, vararg spinners: SmartMaterialSpinner<*>) {
        for (spinner in spinners) {
            spinner.errorTextSize = size.toFloat()
        }
    }

    private fun setSelection(index: Int, vararg spinners: SmartMaterialSpinner<*>) {
        for (spinner in spinners) {
            spinner.setSelection(index)
        }
    }

    private fun clearSelection(vararg spinners: SmartMaterialSpinner<*>) {
        for (spinner in spinners) {
            spinner.clearSelection()
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        if (v.id == R.id.btn_goto_runtime_render) {
            intent = Intent(applicationContext, RuntimeRenderViewActivity::class.java)
            startActivity(intent)
        }
    }
}