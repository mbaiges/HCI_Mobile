package ar.edu.itba.hci.uzr.intellifox;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaCodecInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.RecognizerIntent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Error;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.database.AppDatabase;
import ar.edu.itba.hci.uzr.intellifox.ui.settings.SettingsViewModel;
import ar.edu.itba.hci.uzr.intellifox.wrappers.BelledDevices;
import ar.edu.itba.hci.uzr.intellifox.wrappers.TypeAndDeviceId;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private final int REQ_CODE_SPEECH_INPUT = 100;

    private final static String DATABASE_NAME = "intellifox_db";

    private final static String BELLED_DEVICES = "belled_devices";

    static final String DEVICE_ID_ARG = "DEVICE_ID";
    static final String DEVICE_TYPE_NAME_ARG = "DEVICE_TYPE_NAME";

    private static final String CHANNEL_ID = "NOTIFICATIONS";


    public static final String MESSAGE_ID = "ar.edu.itba.MESSAGE_ID";
    public static final String MyPREFERENCES = "intellifoxPrefs";
    public static final String KEY_ISNIGHTMODE = "isNightMode";
    SharedPreferences sharedPreferences;
    AppDatabase db;

    private AlarmManager alarmManager;
    private PendingIntent alarmBroadcastReceiverPendingIntent;
    private final static int INTERVAL = 60000;
    public final static String TAG = "Alarm";


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.app_bar_buttons, menu);

        Drawable drawable = menu.findItem(R.id.btnMicrophone).getIcon();
        if (drawable != null) {
            int c = ContextCompat.getColor(this.getBaseContext(), R.color.background1);
            drawable.mutate();
            drawable.setColorFilter(c,PorterDuff.Mode.SRC_ATOP);
        }

        Drawable drawable2 = menu.findItem(R.id.btnQrScann).getIcon();
        if (drawable2 != null) {
            int c = ContextCompat.getColor(this.getBaseContext(), R.color.background1);
            drawable2.mutate();
            drawable2.setColorFilter(c,PorterDuff.Mode.SRC_ATOP);
        }

        MenuItem appBtn1 = menu.findItem(R.id.btnMicrophone);
        if (appBtn1 != null) {
            appBtn1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Log.v("BTN", "Microphone Clicked");
                    handleMicrophoneBtn();
                    return true;
                }
            });
        }

        MenuItem appBtn2 = menu.findItem(R.id.btnQrScann);
        if (appBtn2 != null) {
            appBtn2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Log.v("BTN", "Qr Scann Clicked");
                    handleQRScanBtn();
                    return true;
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent alarmNotificationReceiverIntent = new Intent(MainActivity.this, AlarmBroadcastReceiver.class);
        alarmBroadcastReceiverPendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmNotificationReceiverIntent, 0);
        createNotificationChannel();


//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_heart_outline)
//                .setContentTitle("My notification")
//                .setContentText("Much longer text that cannot fit one line...")
//                .setStyle(new NotificationCompat.BigTextStyle()
//                .bigText("Much longer text that cannot fit one line..."))
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        SharedPreferencesGetter.setInstance(sharedPreferences);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, DATABASE_NAME).build();

        DatabaseGetter.setInstance(db);

        checkNightModeActivated();
        //checkLanguage();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //FloatingActionButton fab = findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //       Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                .setAction("Action", null).show();
        //    }
        //});
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_favourites, R.id.nav_rooms, R.id.nav_device_types, R.id.nav_routines, R.id.nav_electrical_cons, R.id.nav_history, R.id.nav_settings, R.id.nav_help_and_feed)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String value = extras.getString(MESSAGE_ID);
            if (value != null) {
                treatNotification(value);
            }
        }
    }

    private void treatNotification(String info) {
        String[] params = info.split(",");
        String deviceType = params[0];
        String deviceId = params[1];

        Bundle args = new Bundle();
        args.putString(DEVICE_ID_ARG, deviceId);
        args.putString(DEVICE_TYPE_NAME_ARG, deviceType);
        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_device, args);
    }

    public void checkNightModeActivated() {
        if(sharedPreferences.getBoolean(KEY_ISNIGHTMODE, true)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (fetchAndSaveBelledDevices()) {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime() + INTERVAL,
                    INTERVAL,
                    alarmBroadcastReceiverPendingIntent);
        }
    }

    private boolean fetchAndSaveBelledDevices() {
        final Gson gson = new Gson();
        boolean worked = false;
        String json = sharedPreferences.getString(BELLED_DEVICES, "");
        if (!json.equals("")) {
            BelledDevices belledDevices = gson.fromJson(json, BelledDevices.class);
            if (belledDevices != null) {
                HashSet<TypeAndDeviceId> tadis =  belledDevices.getBelledDevices();
                if (tadis != null) {
                    DatabaseTruncateTablesAsyncTask task = new DatabaseTruncateTablesAsyncTask();
                    task.execute();
                    for (TypeAndDeviceId tadi: tadis) {
                        String deviceId = tadi.getDeviceId();
                        String typeName = tadi.getTypeName();
                        if (deviceId != null && typeName != null) {
                            ApiClient.getInstance().getDevice(deviceId, new Callback<Result<Device>>() {
                                @Override
                                public void onResponse(Call<Result<Device>> call, Response<Result<Device>> response) {
                                    if (response.isSuccessful()) {
                                        Result<Device> result = response.body();
                                        if (result != null) {
                                            Device device = result.getResult();
                                            if (device != null) {
                                                Log.d("DEVICE_BELLED", result.getResult().toString());
                                                DatabaseAddDeviceAsyncTask task = new DatabaseAddDeviceAsyncTask(typeName, device);
                                                task.execute();
                                            }
                                        } else {
                                            handleError(response);
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<Result<Device>> call, @NonNull Throwable t) {
                                    handleUnexpectedError(t);
                                }
                            });
                        }
                        worked = true;
                    }
                }
            }
        }
        return worked;
    }

    protected <T> void handleError(Response<T> response) {
        Error error = ApiClient.getInstance().getError(response.errorBody());
        List<String> descList = error.getDescription();
        String desc = "";
        if (descList != null) {
            desc = descList.get(0);
        }
        String code = "Code " + String.valueOf(error.getCode());
        Log.e("ERROR", code + " - " + desc);
        /*
        String text = getResources().getString(R.string.error_message, error.getDescription().get(0), error.getCode());
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
        */
    }

    protected void handleUnexpectedError(Throwable t) {
        String LOG_TAG = "ar.edu.itba.hci.uzr.intellifox.api";
        Log.e(LOG_TAG, t.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        alarmManager.cancel(alarmBroadcastReceiverPendingIntent);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(R.color.primary);
            channel.enableVibration(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void handleQRScanBtn(){
        
    }

    private void handleMicrophoneBtn(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getResources().getString(R.string.prompt));

        if (intent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_SPEECH_INPUT &&
                resultCode == RESULT_OK &&
                null != data) {

            ArrayList<String> result = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            //recognizedText.setText(result != null && result.size() > 0 ? result.get(0) : "");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}