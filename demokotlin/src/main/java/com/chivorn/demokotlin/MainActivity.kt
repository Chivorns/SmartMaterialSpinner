package com.chivorn.demokotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import chivorn.com.demokotlin.R
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private var spProvince: SmartMaterialSpinner? = null
    private var provinceList: MutableList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initSpinner()
    }

    private fun initSpinner() {
        spProvince = findViewById(R.id.sp_provinces)
        provinceList = ArrayList()

        provinceList!!.add("Kampong Thom")
        provinceList!!.add("Kampong Cham")
        provinceList!!.add("Kampong Chhnang")
        provinceList!!.add("Phnom Penh")
        provinceList!!.add("Kandal")
        provinceList!!.add("Kampot")

        spProvince!!.setItems<Any>(provinceList!!)

        spProvince!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@MainActivity, provinceList!!.get(position), Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        })
    }
}
