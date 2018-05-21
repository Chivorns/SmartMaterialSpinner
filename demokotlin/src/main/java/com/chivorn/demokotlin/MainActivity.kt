package com.chivorn.demokotlin

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.chivorn.resourcemodule.MainApp
import java.util.*

class MainActivity : MainApp() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.smart_material_spinner_sample_layout)

        initBaseView()
        initSpinnerInKotlin()
        onGithubRepoClickListenter()
    }

    private fun initSpinnerInKotlin() {
        provinceList = ArrayList()
        provinceList!!.add("Kampong Thom")
        provinceList!!.add("Kampong Cham")
        provinceList!!.add("Kampong Chhnang")
        provinceList!!.add("Phnom Penh")
        provinceList!!.add("Kandal")
        provinceList!!.add("Kampot")

        spProvince!!.setItems<Any>(provinceList!!)

        spProvince!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@MainActivity, provinceList!![position], Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

        spEmptyItem!!.setShowEmptyDropdown(false)
        // Use this method instead of OnClicklistener() to handle touch even.
        spEmptyItem!!.setOnTouchListener({ v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                Toast.makeText(this@MainActivity, getString(R.string.empty_item_spinner_click_msg), Toast.LENGTH_SHORT).show()
            }
            false
        })

        // TODO: Set underline color
        spProvince.setUnderlineColor(ContextCompat.getColor(applicationContext, R.color.underline_color))
    }
}
