package ar.edu.itba.hci.uzr.intellifox.ui.routine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.DateFormat;
import java.util.Date;

import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoutineSchedulerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String id = intent.getStringExtra("RoutineID");
        Log.v("ROUTINE_ALARM:", "routine id: " + id + " executed at: " + DateFormat.getDateTimeInstance().format(new Date()));
        executeRoutine(id);
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
                        //handleError(response);
                    }
                }

                @Override
                public void onFailure(Call<Result<Boolean>> call, Throwable t) {
                    //handleUnexpectedError(t);
                }
            });
        }else{
            Log.v("EXECUTE", "NULL ID");
        }
    }
}
