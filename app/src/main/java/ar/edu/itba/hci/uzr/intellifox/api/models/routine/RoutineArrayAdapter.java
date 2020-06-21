package ar.edu.itba.hci.uzr.intellifox.api.models.routine;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Error;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device_type.DeviceType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoutineArrayAdapter extends ArrayAdapter<Routine> {
    public RoutineArrayAdapter(Activity context, Routine[] objects) {
        super(context, R.layout.device_card_item, objects);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        RoutineViewHolder holder;
        RoutineViewHolder holder2;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.routine_card_item, parent, false);
            holder = new RoutineViewHolder();
            holder.executeIconView = convertView.findViewById(R.id.executeIcon);
            holder.iconView = convertView.findViewById(R.id.icon);
            holder.nameTextView = convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (RoutineViewHolder) convertView.getTag();
            holder2 = (RoutineViewHolder) convertView.getTag();
        }

        Routine routine = getItem(position);
        if (routine != null) {
            ColorFilter filter = new PorterDuffColorFilter(Color.parseColor(routine.getMeta().getColor()), PorterDuff.Mode.SRC_IN);
            holder.executeIconView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    executeRoutine(routine);
                }
            });
            holder.iconView.setColorFilter(filter);
            holder.nameTextView.setText(routine.getName());
        }

        return convertView;
    }

    public void executeRoutine(Routine routine){
        String id = routine.getId();
        if (id != null) {
            ApiClient.getInstance().executeRoutine(id, new Callback<Result<Boolean>>() {
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
        }else{
            Log.v("EXECUTE", "NULL ID");
        }
    }

    private <T> void handleError(Response<T> response) {
        Error error = ApiClient.getInstance().getError(response.errorBody());
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
