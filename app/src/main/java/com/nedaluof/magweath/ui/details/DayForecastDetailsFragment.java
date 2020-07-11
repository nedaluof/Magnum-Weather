package com.nedaluof.magweath.ui.details;

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
import com.nedaluof.magweath.R;
import com.nedaluof.magweath.data.model.WeatherModel;
import com.nedaluof.magweath.databinding.DayForecastDetailsFragmentBinding;
import com.nedaluof.magweath.util.Utility;

public class DayForecastDetailsFragment extends Fragment {
    //Todo using the presenter load data from details Activity
    //Todo to this fragment
    private WeatherModel model;
    private int position;//item position from the recycler view

    public DayForecastDetailsFragment(WeatherModel data, int position) {
        this.model = data;
        this.position = position + 1;
    }


    private DayForecastDetailsFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DayForecastDetailsFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadDataToViews();
    }

    private void loadDataToViews() {
        binding.tvCityName.setText(Utility.getCityName(model.getTimezone()));
        binding.tvTemperature.setText(String.valueOf(Math.round(model.getHourly().get(position).getTemp())));
        Utility.loadImage(getActivity(), Utility.formatImagePath(model.getHourly().get(position).getWeather().get(0).getIcon()))
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
        binding.tvDate.setText(getActivity().getString(R.string.fragment_date).concat(Utility.getDateInstant(model.getHourly().get(position).getDt()).substring(0, 10)));
        binding.tvTime.setText(getActivity().getString(R.string.fragment_time).concat(Utility.getDateInstant(model.getHourly().get(position).getDt()).substring(11, 16)));
        binding.tvFeelsLikeTemp.setText(String.valueOf(Math.round(model.getHourly().get(position).getFeelsLike())));
        binding.tvHumidityTemp.setText(String.valueOf(model.getHourly().get(position).getHumidity()));
        binding.tvPressureTemp.setText(String.valueOf(model.getHourly().get(position).getPressure()));
        binding.tvWindSpeedTemp.setText(String.valueOf(model.getHourly().get(position).getWindSpeed()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
