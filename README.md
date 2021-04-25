# SmartMaterialSpinner 
The best Android spinner library for your android application with more customization
## Current version
![current](https://api.bintray.com/packages/chivorn/maven/smartmaterialspinner/images/download.svg) 


## Available on Play Store
<a href="https://play.google.com/store/apps/details?id=com.chivorn.smsp.demojava" target="_blank">
  <img src="https://github.com/Chivorns/Resources/blob/master/google-play-badge.png" alt="Play Store" width="140" ">
</a>
                                                                                                                   
## Available features
### Spinner mode
  * Dropdown mode ( android:spinnerMode="dropdown" ). By default it is dropdown mode
  * Dialog mode ( android:spinnerMode="dialog" )
  * Searchable mode ( app:smsp_isSearchable="true" )
### Searchable spinner
  * `smsp_isSearchable` : Default is false. Set it to true to enable this feature.
  * `smsp_enableSearchHeader`: Default is true. Set it to false to hide search header.
  * `smsp_searchHeaderText`: Text display as header text
  * `smsp_searchHeaderTextColor`: For changing header text color
  * `smsp_searchHeaderBackgroundColor`: For changing header background color
  * `smsp_searchHint`: To set query text hint
  * `smsp_searchHintColor`: To set hint color to query search text
  * `smsp_searchTextColor`: To set color to query search text
  * `smsp_searchBackgroundColor`: To set background color to searchview
  * `smsp_itemListBackgroundColor`: To set background color to search item list
  * `smsp_enableDismissSearch`: To enable/disable dismiss button on search dialog
  * `smsp_dismissSearchText`: To set dismiss button text for search dialog
  * `smsp_dismissSearchColor`: To set dismiss button text color for search dialog
  * `smsp_searchDropdownView`: To custom search view item

### Spinner item
  * `smsp_itemSize`: To set spinner item size
  * `smsp_itemColor`: To set spinner item color

### Hint Label
  * `smsp_hint`: To set spinner hint text
  * `smsp_hintSize`:  To set spinner hint text size
  * `smsp_hintColor`:  To set spinner hint color
  * `smsp_itemListHintColor`:  To set spinner hint text color (dropdown hint)
  * `smsp_itemListHintBackgroundColor`: To set spinner hint background (dropdown hint)


### Floating Label
  * `smsp_enableFloatingLabel`: Default is true. Set it to false to disable floating label.
  * `smsp_alwaysShowFloatingLabel`: Default is false. Set it to true for always display.
  * `smsp_floatingLabelText`: Update it to what text you want. If the value is not set, it get from hint. 
  * `smsp_floatingLabelSize`: For changing floating label size
  * `smsp_floatingLabelColor`: For changing floating label color
### Error Label
  * `smsp_enableErrorLabel`: Default it is true.
  * `smsp_errorText`: Your error message text.
  * `smsp_errorTextSize`: For changing error text size
  * `smsp_errorTextColor`: for changing error text color
  * `smsp_multilineError`: Default is false. Update it to show as single line or multiple line
  * `smsp_errorTextAlignment`: Align error text to left, center or right
### Color customization to:
  * Item text color (`smsp_itemColor`)
  * Hint color (`smsp_hintColor`)
  * Floating Label color (`smsp_floatingLabelColor`)
  * Error Text color (`smsp_errorTextColor`)
  * List Item Hint color and background (`smsp_itemListHintColor` & `smsp_itemListHintBackgroundColor`)
  * List Item color (`smsp_itemListColor`)
  * Selected item color (`smsp_selectedItemListColor`)
  * Arrow or selector color (`smsp_arrowColor`)
  * Underline color (`smsp_underlineColor`)
  * Dropdown list/Search ListView background (`smsp_itemListBackgroundColor`)
  * Search dialog dismiss button color (`smsp_dismissSearchColor`)
### Empty dropdown clickable
  * In case if you want to design view with empty spinner and apply click event and here it is.
### Underline
  * `smsp_underlineSize`: To set underline size (The bottom line of spinner)
  * `smsp_underlineColor`: To set underline color (The bottom line of spinner)
### Re-selectable feature
  * `smsp_isReSelectable`: Allow re-selectable for current selected item. By default it is false
### RTL (Right To Left) feature
  * `smsp_isRtl`: To change position of view from right to left or left to right. I will update more on it.
### Typeface (Custom font)
  * `smsp_typeface`: Apply custom font to whole spinner view. You can use this attr without specify font extension
  * `smsp_typeface_xxx`: Set typeface to specific part of spinner

## Screenshot Demo

<table border="0">
 <tr>
    <td> 
        <a target="_blank">
            <img src="https://github.com/Chivorns/Resources/blob/master/smsp/smsp_home.jpg" alt="SmartMaterialSpinner" width="250" >
        </a>
    </td>
   <td>
        <a target="_blank">
            <img src="https://github.com/Chivorns/Resources/blob/master/smsp/search_1.jpg" alt="SmartMaterialSpinner" width="250">
        </a>
    </td>
    <td>
        <a target="_blank">
            <img src="https://github.com/Chivorns/Resources/blob/master/smsp/smsp_dropdown.jpg" alt="SmartMaterialSpinner" width="250">
        </a>
    </td>
 </tr>
</table>

<table border="0">

 <tr>
    <td> 
         <a target="_blank">
            <img src="https://github.com/Chivorns/Resources/blob/master/smsp/smsp_dropdown_no_hint.JPEG" alt="SmartMaterialSpinner" width="250">
        </a>
    </td>
    <td> 
        <a target="_blank">
            <img src="https://github.com/Chivorns/Resources/blob/master/smsp/smsp_dialog.jpg" alt="SmartMaterialSpinner" width="250">
        </a>
    </td>
   <td>
        <a target="_blank">
           <img src="https://github.com/Chivorns/Resources/blob/master/smsp/smsp_custom_color.jpg" alt="SmartMaterialSpinner" width="250">
        </a>
    </td>
 </tr>
</table>
                                                                                                                
## Usage
### Add the dependencies to your gradle file:

```gradle
dependencies {
    implementation 'com.github.chivorns:smartmaterialspinner:1.5.0'
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

        spProvince.setItem(provinceList);

        spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(MainActivity.this, provinceList.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
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

        spProvince!!.setItem<Any>(provinceList!!)

        spProvince!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@MainActivity, provinceList!![position], Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
    }
}
```

## Issue and new feature request
### Note
Each raised issue, should be provided with some information to be easy for me to replicate it like `Device Model`, `Android version` and `log message or file`
### Where to report issue and new feature request
- [Email](mailto:chivorn@live.com): chivorn@live.com
- [Github](https://github.com/Chivorns/SmartMaterialSpinner/issues)
- [Facebook Group](https://www.facebook.com/groups/837642023371127/)

You can create issue on Github to record issue history then to make it fast alert to me, you can send me email or post to Facebook group for any discussion

## Contribution
If you want to contribute in this library, please `fork` this repository and [create pull request](https://github.com/Chivorns/SmartMaterialSpinner/pulls).
