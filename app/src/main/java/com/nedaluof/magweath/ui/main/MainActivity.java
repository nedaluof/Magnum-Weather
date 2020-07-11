package com.nedaluof.magweath.ui.main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.nedaluof.magweath.R;
import com.nedaluof.magweath.data.model.MagForecastData;
import com.nedaluof.magweath.data.model.WeatherModel;
import com.nedaluof.magweath.databinding.ActivityMainBinding;
import com.nedaluof.magweath.ui.base.BaseActivity;
import com.nedaluof.magweath.ui.details.DetailsActivity;
import com.nedaluof.magweath.ui.settings.SettingsActivity;
import com.nedaluof.magweath.util.Const;
import com.nedaluof.magweath.util.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import static com.nedaluof.magweath.util.Const.LOCATION_REQUEST_DISPLACEMENT;
import static com.nedaluof.magweath.util.Const.LOCATION_REQUEST_INTERVAL;

public class MainActivity extends BaseActivity implements MainActivityView {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;

    private ForecastAdapter adapterCurrentDayForecast;
    private ForecastAdapter adapterCurrentWeekForecast;
    private WeatherModel globalModel;

    @Inject
    MainActivityPresenter presenter;

    //location references
    private FusedLocationProviderClient fusedLocationClient;
    private SettingsClient settingsClient;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private LocationCallback locationCallback;
    private Location location;
    private boolean checkPermissionsGranted = false;
    private boolean requestingLocationUpdates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activityComponent().inject(this);
        presenter.attachView(this);
        showProgress();
        setupPermission();
        onCreateLocationHelper();
        setupRecyclerViewCurrentDayForecast();
        setupRecyclerViewCurrentWeekForecast();
        presenter.loadWeatherDataWithLocation(location);
        setupSwipeRefresh();
    }

    private void setupPermission() {
        new Thread(() -> Dexter.withContext(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        checkPermissionsGranted = true;
                        startUpdatesHandler();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            Utility.showSettingsDialog(MainActivity.this);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).withErrorListener(error -> Toast.makeText(getApplicationContext(), getString(R.string.permission_error) + error.toString(), Toast.LENGTH_SHORT).show()).check()).start();
    }

    private void showProgress() {
        binding.currentLayout.setVisibility(View.GONE);
        binding.cdMainInfo.setVisibility(View.GONE);
        binding.progressCd1.setVisibility(View.VISIBLE);
        binding.progressCd2.setVisibility(View.VISIBLE);
        binding.progressCd3.setVisibility(View.VISIBLE);
        binding.progressCd4.setVisibility(View.VISIBLE);
    }


    private void setupSwipeRefresh() {
        binding.swipeRefresh.setColorSchemeColors(getColor(R.color.blue));
        binding.swipeRefresh.setOnRefreshListener(() -> {
            startLocationUpdates();
            presenter.loadWeatherDataWithLocation(location);
            setupDateTime(0);
            binding.swipeRefresh.setRefreshing(false);
        });
    }


    public void setupRecyclerViewCurrentDayForecast() {
        binding.recyclerCurrentDayForecast.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerCurrentDayForecast.setHasFixedSize(true);
        binding.recyclerCurrentDayForecast.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerCurrentDayForecast.setNestedScrollingEnabled(true);
        adapterCurrentDayForecast = new ForecastAdapter(MainActivity.this, 0, this);
        binding.recyclerCurrentDayForecast.setAdapter(adapterCurrentDayForecast);
        binding.recyclerCurrentDayForecast.setVisibility(View.GONE);

    }

    public void setupRecyclerViewCurrentWeekForecast() {
        binding.recyclerCurrentWeekForecast.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        binding.recyclerCurrentWeekForecast.setHasFixedSize(true);
        binding.recyclerCurrentWeekForecast.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerCurrentWeekForecast.setNestedScrollingEnabled(true);
        adapterCurrentWeekForecast = new ForecastAdapter(MainActivity.this, 1, this);
        binding.recyclerCurrentWeekForecast.setAdapter(adapterCurrentWeekForecast);
        binding.recyclerCurrentWeekForecast.setVisibility(View.GONE);
    }

    private void setupDateTime(int type) {
        if (type == 0) {
            binding.hintToSwipe.setText(getString(R.string.hint_to_swipe).concat(Utility.getDateTimeNow()));
        } else {
            binding.hintToSwipe.setText(getString(R.string.hint_to_swipe_current_location).concat(Utility.getDateTimeNow()));
        }
    }

    @Override
    public void showCurrentDay(WeatherModel model) {
        binding.tvTemperature.setText(String.valueOf(Math.round(model.getCurrent().getTemp())));
        binding.tvDescription.setText(model.getCurrent().getWeather().get(0).getDescription());
        binding.tvCityName.setText(Utility.getCityName(model.getTimezone()));
        Utility.loadImage(MainActivity.this, Utility.formatImagePath(model.getCurrent().getWeather().get(0).getIcon()))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.d("MainActivity", "onLoadFailed: ");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d("MainActivity", "onResourceReady: ");
                        return false;
                    }
                })
                .into(binding.imgStatus);
        binding.cdMainSunrise.setText(Utility.getDateInstant(model.getCurrent().getSunrise()).substring(11, 16).concat(getString(R.string.sunrise_uni)));
        binding.cdMainSunset.setText(Utility.getDateInstant(model.getCurrent().getSunset()).substring(11, 16).concat(getString(R.string.sunset_uni)));
        binding.cdMainFeelsLike.setText(String.valueOf((int) Math.round(model.getCurrent().getFeelsLike())).concat(getString(R.string.deg_sign)));
        binding.cdMainHumidity.setText(String.valueOf(model.getCurrent().getHumidity()));
        binding.cdMainWindSpeed.setText(String.valueOf(model.getCurrent().getWindSpeed()));
        binding.cdMainWindDeg.setText(String.valueOf(model.getCurrent().getWindDeg()).concat(getString(R.string.deg_sign)));
        SimpleDateFormat.getDateTimeInstance().format(new Date());
    }

    @Override
    public void showErrorCurrentDay(String message) {
        Toast.makeText(this, "ERROR IN FETCHING API RESPONSE. Try again",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void showEmptyCurrentDay() {

    }

    @Override
    public void hideLoadingCurrentDay() {
        binding.progressCd1.setVisibility(View.GONE);
        binding.progressCd4.setVisibility(View.GONE);
        binding.currentLayout.setVisibility(View.VISIBLE);
        binding.cdMainInfo.setVisibility(View.VISIBLE);

    }


    @Override
    public void showCurrentDayForecast(WeatherModel model) {
        adapterCurrentDayForecast.clear();
        globalModel = model;
        Log.d(TAG, "showCurrentDayForecast: " + globalModel.getHourly().get(0).getTemp());
        List<MagForecastData> data = new ArrayList<>();
        for (int i = 1; i <= 12/*model.getHourly().size()*/; i++) {
            data.add(new MagForecastData(
                    (int) Math.round(model.getHourly().get(i).getTemp()),
                    Utility.getDateInstant(model.getHourly().get(i).getDt()),
                    model.getHourly().get(i).getWeather().get(0).getIcon()
            ));
        }
        adapterCurrentDayForecast.addAll(data);
    }

    @Override
    public void showErrorCurrentDayForecast(String message) {
        Toast.makeText(this, "ERROR IN FETCHING API RESPONSE. Try again",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void showEmptyCurrentDayForecast() {

    }

    @Override
    public void hideLoadingCurrentDayForecast() {
        binding.progressCd2.setVisibility(View.GONE);
        binding.recyclerCurrentDayForecast.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDayClickListener(int position) {
        startActivity(
                new Intent(this, DetailsActivity.class)
                        .putExtra("details", 0)
                        .putExtra("position", position)
                        .putExtra("model", globalModel));
    }


    @Override
    public void showCurrentWeekForecast(WeatherModel model) {
        adapterCurrentWeekForecast.clear();
        Log.d(TAG, "onResponse: ");
        //start from 1 to skip the current day
        for (int i = 1; i < model.getDaily().size(); i++) {
            adapterCurrentWeekForecast.add(new MagForecastData(
                    (int) Math.round(model.getDaily().get(i).getTemp().getDay()),
                    Utility.getDateInstant(model.getDaily().get(i).getDt()),
                    model.getDaily().get(i).getWeather().get(0).getIcon()
            ));
        }
    }

    @Override
    public void showErrorCurrentWeekForecast(String message) {
        Toast.makeText(this, "ERROR IN FETCHING API RESPONSE. Try again",
                Toast.LENGTH_LONG).show();
        Toast.makeText(this, message,
                Toast.LENGTH_LONG).show();
        Log.d(TAG, "showErrorCurrentWeekForecast: " + message);
    }

    @Override
    public void showEmptyCurrentWeekForecast() {

    }

    @Override
    public void hideLoadingCurrentWeekForecast() {
        binding.progressCd3.setVisibility(View.GONE);
        binding.recyclerCurrentWeekForecast.setVisibility(View.VISIBLE);
    }

    @Override
    public void onWeekClickListener(int position) {
        startActivity(
                new Intent(this, DetailsActivity.class)
                        .putExtra("details", 1)
                        .putExtra("position", position)
                        .putExtra("model", globalModel));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }


    //Location Work Here

    private void onCreateLocationHelper() {
        requestingLocationUpdates = false;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        settingsClient = LocationServices.getSettingsClient(this);
        createLocationRequest();
        buildLocationSettingsRequest();
        createLocationCallback();
    }


    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(LOCATION_REQUEST_INTERVAL);
        locationRequest.setFastestInterval(LOCATION_REQUEST_INTERVAL / 2);
        locationRequest.setSmallestDisplacement(LOCATION_REQUEST_DISPLACEMENT);
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        locationSettingsRequest = builder.build();
    }

    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult == null) {
                    Log.d(TAG, "onLocationResult: null");
                    return;
                }
                // TODO: 7/9/2020  make strategy to run via location OR Prefernces Or from selected city
                location = locationResult.getLastLocation();
                //presenter.loadWeatherDataWithLocation(location);
                Log.d(TAG, location.toString());
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check for the integer request code originally supplied to startResolutionForResult().
        if (requestCode == Const.REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Log.i(TAG, "User agreed to make required location settings changes.");
                    // Nothing to do. startLocationupdates() gets called in onResume again.
                    break;
                case Activity.RESULT_CANCELED:
                    Log.i(TAG, "User chose not to make required location settings changes.");
                    //should show dialogue to user till him about the location to gain this feature
                    requestingLocationUpdates = false;
                    break;
            }
        }
    }

    public void startUpdatesHandler() {
        if (!requestingLocationUpdates) {
            requestingLocationUpdates = true;
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        settingsClient.checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(this, locationSettingsResponse -> {
                    Log.i(TAG, "All location settings are satisfied.");
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                })
                .addOnFailureListener(this, e -> {
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                    "location settings ");
                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the
                                // result in onActivityResult().
                                ResolvableApiException rae = (ResolvableApiException) e;
                                rae.startResolutionForResult(this, Const.REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sie) {
                                Log.i(TAG, "PendingIntent unable to execute request.");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            String errorMessage = "Location settings are inadequate, and cannot be " +
                                    "fixed here. Fix in Settings.";
                            Log.e(TAG, errorMessage);
                            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                            requestingLocationUpdates = false;
                    }
                });
    }


    private void stopLocationUpdates() {
        if (!requestingLocationUpdates) {
            Log.d(TAG, "stopLocationUpdates: updates never requested, no-op.");
            return;
        }
        fusedLocationClient.removeLocationUpdates(locationCallback)
                .addOnCompleteListener(this, task -> requestingLocationUpdates = false);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissionsGranted) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onBackPressed() {

    }
}
