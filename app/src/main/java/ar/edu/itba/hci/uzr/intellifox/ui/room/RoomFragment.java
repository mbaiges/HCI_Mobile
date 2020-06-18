package ar.edu.itba.hci.uzr.intellifox.ui.room;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.List;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.models.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.models.Error;
import ar.edu.itba.hci.uzr.intellifox.api.models.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.room.Room;
import ar.edu.itba.hci.uzr.intellifox.api.models.room.RoomArrayAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomFragment extends Fragment {

    GridView gridView;
    Room[] rooms;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_rooms, container, false);
        gridView = root.findViewById(R.id.rooms_grid_view);
//        Room[] values = new Room[]{
//                new Room(1, "Mati's Room"),
//                new Room(2, "Garden"),
//                new Room(3, "Kitchen"),
//                new Room(4, "Gabi's Playroom"),
//                new Room(5, "Loundry Room")
//        };
        getRooms(gridView);
        return root;
    }


    private void getRooms(GridView gridView) {
        ApiClient.getInstance().getRooms(new Callback<Result<List<Room>>>() {
            @Override
            public void onResponse(@NonNull Call<Result<List<Room>>> call, @NonNull Response<Result<List<Room>>> response) {
                if (response.isSuccessful()) {
                    Result<List<Room>> result = response.body();
                    //showResult(result != null ? TextUtils.join(",", result.getResult()) : "null");
                    if (result != null) {
                        List<Room> roomList = result.getResult();
                        rooms = new Room[roomList.size()];
                        int i = 0;
                        for (Room r : roomList) {
                            rooms[i++] = r;
                        }
                        for (Room r: rooms) {
                            Log.d("ROOM:", r.getId() + "+" + r.getName() + "+" + r.getMeta().getDesc() + "+" + r.getMeta().getIcon());
                        }
                        RoomArrayAdapter adapter = new RoomArrayAdapter(getActivity(), rooms);
                        gridView.setAdapter(adapter);
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

    private <T> void handleError(Response<T> response) {
        Error error = ApiClient.getInstance().getError(response.errorBody());
        String text = getResources().getString(R.string.error_message, error.getDescription().get(0), error.getCode());
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }

    private void handleUnexpectedError(Throwable t) {
        String LOG_TAG = "ar.edu.itba.hci.uzr.intellifox.api";
        Log.e(LOG_TAG, t.toString());
    }
}