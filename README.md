# SmartMaterialSpinner
[ ![Download](https://api.bintray.com/packages/chivorn/maven/smartmaterialspinner/images/download.svg) ](https://bintray.com/chivorn/maven/smartmaterialspinner/_latestVersion)

The best Android spinner library for your android application

## Available on Play Store
<a href="https://play.google.com/store/apps/details?id=com.chivorn.smsp.demojava" target="_blank">
  <img src="https://github.com/Chivorns/Resources/blob/master/google-play-badge.png" alt="Play Store" width="200" ">
</a>

## Screenshot Demo

![](https://github.com/Chivorns/SmartMaterialSpinner/blob/master/resources/src/main/assets/1.smsp_no_select.png)
![](https://github.com/Chivorns/SmartMaterialSpinner/blob/master/resources/src/main/assets/2.smsp_dropdown.png)
![](https://github.com/Chivorns/SmartMaterialSpinner/blob/master/resources/src/main/assets/3.smsp_selected.png)

## Usage
### Add the dependencies to your gradle file:

```gradle
dependencies {
    implementation 'com.github.chivorns:smartmaterialspinner:1.0.2'
}
```

### Add SmartMaterialSpinner to your layout:

```xml
<com.chivorn.smartmaterialspinner.SmartMaterialSpinner
    android:id="@+id/sp_provinces"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:smsp_baseColor="@color/base_color"
    app:smsp_floatingLabelColor="@color/floating_color"
    app:smsp_highlightColor="@color/colorPrimary"
    app:smsp_hint="Province"
    app:smsp_hintColor="@color/hint_color"
    app:smsp_hintTextSize="14sp" 
    app:smsp_showEmptyDropdown="true"/>
```

## In Java class
```java
public class MainActivity extends AppCompatActivity {

    private SmartMaterialSpinner spProvince;
    private SmartMaterialSpinner spCountry;
    private List<String> provinceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smart_material_spinner_sample_layout);

        initSpinner();
    }

    private void initSpinner() {
        spProvince = findViewById(R.id.sp_provinces);
        spCountry = findViewById(R.id.sp_countries);
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

        spCountry.setShowEmptyDropdown(false);
        // Use this method instead of OnClicklistener() to handle touch even.
        spCountry.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Toast.makeText(MainActivity.this, "Country spinner is clicked", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }
}
```

## In Kotlin class
```kotlin
class MainActivity : AppCompatActivity() {
    private var spProvince: SmartMaterialSpinner? = null
    private var spCountry: SmartMaterialSpinner? = null
    private var provinceList: MutableList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.smart_material_spinner_sample_layout)

        initSpinner()
    }

    private fun initSpinner() {
        spProvince = findViewById(R.id.sp_provinces)
        spCountry = findViewById(R.id.sp_countries)
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

        spCountry!!.setShowEmptyDropdown(false)
        // Use this method instead of OnClicklistener() to handle touch even.
        spCountry!!.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                Toast.makeText(this@MainActivity, "Country spinner is clicked", Toast.LENGTH_SHORT).show()
            }
            true
        })
    }
}
```

