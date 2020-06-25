package ar.edu.itba.hci.uzr.intellifox.path_highlighter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import ar.edu.itba.hci.uzr.intellifox.settings.FusedLocationClientSetting;
import ar.edu.itba.hci.uzr.intellifox.MainActivity;
import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Error;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.room.Room;
import ar.edu.itba.hci.uzr.intellifox.api.models.room.RoomMeta;
import ar.edu.itba.hci.uzr.intellifox.api.models.room.RoomMetaLocation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PathHighlighter {

    static private final String PATH_HIGHLIGHTER_TAG = "Path_Highlighter";

    static private final int FREQ_IN_SECS = 5;
    static private final String LIGHTS_COLOR = "FFFFFF";
    static private final String SET_COLOR_COMMAND = "setColor";
    static private final String TURN_ON_COMMAND = "turnOn";

    static private final String LIGHT_TYPE_NAME = "lamp";
    private static final int PERMISSION_ACCESS_FINE_LOCATION = 1;

    static private boolean highlighting = false;

    static private FusedLocationProviderClient fusedLocationClient;

    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> pathHighlighterHandler;

    static private PathHighlighter instance;
    static private AppCompatActivity mainActivity;

    private PathHighlighter() { }

    static public PathHighlighter getInstance() {
        if (fusedLocationClient == null) {
            fusedLocationClient = FusedLocationClientSetting.getInstance();
        }
        if (instance == null) {
            instance = new PathHighlighter();
        }
        return instance;
    }

    static public void associateActivity(AppCompatActivity activity) {
        mainActivity = activity;
    }

    public void highlightPath(boolean intentSwitch) {
        Log.d(PATH_HIGHLIGHTER_TAG, "I want to highlight path");
        if (mainActivity != null) {
            Log.d(PATH_HIGHLIGHTER_TAG, "Activity found");
            if (intentSwitch && !highlighting) {
                final Runnable fetcher = new Runnable() {
                    public void run() {
                        checkAndHighlight();
                    }
                };
                pathHighlighterHandler = scheduler.scheduleAtFixedRate(fetcher, 0, FREQ_IN_SECS, TimeUnit.SECONDS);
                highlighting = true;
            }
            else if (!intentSwitch && highlighting) {
                pathHighlighterHandler.cancel(true);
                highlighting = false;
            }
        }
    }

    private void checkAndHighlight() {
        Log.d(PATH_HIGHLIGHTER_TAG, "Checking For Highligting");
        Location userLocation;
        if (ActivityCompat.checkSelfPermission(mainActivity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mainActivity.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mainActivity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_ACCESS_FINE_LOCATION);
            // PERMISSION_ACCESS_FINE_LOCATION is an app-defined int constant. The callback method gets the result of the request.
            //realmente no entendi bien esto ultmo de PERMISSION_ACCESS_FINE_LOCATION
        }
        else {
            Log.d(PATH_HIGHLIGHTER_TAG, "Can access location");
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(mainActivity, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            Log.d(PATH_HIGHLIGHTER_TAG, "Reached Success");
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Log.d(PATH_HIGHLIGHTER_TAG, "MyLocation " + location);
                                ApiClient.getInstance().getRooms(new Callback<Result<List<Room>>>() {
                                    @Override
                                    public void onResponse(@NonNull Call<Result<List<Room>>> call, @NonNull Response<Result<List<Room>>> response) {
                                        if (response.isSuccessful()) {
                                            Result<List<Room>> result = response.body();
                                            if (result != null) {
                                                List<Room> roomList = result.getResult();
                                                if (roomList != null) {
                                                    List<Room> roomsInRange = roomList.stream().filter(r -> inRangeWithRoom(r, location)).collect(Collectors.toList());
                                                    if (roomsInRange != null) {
                                                        for(Room r: roomsInRange) {
                                                            if (r != null) {
                                                                String roomId = r.getId();
                                                                if (roomId != null) {
                                                                    findLightsAndSetThem(roomId);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                handleError(response);
                                            }
                                        }
                                    }
                                    @Override
                                    public void onFailure(@NonNull Call<Result<List<Room>>> call, @NonNull Throwable t) {
                                        handleUnexpectedError(t);
                                    }
                                });
                            }
                            else {
                                Log.d(PATH_HIGHLIGHTER_TAG, "Where is location?");
                            }
                        }
                    });
        }
    }

    private boolean inRangeWithRoom(Room r, Location userLocation) {
        if (r != null) {
            RoomMeta roomMeta = r.getMeta();
            if (roomMeta != null) {
                RoomMetaLocation roomMetaLocation = roomMeta.getLocation();
                if (roomMetaLocation != null) {
                    Double latitude = roomMetaLocation.getLatitude();
                    Double longitude = roomMetaLocation.getLongitude();
                    Double radius = roomMetaLocation.getRadius();
                    if (latitude != null && longitude != null && radius != null) {
                        Location roomLocation = new Location("");
                        roomLocation.setLatitude(latitude);
                        roomLocation.setLongitude(longitude);

                        return roomLocation.distanceTo(userLocation) < radius;
                    }
                }
            }
        }
        return false;
    }

    private void findLightsAndSetThem(String roomId) {
        ApiClient.getInstance().getDevicesByRoom(roomId, new Callback<Result<List<Device>>>() {
            @Override
            public void onResponse(Call<Result<List<Device>>> call, Response<Result<List<Device>>> response) {
                if(response.isSuccessful()){
                    Result<List<Device>> result = response.body();
                    if(result != null){
                        List<Device> devicesList =  result.getResult();
                        if(devicesList != null){
                            List<String> lightsIds = devicesList.stream().filter(d -> d != null && d.getType() != null && d.getType().getName() != null && d.getType().getName().equals(LIGHT_TYPE_NAME)).map(Device::getId).collect(Collectors.toList());
                            Log.d(PATH_HIGHLIGHTER_TAG, "Setting lights " + lightsIds);
                            turnLights(lightsIds);
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<Result<List<Device>>> call, Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    private void turnLights(List<String> ids) {
        String[] args = {};
        for(String id: ids) {
            ApiClient.getInstance().executeDeviceAction(id, TURN_ON_COMMAND, args, new Callback<Result<Object>>() {
                @Override
                public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                    if(response.isSuccessful()){
                        Result<Object> result = response.body();
                        if(result != null){
                            Object success =  result.getResult();
                            if(success != null){
                                String[] colorArgs = {LIGHTS_COLOR};
                                Log.d(PATH_HIGHLIGHTER_TAG, "Light Turned On");
                                ApiClient.getInstance().executeDeviceAction(id, SET_COLOR_COMMAND, colorArgs, new Callback<Result<Object>>() {
                                    @Override
                                    public void onResponse(Call<Result<Object>> call, Response<Result<Object>> response) {
                                        if(response.isSuccessful()){
                                            Result<Object> result = response.body();
                                            if(result != null){
                                                Object success =  result.getResult();
                                                if(success != null){
                                                    Log.d(PATH_HIGHLIGHTER_TAG, "Light Color Set");
                                                }
                                            }
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<Result<Object>> call, Throwable t) {
                                        handleUnexpectedError(t);
                                    }
                                });
                            }
                        }
                    }
                }
                @Override
                public void onFailure(Call<Result<Object>> call, Throwable t) {
                    handleUnexpectedError(t);
                }
            });
        }
    }

    private <T> void handleError(Response<T> response) {
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

    private void handleUnexpectedError(Throwable t) {
        String LOG_TAG = "ar.edu.itba.hci.uzr.intellifox.api";
        Log.e(LOG_TAG, t.toString());
    }
}
