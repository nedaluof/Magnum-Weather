package com.nedaluof.magweath.ui.details;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.nedaluof.magweath.data.model.WeatherModel;
import com.nedaluof.magweath.databinding.ActivityDetailsBinding;

public class DetailsActivity extends AppCompatActivity {

    private ActivityDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imgBtnBack.setOnClickListener(v -> this.finish());

        Intent intent = getIntent();
        int fragmentType = getIntent().getIntExtra("details", 0);
        int itemPosition = getIntent().getIntExtra("position", 0);
        WeatherModel model = intent.getParcelableExtra("model");
        loadFragment(fragmentType, model, itemPosition);
    }

    private void loadFragment(int fragmentType, WeatherModel data, int position) {
        switch (fragmentType) {
            case 0:
                getSupportFragmentManager().beginTransaction()
                        .replace(binding.detailsFragmentContainer.getId(), new DayForecastDetailsFragment(data, position)).commit();
                break;
            case 1:
                getSupportFragmentManager().beginTransaction()
                        .replace(binding.detailsFragmentContainer.getId(), new WeekForecastDetailsFragment(data, position)).commit();
                break;
        }
    }

}
