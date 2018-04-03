# SmartMaterialSpinner
The best Android spinner library for your android application
## Usage
### Add the dependencies to your gradle file:

```gradle
dependencies {
    implementation 'com.github.chivorns:smartmaterialspinner:1.0.0'
}
```

### Add SmartMaterialSpinner to your layout:

```xml
<com.chivorn.smartmaterialspinner.SmartMaterialSpinner
    android:id="@+id/sp_province"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:smsp_baseColor="#8f8989"
    app:smsp_floatingLabelColor="#665d5d"
    app:smsp_hint="Province"
    app:smsp_hintColor="#665d5d" />
```
