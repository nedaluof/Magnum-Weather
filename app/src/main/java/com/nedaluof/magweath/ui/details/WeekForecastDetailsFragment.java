package com.nedaluof.magweath.ui.details;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.nedaluof.magweath.data.model.WeatherModel;
import com.nedaluof.magweath.databinding.WeekForecastDetailsFragmentBinding;
import com.nedaluof.magweath.util.Utility;

import static java.lang.Math.round;

public class WeekForecastDetailsFragment extends Fragment {
    // TODO: 7/9/2020 publish data using presenter
    private final WeatherModel model;
    private final int position;

    public WeekForecastDetailsFragment(WeatherModel model, int position) {
        this.model = model;
        this.position = position + 1;
    }

    private WeekForecastDetailsFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = WeekForecastDetailsFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadDataToViews();
    }

    @SuppressLint("SetTextI18n")
    private void loadDataToViews() {
        binding.tvCityName.setText(Utility.getCityName(model.getTimezone()));
        binding.tvTemperature.setText(String.valueOf(round(model.getDaily().get(position).getTemp().getDay())));
        Utility.loadImage(getActivity(), Utility.formatImagePath(model.getDaily().get(position).getWeather().get(0).getIcon()))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        //todo hide progress in future
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        //todo hide progress in future
                        return false;
                    }
                }).into(binding.imgWeathStatus);
        binding.tvDescription.setText(model.getHourly().get(position).getWeather().get(0).getDescription());
        binding.tvDayName.setText(Utility.formatDayName(Utility.getDateInstant(model.getDaily().get(position).getDt())));
        binding.tvSunrise.setText("Sunrise : " + Utility.getDateInstant(model.getDaily().get(position).getSunrise()).substring(11, 16));
        binding.tvSunset.setText("Sunset : " + Utility.getDateInstant(model.getDaily().get(position).getSunset()).substring(11, 16));
        binding.tvMinTemp.setText("Min : " + round(model.getDaily().get(position).getTemp().getMin()));
        binding.tvMaxTemp.setText("Max : " + round(model.getDaily().get(position).getTemp().getMax()));
        binding.tvDate.setText("Date : " + Utility.getDateInstant(model.getDaily().get(position).getDt()).substring(0, 10));
        binding.tvTime.setText("Time : " + Utility.getDateInstant(model.getDaily().get(position).getDt()).substring(11, 16));
        binding.tvFeelsLikeTemp.setText(String.valueOf(round(model.getDaily().get(position).getFeelsLike().getDay())));
        binding.tvHumidityTemp.setText(String.valueOf(model.getDaily().get(position).getHumidity()));
        binding.tvPressureTemp.setText(String.valueOf(model.getDaily().get(position).getPressure()));
        binding.tvWindSpeedTemp.setText(String.valueOf(model.getDaily().get(position).getWindSpeed()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
