package com.chivorn.demokotlin

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.chivorn.resourcemodule.MainApp
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner

class MainActivity<T> : MainApp<T>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.smart_material_spinner_sample_layout)

        initBaseView()
        initItemList()
        initSpinnerInKotlin()
        onClickListener()
    }

    private fun initSpinnerInKotlin() {
        spSearchable!!.item = provinceList
        spProvince!!.item = provinceList
        spProvinceOutlinedStyle!!.item = provinceList
        spProvinceDialog!!.item = provinceList
        spCustomColor!!.item = provinceList
        spNoHint!!.item = provinceList

        setOnEmptySpinnerClickListener(spEmptyItem)
        setOnItemSelectedListener(spSearchable, spProvince, spProvinceOutlinedStyle, spProvinceDialog, spCustomColor, spEmptyItem)
        setOnSpinnerEventListener(spSearchable, spProvince, spProvinceOutlinedStyle, spProvinceDialog, spCustomColor, spEmptyItem)
    }

    private fun setOnItemSelectedListener(vararg spinners: SmartMaterialSpinner<*>) {
        for (spinner in spinners) {
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                    if (spinner.id == R.id.sp_searchable) {
                        spinner.errorText = "Your selected item is \"" + spinner.item[position] + "\" ."
                    } else {
                        spinner.errorText = "You are selecting on spinner item -> \"" + spinner.item[position] + "\" . You can show it both in XML and programmatically and you can display as single line or multiple lines"
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {
                    spinner.errorText = "On Nothing Selected"
                }
            }
        }
    }

    private fun setOnSpinnerEventListener(vararg spinners: SmartMaterialSpinner<*>) {
        for (spinner in spinners) {
            spinner.setOnSpinnerEventListener(object : SmartMaterialSpinner.OnSpinnerEventListener {
                override fun onSpinnerOpened(spinner: SmartMaterialSpinner<*>) {
                    Log.i("SpinnerEventListener", "onSpinnerOpened: ")
                }

                override fun onSpinnerClosed(spinner: SmartMaterialSpinner<*>) {
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
}
