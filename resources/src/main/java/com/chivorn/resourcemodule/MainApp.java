package com.chivorn.resourcemodule;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainApp extends AppCompatActivity implements View.OnClickListener {
    private final String repoUrl = "https://github.com/Chivorns/SmartMaterialSpinner";

    protected SmartMaterialSpinner spProvince;
    protected SmartMaterialSpinner spProvinceDialog;
    protected SmartMaterialSpinner spCustomColor;
    protected SmartMaterialSpinner spEmptyItem;
    protected SmartMaterialSpinner spSearchable;
    protected List<Object> provinceList;

    private LinearLayout githubRepo;
    private Button btnShowError;
    private Button githubRepoBtn;
    private Intent intent;

    public void initBaseView() {
        spProvince = findViewById(R.id.sp_provinces);
        spProvinceDialog = findViewById(R.id.sp_provinces_dialog);
        spCustomColor = findViewById(R.id.sp_custom_color);
        spEmptyItem = findViewById(R.id.sp_empty_item);
        spSearchable = findViewById(R.id.sp_searchable);
        githubRepo = findViewById(R.id.git_repo_container);
        btnShowError = findViewById(R.id.btn_show_error);
        githubRepoBtn = findViewById(R.id.github_repo_btn);
    }

    public void initItemList() {
        provinceList = new ArrayList<>();
        provinceList.add("Banteay Meanchey");
        provinceList.add("Battambang");
        provinceList.add("Kampong Cham");
        provinceList.add("Kampong Chhnang");
        provinceList.add("Kampong Speu");
        provinceList.add("Kampong Thom");
        provinceList.add("Kampot");
        provinceList.add("Kandal");
        provinceList.add("Kep");
        provinceList.add("Koh Kong");
        provinceList.add("Kratie");
        provinceList.add("Mondulkiri");
        provinceList.add("Oddar Meanchey");
        provinceList.add("Pailin");
        provinceList.add("Phnom Penh");
        provinceList.add("Preah Vihear");
        provinceList.add("Prey Veng");
        provinceList.add("Pursat");
        provinceList.add("Ratanakiri");
        provinceList.add("Siem Reap");
        provinceList.add("Sihanoukville");
        provinceList.add("Stung Treng");
        provinceList.add("Svay Rieng");
        provinceList.add("Takeo");
        provinceList.add("Tbong Khmum");

        provinceList.add(111);
        provinceList.add(222);
        provinceList.add(333);
        provinceList.add(444);
        provinceList.add(555.555);

        provinceList.add(true);

        Map<String, String> map = new HashMap<>();
        map.put("A", "This is value of A");
        map.put("B", "This is value of B");
        map.put("C", "This is value of C");

        provinceList.add(map);
    }

    public void onClickListener() {
        if (githubRepo != null && githubRepoBtn != null) {
            githubRepo.setOnClickListener(this);
            githubRepoBtn.setOnClickListener(this);
        }

        if (btnShowError != null) {
            btnShowError.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.git_repo_container || v.getId() == R.id.github_repo_btn) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(repoUrl));
            startActivity(intent);
        } else if (v.getId() == R.id.btn_show_error) {
            spProvince.setErrorText(getResources().getString(R.string.sample_error_message));
            spProvinceDialog.setErrorText(getResources().getString(R.string.sample_error_message));
            spCustomColor.setErrorText(getResources().getString(R.string.sample_error_message));
            spSearchable.setErrorText(getResources().getString(R.string.sample_error_message));
        }
    }
}
