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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaCodecInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.speech.RecognizerIntent;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
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

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Error;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.database.AppDatabase;
import ar.edu.itba.hci.uzr.intellifox.speech_analyzer.CommandExecutorTask;
import ar.edu.itba.hci.uzr.intellifox.ui.settings.SettingsViewModel;
import ar.edu.itba.hci.uzr.intellifox.wrappers.BelledDevices;
import ar.edu.itba.hci.uzr.intellifox.wrappers.QRInfo;
import ar.edu.itba.hci.uzr.intellifox.wrappers.TypeAndDeviceId;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.core.content.FileProvider.getUriForFile;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private final int REQ_CODE_SPEECH_INPUT = 100;

    private final static String DATABASE_NAME = "intellifox_db";

    private final static String BELLED_DEVICES = "belled_devices";

    static final String DEVICE_ID_ARG = "DEVICE_ID";
    static final String DEVICE_TYPE_NAME_ARG = "DEVICE_TYPE_NAME";

    static final String ROOM_ID_ARG = "room_id";

    static final String ROUTINE_ID_ARG = "routine_id";
    static final String ROUTINE_EXECUTION_ARG = "routine_execution";

    private static final String CHANNEL_ID = "NOTIFICATIONS";

    private static final String TAKE_PHOTO_TAG = "Take Photo";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri photoUri;
    private Bitmap bitmap;
    private BarcodeDetector detector;

    private CommandExecutorTask commandsInterpreter;

    public static final String MESSAGE_ID = "ar.edu.itba.MESSAGE_ID";
    public static final String MyPREFERENCES = "intellifoxPrefs";
    public static final String KEY_ISNIGHTMODE = "isNightMode";
    private SharedPreferences sharedPreferences;
    private AppDatabase db;

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
            drawable.setColorFilter(c, PorterDuff.Mode.SRC_ATOP);
        }

        Drawable drawable2 = menu.findItem(R.id.btnQrScann).getIcon();
        if (drawable2 != null) {
            int c = ContextCompat.getColor(this.getBaseContext(), R.color.background1);
            drawable2.mutate();
            drawable2.setColorFilter(c, PorterDuff.Mode.SRC_ATOP);
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
                    try {
                        handleQRScanBtn();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
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

        if (detector == null) {
            detector = new BarcodeDetector.Builder(getApplicationContext())
                    .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                    .build();
        }

        waitUntilBarcodeDetectorIsOperational(detector, 10);

        Location l1 = new Location("");
        l1.setLatitude(-34.7214235);
        l1.setLongitude(-58.2958171);
        Location l2 = new Location("");
        l2.setLatitude(-34.7232241);
        l2.setLongitude(-58.2940983);

        // De acá a 2 cuadras (casi 3)... devuelve 253 (supongo que son metros), así que esta bien
        Log.d("PRUEBA_LOCATION", String.valueOf(l1.distanceTo(l2)));

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
        if (extras != null) {
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
        if (sharedPreferences.getBoolean(KEY_ISNIGHTMODE, true)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
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
                HashSet<TypeAndDeviceId> tadis = belledDevices.getBelledDevices();
                if (tadis != null) {
                    DatabaseReloadTablesAsyncTask task = new DatabaseReloadTablesAsyncTask(tadis);
                    task.execute();
                    worked = true;
                }
            }
        }
        return worked;
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
//-----------------------------------------------------------------------QRs----------------------------------------------------------------------------

    private void handleQRScanBtn() throws FileNotFoundException {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.d("LLEGO AL ACTIVITY RESULT", "SIII");
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    //Log.d("ENTRA?", "SIIII");
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        bitmap = (Bitmap) extras.get("data");

                        if (bitmap != null) {
                            //Log.d("LO QUE HAY EN BITMAP ES:", bitmap.toString());
                            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                            SparseArray<Barcode> barcodeArray = detector.detect(frame);
                            //
                            if (barcodeArray.size() != 0) {
                                Barcode barcode = barcodeArray.valueAt(0);
                                processBarcodeScan(barcode.rawValue);
                            }
                            // Decode the barcode

                        } else {
                            //Log.d("RESPUESTAAAAAAAAAAAAAAAAAAAAAAA", "El BITMAP ES NULL");
                        }
                    }
                }
                break;
            case REQ_CODE_SPEECH_INPUT:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (result != null && result.size() > 0) {
                        String speech = result.get(0);
                        processSpeech(speech);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void processBarcodeScan(String json) {
        Gson gson = new Gson();
        Log.d("BARCODE_SCANNER", json);
        QRInfo qrInfo = gson.fromJson(json, QRInfo.class);
        String type = qrInfo.getType();
        String id = qrInfo.getId();
        if (type.equals("device")) {
            String typeName = qrInfo.getTypeName();
            if (typeName != null) {
                Bundle args = new Bundle();
                args.putString(DEVICE_ID_ARG, id);
                args.putString(DEVICE_TYPE_NAME_ARG, typeName);
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_device, args);
            }
        }
        else if (type.equals("room")) {
            Bundle args = new Bundle();
            args.putString(ROOM_ID_ARG, id);
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_room, args);
        }
        else if (type.equals("routine")) {
            boolean executable = qrInfo.isExecutable() != null && qrInfo.isExecutable();
            Bundle args = new Bundle();
            args.putString(ROUTINE_ID_ARG, id);
            args.putBoolean(ROUTINE_EXECUTION_ARG, executable);
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_routine, args);
        }
    }

    private void waitUntilBarcodeDetectorIsOperational(BarcodeDetector detector, int retries) {
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            Log.d(TAG, "Waiting for barcode detector to be operational...");
            if (retries > 0) {
                if(detector.isOperational()) {
                    Log.v("BARCODE DETECTOR", "LLego a ser operacional");
                } else {
                    waitUntilBarcodeDetectorIsOperational(detector, retries - 1);
                }
            } else {
                Log.e("BARCODE DETECTOR", "Nunca llego a ser operacional");
            }
        }, 10000);
    }


    //---------------------------------------------------------------------------------------------------------------------------------------------------

    // Speech-to-Text

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

    private void processSpeech(String speech) {
        CommandExecutorTask task = new CommandExecutorTask(speech);
        task.execute();
    }

}