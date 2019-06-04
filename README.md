# SmartMaterialSpinner 
[ ![Download](https://api.bintray.com/packages/chivorn/maven/smartmaterialspinner/images/download.svg) ]

The best Android spinner library for your android application with more customization

## Available on Play Store
<a href="https://play.google.com/store/apps/details?id=com.chivorn.smsp.demojava" target="_blank">
  <img src="https://github.com/Chivorns/Resources/blob/master/google-play-badge.png" alt="Play Store" width="200" ">
</a>
                                                                                                                   
## Available features
- Searchable spinner
  - smsp_isSearchable : Default is false. Set it to true to enable this feature.
  - smsp_enableSearchHeader: Default is true. Set it to false to hide search header.
  - smsp_searchHeaderText: Text display as header text
  - smsp_searchHeaderTextColor: For changing header text color
  - smsp_searchHeaderBackgroundColor: For changing header background color
  - smsp_searchHint: To set query text hint
- Typeface (smsp_typeface)
- Base color and highlight color
- Floating Label
  - smsp_enableFloatingLabel: Default is true. Set it to false to disable floating label.
  - smsp_alwaysShowFloatingLabel: Default is false. Set it to true for always display.
  - smsp_floatingLabelText: Update it to what text you want. If the value is not set, it get from hint. 
  - smsp_floatingLabelSize: For changing floating label size
  - smsp_floatingLabelColor: For changing floating label color
- Error Label
  - smsp_enableErrorLabel: Default it is true.
  - smsp_errorText: Your error message text.
  - smsp_errorTextSize: For changing error text size
  - smsp_errorTextColor: for changing error text color
  - smsp_multilineError: Default is false. Update it to show as single line or multiple line
- Empty dropdown clickable
- Color customization to:
  - Item text color (smsp_itemColor)
  - Hint color (smsp_hintColor)
  - Floating Label color (smsp_floatingLabelColor)
  - Error Text color (smsp_errorTextColor)
  - List Item Hint color and background (smsp_itemListHintColor && smsp_itemListHintBackgroundColor)
  - List Item color (smsp_itemListColor)
  - Selected item color (smsp_selectedItemListColor)
  - Arrow or selector color (smsp_arrowColor)
  - Underline color (smsp_underlineColor)

## Screenshot Demo

![](https://github.com/Chivorns/SmartMaterialSpinner/blob/master/resources/src/main/assets/1.smsp_no_select.png)
![](https://github.com/Chivorns/SmartMaterialSpinner/blob/master/resources/src/main/assets/2.smsp_dropdown.png)
![](https://github.com/Chivorns/SmartMaterialSpinner/blob/master/resources/src/main/assets/3.smsp_selected.png)

## Usage
### Add the dependencies to your gradle file:

```gradle
dependencies {
    implementation 'com.github.chivorns:smartmaterialspinner:1.0.7'
}
```

### Add SmartMaterialSpinner to your layout:

```xml
    <com.chivorn.smartmaterialspinner.SmartMaterialSpinner
        android:id="@+id/spinner1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:smsp_errorText="This is error text. You can show it as single line or multiple lines using attr smsp_multilineError"
        app:smsp_floatingLabelColor="#1976D2"
        app:smsp_floatingLabelText="Floating Label Text"
        app:smsp_hint="Hint Text"
        app:smsp_hintColor="#388E3C"
        app:smsp_itemColor="#512DA8"
        app:smsp_itemListColor="#7C4DFF"
        app:smsp_itemListHintBackgroundColor="#808080"
        app:smsp_itemListHintColor="#FFFFFF"
        app:smsp_multilineError="false"
        app:smsp_selectedItemListColor="#FF5252" />
```

## In Java class
```java
public class MainActivity extends AppCompatActivity {

    private SmartMaterialSpinner spProvince;
    private SmartMaterialSpinner spEmptyItem;
    private List<String> provinceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smart_material_spinner_sample_layout);

        initSpinner();
    }

    private void initSpinner() {
        spProvince = findViewById(R.id.sp_provinces);
        spEmptyItem = findViewById(R.id.sp_empty_item);
        provinceList = new ArrayList<>();

        provinceList.add("Kampong Thom");
        provinceList.add("Kampong Cham");
        provinceList.add("Kampong Chhnang");
        provinceList.add("Phnom Penh");
        provinceList.add("Kandal");
        provinceList.add("Kampot");

        spProvince.setItems(provinceList);

        spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(MainActivity.this, provinceList.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spEmptyItem.setShowEmptyDropdown(false);
        // Use this method instead of OnClicklistener() to handle touch even.
        spEmptyItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Toast.makeText(MainActivity.this, "Country spinner is clicked", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }
}
```

## In Kotlin class
```kotlin
class MainActivity : AppCompatActivity() {
    private var spProvince: SmartMaterialSpinner? = null
    private var spEmptyItem: SmartMaterialSpinner? = null
    private var provinceList: MutableList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.smart_material_spinner_sample_layout)

        initSpinner()
    }

    private fun initSpinner() {
        spProvince = findViewById(R.id.sp_provinces)
        spEmptyItem = findViewById(R.id.sp_empty_item)
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
        spEmptyItem!!.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                Toast.makeText(this@MainActivity, "Country spinner is clicked", Toast.LENGTH_SHORT).show()
            }
            false
        })
    }
}
```

