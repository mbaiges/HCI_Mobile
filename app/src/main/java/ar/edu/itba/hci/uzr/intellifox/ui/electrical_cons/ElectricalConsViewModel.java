package ar.edu.itba.hci.uzr.intellifox.ui.electrical_cons;

import android.icu.util.LocaleData;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Error;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.log.DeviceLogRecord;
import ar.edu.itba.hci.uzr.intellifox.wrappers.ElectricalInfoRecord;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ElectricalConsViewModel extends ViewModel {

    static private DateTimeFormatter formatter;
    private Integer amount_of_days = 1;
    private final int STARTING_AMOUNT_OF_LOGS = 100;
    private final int INCREMENT_LOGS = 100;
    private int logs_to_fetch;
    private DateTime todaysDate, minDate;
    private static Map<String, Pair<String, String>> deviceTurnActions;
    private int lastSize;

    private MutableLiveData<List<ElectricalInfoRecord>> mElInfoRecords;

    public ElectricalConsViewModel() {
        mElInfoRecords = new MutableLiveData<>();
        logs_to_fetch = STARTING_AMOUNT_OF_LOGS;
    }

    public void search(int days) {
        todaysDate = new DateTime();
        amount_of_days = days;
        minDate = todaysDate.minusDays(amount_of_days);
        lastSize = 0;
        fetchElectricalInfoRecords();

        if (formatter == null) {
            formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        }
        if (deviceTurnActions == null) {
            deviceTurnActions = new HashMap<String, Pair<String, String>>() {{
                put("ac", new Pair<>("turnOn","turnOff"));
                put("speaker", new Pair<>("turnOn","turnOff"));
                put("lamp", new Pair<>("turnOn","turnOff"));
                put("vacuum", new Pair<>("start","pause"));
                put("door", new Pair<>("open","close"));
                put("blinds", new Pair<>("open","close"));
                put("oven", new Pair<>("turnOn","turnOff"));
                put("faucet", new Pair<>("open","close"));
            }};
        }

    }

    public LiveData<List<ElectricalInfoRecord>> getElectricalInfoRecords() {
        return mElInfoRecords;
    }

    private void fetchElectricalInfoRecords() {
        ApiClient.getInstance().getDevicesLogs(logs_to_fetch, 0, new Callback<Result<List<DeviceLogRecord>>>() {
            @Override
            public void onResponse(@NonNull Call<Result<List<DeviceLogRecord>>> call, @NonNull Response<Result<List<DeviceLogRecord>>> response) {
                if (response.isSuccessful()) {
                    Result<List<DeviceLogRecord>> result = response.body();
                    if (result != null) {
                        List<DeviceLogRecord> deviceLogRecords = result.getResult();
                        if (deviceLogRecords != null && deviceLogRecords.size() > 0) {
                            String lowestFormattedDate = deviceLogRecords.stream().reduce((a, b) -> (a.getTimestamp().compareTo(b.getTimestamp()) < 0)?a:b).get().getTimestamp();
                            DateTime lowestDate = DateTime.parse(lowestFormattedDate, formatter);
                            //Log.v("LOWEST_DATE", lowestFormattedDate);
                            if ( lowestDate.compareTo(minDate) < 0 || (lastSize > 0 && deviceLogRecords.size() == lastSize) ) {
                                String formattedMinDate = minDate.toString(formatter);
                                //Log.v("FORMATTED_MIN_DATE", formattedMinDate);
                                List<DeviceLogRecord> inRangeRecords = deviceLogRecords.stream().filter(d -> d != null && d.getTimestamp() != null && d.getTimestamp().compareTo(formattedMinDate) > 0).collect(Collectors.toList());
                                if (inRangeRecords.size() > 0) {
                                    getElectricalWattagePerDevice(inRangeRecords);
                                }
                            }
                            else {
                                logs_to_fetch += INCREMENT_LOGS;
                                fetchElectricalInfoRecords();
                            }
                            lastSize = deviceLogRecords.size();
                            //mLogs.postValue(deviceLogRecords.stream().filter(dlr -> dlr.getTimestamp()).sorted(Comparator.comparing(DeviceLogRecord::getTimestamp).reversed()).collect(Collectors.toList()));
                        }
                    } else {
                        handleError(response);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<List<DeviceLogRecord>>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    private void getElectricalWattagePerDevice(List<DeviceLogRecord> inRangeRecords) {
        ApiClient.getInstance().getDevices(new Callback<Result<List<Device>>>() {
            @Override
            public void onResponse(@NonNull Call<Result<List<Device>>> call, @NonNull Response<Result<List<Device>>> response) {
                if (response.isSuccessful()) {
                    Result<List<Device>> result = response.body();
                    if (result != null) {
                        List<Device> comingDevicesList = result.getResult();
                        if (comingDevicesList != null && comingDevicesList.size() > 0) {
                            List<ElectricalInfoRecord> deviceElectricalRecords = comingDevicesList.stream().map(d -> new ElectricalInfoRecord(d.getId(), d.getName(), d.getType().getName(), 0.0, d.getType().getPowerUsage())).collect(Collectors.toList());
                            getCalculatedElectricalInfoRecords(inRangeRecords, deviceElectricalRecords);
                        }
                    } else {
                        handleError(response);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<List<Device>>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    private void getCalculatedElectricalInfoRecords(List<DeviceLogRecord> inRangeRecords, List<ElectricalInfoRecord> deviceElectricalRecords) {
        List<DeviceLogRecord> sortedInRangeRecords = inRangeRecords.stream().sorted(Comparator.comparing(DeviceLogRecord::getTimestamp)).collect(Collectors.toList());
        for (ElectricalInfoRecord record : deviceElectricalRecords) {
            String deviceId = record.getDeviceId();
            if (deviceId != null) {
                List<Interval> intervals = new LinkedList<>();
                DateTime start = null;
                for(DeviceLogRecord deviceLogRecord : sortedInRangeRecords) {
                    if (deviceLogRecord != null && deviceId.equals(deviceLogRecord.getDeviceId())) {
                        String formattedDate = deviceLogRecord.getTimestamp();
                        if (formattedDate != null) {
                            DateTime date = DateTime.parse(formattedDate, formatter.withZoneUTC());
                            String deviceTypeName = record.getDeviceTypeName();
                            //Log.v("DEVICE_LOG_RECORD", "Device - " + record.getDeviceName() + " showed log for " + deviceLogRecord.getAction() + " at " + date);
                            Pair<String,String> turnActionPair = deviceTurnActions.get(deviceTypeName);
                            if (turnActionPair != null) {
                                if (start == null) {
                                    if (turnActionPair.first.equals(deviceLogRecord.getAction())) {
                                        //Log.v("DEVICE_LOG_RECORD", "Device - " + record.getDeviceName() + " has started at: " + date);
                                        start = date;
                                    }
                                }
                                else {
                                    if (turnActionPair.second.equals(deviceLogRecord.getAction())) {
                                        Interval interval = new Interval(start, date);
                                        //Log.v("DEVICE_LOG_RECORD", "Device - " + record.getDeviceName() + " has finished at: " + date);
                                        //Log.v("DEVICE_LOG_RECORD", "Device - " + record.getDeviceName() + " has closed an interval: " + interval.toPeriod().getSeconds());
                                        intervals.add(interval);
                                        start = null;
                                    }
                                }
                            }
                        }
                    }
                }
                double hours = getHours(intervals);
                record.setHours(hours);
                //Log.v("DEVICE_LOG_RECORD", "Device - " + record.getDeviceName() + " acumulates " + hours + " hours");
            }
        }
        mElInfoRecords.postValue(deviceElectricalRecords);
    }

    public double getHours(final List<Interval> intervals) {
        Duration duration = null;
        for (final Interval interval : intervals) {
            if (duration == null) {
                duration = interval.toDuration();
            } else {
                duration = duration.plus(interval.toDuration());
            }
        }
        if (intervals.size() == 0 || duration == null) {
            return 0.0;
        }
        else {
            return ( ( (double) duration.getMillis() ) / 3600000 );
        }
    }

    private <T> void handleError(Response<T> response) {
        Error error = ApiClient.getInstance().getError(response.errorBody());
        String desc = error.getDescription();
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