package ar.edu.itba.hci.uzr.intellifox.ui.history;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.List;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Error;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.log.DeviceLogRecord;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.log.DeviceLogRecordArrayAdapter;
import ar.edu.itba.hci.uzr.intellifox.api.models.routine.Routine;
import ar.edu.itba.hci.uzr.intellifox.api.models.routine_action.RoutineAction;
import ar.edu.itba.hci.uzr.intellifox.api.models.routine_action.RoutineActionArrayAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {

    HistoryViewModel historyViewModel;
    View listView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View root = inflater.inflate(R.layout.fragment_history, container, false);
    listView = root.findViewById(R.id.history_logs_list);

    historyViewModel =
            ViewModelProviders.of(this).get(HistoryViewModel.class);
        historyViewModel.getDeviceLogs().observe(getViewLifecycleOwner(), new Observer<List<DeviceLogRecord>>() {
            @Override
            public void onChanged(@Nullable List<DeviceLogRecord> deviceLogRecords) {
                if (deviceLogRecords != null) {
                    DeviceLogRecord[] deviceLogRecordsArray = new DeviceLogRecord[deviceLogRecords.size()];
                    int i = 0;
                    for (DeviceLogRecord dli: deviceLogRecords) {
                        deviceLogRecordsArray[i++] = dli;
                    }
                    DeviceLogRecordArrayAdapter adapter = new DeviceLogRecordArrayAdapter(getActivity(), deviceLogRecordsArray);
                    int orientation = getResources().getConfiguration().orientation;
                    /*
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        // In landscape
                        ((GridView) listView).setAdapter(adapter);
                    } else {
                        // In portrait
                        ((ListView) listView).setAdapter(adapter);
                    }
                    */
                    ((ListView) listView).setAdapter(adapter);
                }
            }
        });

        return root;
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
}