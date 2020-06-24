package ar.edu.itba.hci.uzr.intellifox.ui.routine;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
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
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceArrayAdapter;
import ar.edu.itba.hci.uzr.intellifox.api.models.routine.Routine;
import ar.edu.itba.hci.uzr.intellifox.api.models.routine_action.RoutineAction;
import ar.edu.itba.hci.uzr.intellifox.api.models.routine_action.RoutineActionArrayAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RoutineFragment extends Fragment {

    static final String ROUTINE_ID_ARG = "routine_id";
    static final String ROUTINE_EXECUTION_ARG = "routine_execution";

    RoutineViewModel routineViewModel;
    View listView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_routine, container, false);
        listView = root.findViewById(R.id.routines_routine_detail_list);

        routineViewModel =
                ViewModelProviders.of(this).get(RoutineViewModel.class);

        Bundle bundle = this.getArguments();
        String routineId = null;
        boolean executable = false;
        if (bundle != null) {
            routineId = bundle.getString(ROUTINE_ID_ARG, null);
            executable = bundle.getBoolean(ROUTINE_EXECUTION_ARG, false);
        }

        if (routineId != null) {
            if (executable) {
                ApiClient.getInstance().executeRoutine(routineId, new Callback<Result<Boolean>>() {
                    @Override
                    public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                        if (response.isSuccessful()) {
                            //Log.v("ROUTINE_EXECUTE", "Routine executed successfully");
                        }
                        else {
                            handleError(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<Result<Boolean>> call, Throwable t) {
                        handleUnexpectedError(t);
                    }
                });
            }
            routineViewModel.getRoutine().observe(getViewLifecycleOwner(), new Observer<Routine>() {
                @Override
                public void onChanged(Routine routine) {
                    if (routine != null) {
                        String routineName = routine.getName();
                        if (routineName != null) {
                            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(routineName);
                        }
                    }
                }
            });
            routineViewModel.getRoutine().observe(getViewLifecycleOwner(), new Observer<Routine>() {
                @Override
                public void onChanged(@Nullable Routine routine) {
                    if (routine != null) {
                        RoutineAction[] routineActions = routine.getActions();
                        if (routineActions != null) {
                            RoutineActionArrayAdapter adapter = new RoutineActionArrayAdapter(getActivity(), routineActions);
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
                }
            });
            routineViewModel.init(routineId);
        }

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