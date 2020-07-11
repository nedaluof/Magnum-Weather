package com.nedaluof.magweath.ui.main;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.nedaluof.magweath.R;
import com.nedaluof.magweath.data.model.MagForecastData;
import com.nedaluof.magweath.databinding.ItemForecastDailyBinding;
import com.nedaluof.magweath.databinding.ItemForecastWeekBinding;
import com.nedaluof.magweath.util.Utility;

import java.util.ArrayList;
import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MagForecastData> list = new ArrayList<>();
    private Context context;
    private int forecastType;
    private MainActivityView onItemClick;

    public ForecastAdapter(Context context, int forecastType, MainActivityView onItemClick) {
        this.context = context;
        this.forecastType = forecastType;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (forecastType) {
            case 0:
                View view = inflater.inflate(R.layout.item_forecast_daily, parent, false);
                holder = new ForecastDailyVewHolder(view);
                break;
            case 1:
                View view2 = inflater.inflate(R.layout.item_forecast_week, parent, false);
                holder = new ForecastWeekVewHolder(view2);
                break;
        }
        assert holder != null;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (forecastType) {
            case 0:
                ForecastDailyVewHolder dailyHolder = (ForecastDailyVewHolder) holder;
                dailyHolder.binding.tvTemp.setText(String.valueOf(list.get(position).getTemp()));
                dailyHolder.binding.tvHour.setText(list.get(position).getDayName().substring(11, 16));
                Utility.loadImage(context, Utility.formatImagePath(list.get(position).getImgPath()))
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        }).into(dailyHolder.binding.imgWeather);
                dailyHolder.itemView.setOnClickListener(v -> onItemClick.onDayClickListener(position));
                break;
            case 1:
                ForecastWeekVewHolder weekViewHolder = (ForecastWeekVewHolder) holder;
                weekViewHolder.binding.tvTemp.setText(String.valueOf(list.get(position).getTemp()));
                weekViewHolder.binding.tvDayName.setText(Utility.formatDayName(list.get(position).getDayName()));
                Utility.loadImage(context, Utility.formatImagePath(list.get(position).getImgPath()))
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        }).into(weekViewHolder.binding.imgWeather);
                weekViewHolder.itemView.setOnClickListener(v -> onItemClick.onWeekClickListener(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void add(MagForecastData moviesResult) {
        list.add(moviesResult);
        //notifyItemInserted(list.size() - 1);
        notifyDataSetChanged();
    }

    public void addAll(List<MagForecastData> movieResults) {
        for (MagForecastData movie : movieResults) {
            add(movie);
        }
    }

    public void clear() {
        int size = list.size();
        if (size > 0) {
            list.subList(0, size).clear();
            notifyItemRangeRemoved(0, size);
        }
    }


    static class ForecastDailyVewHolder extends RecyclerView.ViewHolder {
        ItemForecastDailyBinding binding;
        ForecastDailyVewHolder(View view) {
            super(view);
            binding = ItemForecastDailyBinding.bind(view);
        }
    }

    static class ForecastWeekVewHolder extends RecyclerView.ViewHolder {
        ItemForecastWeekBinding binding;
        ForecastWeekVewHolder(View view) {
            super(view);
            binding = ItemForecastWeekBinding.bind(view);
        }
    }

}
