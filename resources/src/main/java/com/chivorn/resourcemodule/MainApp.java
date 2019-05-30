package com.chivorn.resourcemodule;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;

import java.util.List;

public class MainApp extends AppCompatActivity implements View.OnClickListener {
    private final String repoUrl = "https://github.com/Chivorns/SmartMaterialSpinner";

    protected SmartMaterialSpinner spProvince;
    protected SmartMaterialSpinner spProvinceDialog;
    protected SmartMaterialSpinner spCustomColor;
    protected SmartMaterialSpinner spEmptyItem;
    protected List<String> provinceList;

    private LinearLayout githubRepo;
    private Button githubRepoBtn;
    private Intent intent;

    public void initBaseView() {
        spProvince = findViewById(R.id.sp_provinces);
        spProvinceDialog = findViewById(R.id.sp_provinces_dialog);
        spCustomColor = findViewById(R.id.sp_custom_color);
        spEmptyItem = findViewById(R.id.sp_empty_item);
        githubRepo = findViewById(R.id.git_repo_container);
        githubRepoBtn = findViewById(R.id.github_repo_btn);

        setUnderlineColor(spProvince, spProvinceDialog, spCustomColor);
    }

    public void onGithubRepoClickListenter() {
        if (githubRepo != null && githubRepoBtn != null) {
            githubRepo.setOnClickListener(this);
            githubRepoBtn.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.git_repo_container || v.getId() == R.id.github_repo_btn) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(repoUrl));
            startActivity(intent);
        }
    }

    private void setUnderlineColor(SmartMaterialSpinner... spinners) {
        for (SmartMaterialSpinner spinner : spinners) {
            spinner.setUnderlineColor(ContextCompat.getColor(getApplicationContext(), R.color.underline_color));
        }
    }
}
