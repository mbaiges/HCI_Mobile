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
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceMeta;
import ar.edu.itba.hci.uzr.intellifox.api.models.device_type.DeviceType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoutineArrayAdapter extends ArrayAdapter<Routine> {

    static final String ROUTINE_ID_ARG = "routine_id";

    private final static Integer FAVOURITE_ICON = R.drawable.ic_heart_filled;
    private final static Integer NON_FAVOURITE_ICON = R.drawable.ic_heart_outline;

    public RoutineArrayAdapter(Activity context, Routine[] objects) {
        super(context, R.layout.routine_card_item, objects);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        RoutineViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.routine_card_item, parent, false);
            holder = new RoutineViewHolder();
            holder.iconView = convertView.findViewById(R.id.icon);
            holder.nameTextView = convertView.findViewById(R.id.name);
            holder.arrowBtn = convertView.findViewById(R.id.arrowBtn);
            holder.favourite = convertView.findViewById(R.id.favourite);
            holder.executeIconView = convertView.findViewById(R.id.executeIcon);
            convertView.setTag(holder);
        } else {
            holder = (RoutineViewHolder) convertView.getTag();
        }

        Routine routine = getItem(position);
        if (routine != null) {
            RoutineMeta meta = routine.getMeta();
            if (holder.arrowBtn != null) {
                Bundle args = new Bundle();
                args.putString(ROUTINE_ID_ARG, routine.getId());
                holder.arrowBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_routine, args));
            }
            if (meta != null) {
                Boolean fav = meta.getFavourites();
                if (fav != null) {
                    if (holder.favourite != null) {
                        holder.favourite.setImageResource(fav?FAVOURITE_ICON:NON_FAVOURITE_ICON);
                        holder.favourite.setColorFilter(ContextCompat.getColor(convertView.getContext(), R.color.icon));
                    }
                }
            }
            if (holder.favourite != null) {
                View finalConvertView = convertView;
                holder.favourite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RoutineMeta meta = routine.getMeta();
                        if (meta != null) {
                            Boolean fav = meta.getFavourites();
                            if (fav != null) {
                                fav = !fav;
                                meta.setFavourites(fav);
                                routine.setMeta(meta);
                                updateRoutine(routine);
                                if (holder.favourite != null) {
                                    holder.favourite.setImageResource(fav?FAVOURITE_ICON:NON_FAVOURITE_ICON);
                                    holder.favourite.setColorFilter(ContextCompat.getColor(finalConvertView.getContext(), R.color.icon));
                                }
                            }
                        }
                    }
                });
            }
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

    private void updateRoutine(Routine routine) {
        ApiClient.getInstance().modifyRoutine(routine.getId(), routine, new Callback<Result<Routine>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Routine>> call, @NonNull Response<Result<Routine>> response) {
                if (response.isSuccessful()) {
                    Result<Routine> result = response.body();

                    if (result != null) {
                        Log.v("RESULT", result.toString());
                    } else {
                        handleError(response);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<Routine>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        });
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
