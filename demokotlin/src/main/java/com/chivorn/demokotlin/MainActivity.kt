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
        spProvince!!.items = (provinceList as List<Any>?)!!
        spProvinceDialog!!.items = provinceList as List<Any>
        spCustomColor!!.items = provinceList as List<Any>
        spSearchable!!.items = provinceList as List<Any>

        spProvince!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                spProvince?.error = "You are selecting on spinner item -> \"" + spProvince.items[position] + "\" . You can show it both in XML and programmatically and you can display as single line or multiple lines"
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

        spProvinceDialog!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                spProvinceDialog?.error = "You are selecting on spinner item -> \"" + spProvinceDialog.items[position] + "\" . You can show it both in XML and programmatically and you can display as single line or multiple lines"
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

        spCustomColor!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                spCustomColor?.error = "You are selecting on spinner item -> \"" + spCustomColor.items[position] + "\" . You can show it both in XML and programmatically and you can display as single line or multiple lines"
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

        spSearchable!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                spSearchable?.error = "Your selected item is \"" + spSearchable.items[position] + "\" ."
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

        spEmptyItem!!.setOnEmptySpinnerClicked { Toast.makeText(this@MainActivity, getString(R.string.empty_item_spinner_click_msg), Toast.LENGTH_SHORT).show() }
    }
}
