package com.nedaluof.magweath.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.nedaluof.magweath.BuildConfig;
import com.nedaluof.magweath.databinding.ActivitySplashBinding;
import com.nedaluof.magweath.ui.main.MainActivity;

import yanzhikai.textpath.painter.FireworksPainter;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySplashBinding binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.appNameAnimated.setPathPainter(new FireworksPainter());
        binding.appNameAnimated.startAnimation(0, 1);
        binding.appNameAnimated.setFillColor(true);
        String versionName = BuildConfig.VERSION_NAME;
        binding.tvVersion.setText("v ".concat(versionName));

        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            this.finish();
        }, 2100);
    }
}
