package com.chivorn.resourcemodule;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;

import java.util.List;

public class MainApp extends AppCompatActivity implements View.OnClickListener {
    private final String repoUrl = "https://github.com/Chivorns/SmartMaterialSpinner";

    protected SmartMaterialSpinner spProvince;
    protected SmartMaterialSpinner spCountry;
    protected List<String> provinceList;

    private LinearLayout githubRepo;
    private Button githubRepoBtn;
    private Intent intent;

    public void initBaseView() {
        spProvince = findViewById(R.id.sp_provinces);
        spCountry = findViewById(R.id.sp_countries);
        githubRepo = findViewById(R.id.git_repo_container);
        githubRepoBtn = findViewById(R.id.github_repo_btn);

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
}
