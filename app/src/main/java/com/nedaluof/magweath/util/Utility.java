package com.nedaluof.magweath.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;

import android.provider.Settings;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.nedaluof.magweath.R;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class Utility {

    public static String formatImagePath(String icon) {
        return Const.Base_URL_IMG + icon + "@2x.png";
    }

    public static String getDateInstant(long timeStamp) {
        return Instant.ofEpochSecond(timeStamp).toString();
    }

    public static String getCityName(String city) {
        return city.substring(city.lastIndexOf("/") + 1);
    }

    public static RequestBuilder<Drawable> loadImage(Context context, @NonNull String path) {
        return Glide.with(context)
                .load(path)
                .centerCrop();
    }

    public static String formatDayName(String date) {
        Calendar c = Calendar.getInstance();
        String year = date.substring(0, 4);
        String month = date.substring(5, 7);
        String day = date.substring(8, 10);
        c.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
        String dayOfWeek = getDayOfWeek(c.get(Calendar.DAY_OF_WEEK));
        return dayOfWeek.toUpperCase();
    }

    private static String getDayOfWeek(int value) {
        String day = "";
        switch (value) {
            case 1:
                day = "Sunday";
                break;
            case 2:
                day = "Monday";
                break;
            case 3:
                day = "Tuesday";
                break;
            case 4:
                day = "Wednesday";
                break;
            case 5:
                day = "Thursday";
                break;
            case 6:
                day = "Friday";
                break;
            case 7:
                day = "Saturday";
                break;
        }
        return day;
    }


    public static void showSettingsDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.dialogue_title));
        builder.setMessage(context.getResources().getString(R.string.dialogue_description));
        builder.setPositiveButton(context.getResources().getString(R.string.dialogue_positive_btn), (dialog, which) -> {
            dialog.cancel();
            openSettings(context);
        });
        builder.setNegativeButton(context.getResources().getString(R.string.dialogue_negative_btn), (dialog, which) -> dialog.cancel());
        builder.show();
    }

    /**
     * @param context identify package to navigate the user to app settings
     */
    private static void openSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }

    public static void isLocationEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = Objects.requireNonNull(lm).isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            network_enabled = Objects.requireNonNull(lm).isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gps_enabled && !network_enabled) {
            // notify user
            new AlertDialog.Builder(context)
                    .setMessage("Please turn on Location to continue")
                    .setPositiveButton("Open Location Settings", (paramDialogInterface, paramInt) ->
                            context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))).
                    setNegativeButton("Cancel", null)
                    .show();
        }
    }


    public static String getDateTimeNow() {
        return LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 5);
    }


    /**
     * @return user device language
     */
    @NotNull
    public static String getLanguage() {
        return Locale.getDefault().getDisplayLanguage().equals("العربية") ? "ar" : "en";
    }
}


