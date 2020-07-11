package com.nedaluof.magweath.data.PrefsManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.lang.ref.WeakReference;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferencesHelper {
    @Inject
    public PreferencesHelper() {
    }

    /**
     * Called to save supplied value in shared preferences against given key.
     *
     * @param context Context of caller activity
     * @param key     Key of value to save against
     * @param value   Value to save
     */
    public void saveToPrefs(Context context, String key, Object value) {
        WeakReference<Context> contextWeakReference = new WeakReference<>(context);
        if (contextWeakReference.get() != null) {
            SharedPreferences prefs =
                    android.preference.PreferenceManager.getDefaultSharedPreferences(contextWeakReference.get());
            final SharedPreferences.Editor editor = prefs.edit();
            if (value instanceof Integer) {
                editor.putInt(key, (Integer) value);
            } else if (value instanceof String) {
                editor.putString(key, value.toString());
            } else if (value instanceof Boolean) {
                editor.putBoolean(key, (Boolean) value);
            } else if (value instanceof Long) {
                editor.putLong(key, (Long) value);
            } else if (value instanceof Float) {
                editor.putFloat(key, (Float) value);
            } else if (value instanceof Double) {
                editor.putLong(key, Double.doubleToRawLongBits((double) value));
            }
            editor.apply();
        }
    }

    /**
     * Called to retrieve required value from shared preferences, identified by given key.
     * Default value will be returned of no value found or error occurred.
     *
     * @param context      Context of caller activity
     * @param key          Key to find value against
     * @param defaultValue Value to return if no data found against given key
     * @return Return the value found against given key, default if not found or any error occurs
     */
    public Object getFromPrefs(Context context, String key, Object defaultValue) {
        WeakReference<Context> contextWeakReference = new WeakReference<>(context);
        if (contextWeakReference.get() != null) {
            SharedPreferences sharedPrefs =
                    android.preference.PreferenceManager.getDefaultSharedPreferences(contextWeakReference.get());
            try {
                if (defaultValue instanceof String) {
                    return sharedPrefs.getString(key, defaultValue.toString());
                } else if (defaultValue instanceof Integer) {
                    return sharedPrefs.getInt(key, (Integer) defaultValue);
                } else if (defaultValue instanceof Boolean) {
                    return sharedPrefs.getBoolean(key, (Boolean) defaultValue);
                } else if (defaultValue instanceof Long) {
                    return sharedPrefs.getLong(key, (Long) defaultValue);
                } else if (defaultValue instanceof Float) {
                    return sharedPrefs.getFloat(key, (Float) defaultValue);
                } else if (defaultValue instanceof Double) {
                    return Double.longBitsToDouble(sharedPrefs.getLong(key, Double.doubleToLongBits((double) defaultValue)));
                }
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * @param context Context of caller activity
     * @param key     Key to delete from SharedPreferences
     */
    public void removeFromPrefs(Context context, String key) {
        WeakReference<Context> contextWeakReference = new WeakReference<>(context);
        if (contextWeakReference.get() != null) {
            SharedPreferences prefs =
                    android.preference.PreferenceManager.getDefaultSharedPreferences(contextWeakReference.get());
            final SharedPreferences.Editor editor = prefs.edit();
            editor.remove(key);
            editor.apply();
        }
    }

    public boolean hasKey(Context context, String key) {
        WeakReference<Context> contextWeakReference = new WeakReference<>(context);
        if (contextWeakReference.get() != null) {
            SharedPreferences prefs =
                    android.preference.PreferenceManager.getDefaultSharedPreferences(contextWeakReference.get());
            return prefs.contains(key);
        }
        return false;
    }

}
//Todo usages
/*
public class SampleActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        //Storing data in Preference

        PrefUtils.saveToPrefs(this,PrefKeys.USER_AGE,10);
        PrefUtils.saveToPrefs(this,PrefKeys.USER_MARITAL_STATUS,false);
        PrefUtils.saveToPrefs(this,PrefKeys.USER_SALARY_FLOAT,2.4);
        PrefUtils.saveToPrefs(this,PrefKeys.USER_SALARY_LONG,1287192837);
        PrefUtils.saveToPrefs(this,PrefKeys.USER_INCOME,2398742);

        //Getting data from preference
        int age = (Integer) PrefUtils.getFromPrefs(this, PrefKeys.USER_AGE, new Integer(10));
        boolean isMarried = (Boolean) PrefUtils.getFromPrefs(this, PrefKeys.USER_MARITAL_STATUS, false);
        float salaryFloat  = (Float) PrefUtils.getFromPrefs(this, PrefKeys.USER_SALARY_FLOAT, new Float(2.4));
        float salaryLong  = (Float) PrefUtils.getFromPrefs(this, PrefKeys.USER_SALARY_LONG, new Long(239847));
        double income = (Double) PrefUtils.getFromPrefs(this, PrefKeys.USER_INCOME, new Double(10));


        //Remove a value from preference
        PrefUtils.removeFromPrefs(this, PrefKeys.USER_INCOME);

        //Check whether a key is present in the preference or not
        boolean hasGender = PrefUtils.hasKey(this, PrefKeys.USER_GENDER);


    }

} */
//Todo merge the singleton functionality
/*
* public class PreferencesHelper {
    private static final String PREFS_NAME = "openSooqApp";
    private static final String FAVORITES = "Favorite";
    private static final String SELECTED = "lastSelected";
    private static final String FIRST_TIME = "my_first_time";
    private static SharedPreferences settings;
    private static List<String> favorites;
    private static PreferencesHelper singleton = null;

    private PreferencesHelper(Context context) {
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

    }

    public static synchronized PreferencesHelper getInstance(Context context) {
        if (singleton == null) {
            synchronized (PreferencesHelper.class) {
                if (singleton == null) {
                    singleton = new PreferencesHelper(context);
                }
            }
        }
        return singleton;
    }

    public void storeLastSelectedCity(String lastSelected) {
        settings.edit().putString(SELECTED, lastSelected).apply();
    }

    public boolean isFirstTime() {
        return settings.getBoolean(FIRST_TIME, true);
    }

    public void setFirstTime(boolean flag) {
        settings.edit().putBoolean(FIRST_TIME, flag).apply();
    }

    public void storeList(List<String> favorites) {
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);
        settings.edit().putString(FAVORITES, jsonFavorites).apply();
    }

    public String getLastSelectedCity() {
        return settings.getString(SELECTED, "Amman");
    }

    public List<String> loadList() {
        //singleton approach
        if (favorites == null)
            if (settings.contains(FAVORITES)) {
                String jsonFavorites = settings.getString(FAVORITES, null);
                Gson gson = new Gson();
                String[] listItems = gson.fromJson(jsonFavorites, String[].class);
                favorites = Arrays.asList(listItems);
                favorites = new ArrayList<>(favorites);
            }
        return favorites;
    }

    public void addItem(String item) {
        favorites = loadList();
        if (favorites == null)
            favorites = new ArrayList<>();
        favorites.add(item);
        storeList(favorites);
    }

    public void removeItem(String item) {
        favorites = loadList();
        if (favorites != null) {
            favorites.remove(item);
            storeList(favorites);
        }
    }
}*/

