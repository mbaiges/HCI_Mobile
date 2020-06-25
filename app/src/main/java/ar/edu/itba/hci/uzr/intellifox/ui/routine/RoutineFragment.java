package ar.edu.itba.hci.uzr.intellifox.ui.routine;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ar.edu.itba.hci.uzr.intellifox.broadcast_receivers.AlarmBroadcastReceiver;
import ar.edu.itba.hci.uzr.intellifox.MainActivity;
import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Error;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
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
    ImageButton btnSchedule, btnExecuteFromWithin;
    View listView;
    final Calendar myCalendar = Calendar.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_routine, container, false);
        listView = root.findViewById(R.id.routines_routine_detail_list);
        btnSchedule = root.findViewById(R.id.btnSchedule);
        btnExecuteFromWithin =  root.findViewById(R.id.btnExecuteFromWithin);


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = myCalendar.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        myCalendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                        myCalendar.set(Calendar.MINUTE, selectedMinute);
                        myCalendar.set(Calendar.SECOND, 0);
                        showToast();
                        createAlarm(myCalendar);
                    }
                }, hour, minute, true);//Yes 24 hour time

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        };

        if (btnSchedule != null) {
            btnSchedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DatePickerDialog(getActivity(), date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
        }

        if (btnExecuteFromWithin != null) {
            btnExecuteFromWithin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    executeRoutine(routineViewModel.getRoutine().getValue().getId());
                }
            });
        }

        routineViewModel = ViewModelProviders.of(this).get(RoutineViewModel.class);

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

    private void createAlarm(Calendar calendar){
        routineViewModel = ViewModelProviders.of(this).get(RoutineViewModel.class);

        Intent intent = new Intent(getActivity(), RoutineSchedulerReceiver.class);
        intent.putExtra("RoutineID", routineViewModel.getRoutine().getValue().getId());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);

        AlarmManager alarmMgr  = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        Log.v("SCHEDULE", "want to schedule for: " + myCalendar.getTime() + " - RoutineId: " + routineViewModel.getRoutine().getValue().getId());
    }

    private void showToast() {
        String msg = getActivity().getResources().getString(R.string.routine_future_execution);
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        //Toast.makeText(getActivity(), "Routine execution set for: " + myCalendar.getTime(), Toast.LENGTH_SHORT).show();
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

    public void executeRoutine(String id){
        if (id != null) {
            ApiClient.getInstance().executeRoutine(id, new Callback<Result<Boolean>>() {
                @Override
                public void onResponse(Call<Result<Boolean>> call, Response<Result<Boolean>> response) {
                    if (response.isSuccessful()) {
                        Log.v("ROUTINE_EXECUTE", "Routine executed successfully by alarm manager");
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
        }else{
            Log.v("EXECUTE", "NULL ID");
        }
    }


}
