package com.chivorn.demokotlin

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.chivorn.resourcemodule.MainApp

class MainActivity : MainApp() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.smart_material_spinner_sample_layout)

        initBaseView()
        initItemList()
        initSpinnerInKotlin()
        onClickListener()
    }

    private fun initSpinnerInKotlin() {
        spProvince!!.item = (provinceList as List<Any>?)!!
        spProvinceDialog!!.item = provinceList as List<Any>
        spCustomColor!!.item = provinceList as List<Any>
        spSearchable!!.item = provinceList as List<Any>

        spProvince!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                spProvince?.errorText = "You are selecting on spinner item -> \"" + spProvince.item[position] + "\" . You can show it both in XML and programmatically and you can display as single line or multiple lines"
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
                spProvince?.errorText = "On Nothing Selected"
            }
        }

        spProvinceDialog!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                spProvinceDialog?.errorText = "You are selecting on spinner item -> \"" + spProvinceDialog.item[position] + "\" . You can show it both in XML and programmatically and you can display as single line or multiple lines"
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
                spProvinceDialog?.errorText = "On Nothing Selected"
            }
        }

        spCustomColor!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                spCustomColor?.errorText = "You are selecting on spinner item -> \"" + spCustomColor.item[position] + "\" . You can show it both in XML and programmatically and you can display as single line or multiple lines"
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
                spCustomColor?.errorText = "On Nothing Selected"
            }
        }

        spSearchable!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                spSearchable?.errorText = "Your selected item is \"" + spSearchable.item[position] + "\" ."
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {
                spSearchable.errorText = "On Nothing Selected"
            }
        }

        spEmptyItem!!.setOnEmptySpinnerClicked { Toast.makeText(this@MainActivity, getString(R.string.empty_item_spinner_click_msg), Toast.LENGTH_SHORT).show() }
    }
}
