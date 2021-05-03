package com.chivorn.demokotlin

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.LinearLayout
import android.widget.Toast
import com.chivorn.resourcemodule.MainApp
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner

class RuntimeRenderViewActivity<T> : MainApp<T>() {
    private var layoutContainer: LinearLayout? = null
    private var itemListMap: SparseArray<List<String>>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_runtime_render_view_activity)
        initViewObject()
        initItemList()
        initItemListMap()
        renderView()
    }

    private fun initViewObject() {
        layoutContainer = findViewById(R.id.dynamic_fields_layout)
    }

    private fun initItemListMap() {
        itemListMap = SparseArray()
        itemListMap!!.put(0, countryList)
        itemListMap!!.put(1, countryList.subList(0, (countryList.size - 1) / 2))
        itemListMap!!.put(2, countryList.subList(0, 200))
        itemListMap!!.put(3, countryList.subList(0, 100))
        itemListMap!!.put(4, countryList.subList(0, 50))
        itemListMap!!.put(5, countryList.subList(0, 1))
    }

    private fun renderView() {
        for (i in 0..5) {
            val smsp = SmartMaterialSpinner<String>(this)
            smsp.isReSelectable = true
            smsp.hintColor = Color.GRAY
            smsp.underlineColor = Color.GRAY
            smsp.errorText = "This is error text. Hello Cambodia Kingdom of Wonder. This is error text. Hello Cambodia Kingdom of Wonder. This is error text. Hello Cambodia Kingdom of Wonder. This is error text. Hello Cambodia Kingdom of Wonder. "
            smsp.item = itemListMap!![i]
            smsp.hint = "Spinner " + (i + 1)
            if (smsp.count >= 100) {
                smsp.isSearchable = true
            }
            smsp.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                    Toast.makeText(applicationContext, "You selected on " + smsp.selectedItem, Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Log.i("AAA", "onNothingSelected: ")
                }
            }
            layoutContainer!!.addView(smsp)
        }
        for (i in 0..5) {
            val smsp = SmartMaterialSpinner<String>(this)
            smsp.isReSelectable = true
            smsp.hintColor = Color.GRAY
            smsp.underlineColor = Color.GRAY
            smsp.errorText = "This is error text. Hello Cambodia Kingdom of Wonder. This is error text. Hello Cambodia Kingdom of Wonder. This is error text. Hello Cambodia Kingdom of Wonder. This is error text. Hello Cambodia Kingdom of Wonder. "
            smsp.item = itemListMap!![i]
            smsp.hint = "Normal Spinner " + (i + 1)
            layoutContainer!!.addView(smsp)
        }
        for (i in 0..5) {
            val smsp = SmartMaterialSpinner<String>(this)
            smsp.isReSelectable = true
            smsp.hintColor = Color.GRAY
            smsp.underlineColor = Color.GRAY
            smsp.errorText = "This is error text. Hello Cambodia Kingdom of Wonder. This is error text. Hello Cambodia Kingdom of Wonder. This is error text. Hello Cambodia Kingdom of Wonder. This is error text. Hello Cambodia Kingdom of Wonder. "
            smsp.isMultilineError = false
            smsp.item = itemListMap!![i]
            smsp.hint = "Single Line Error Spinner " + (i + 1)
            layoutContainer!!.addView(smsp)
        }
    }
}