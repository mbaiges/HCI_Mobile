package ar.edu.itba.hci.uzr.intellifox.api;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Button createRoomButton;
    private Button modifyRoomButton;
    private Button deleteRoomButton;
    private Button getRoomButton;
    private Button getRoomsButton;
    private TextView resultTextView;
    private Room room;
    private int index = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = findViewById(R.id.result);

        createRoomButton = findViewById(R.id.create_room);
        if (createRoomButton != null) {
            createRoomButton.setOnClickListener(v -> createRoom());
        }

        modifyRoomButton = findViewById(R.id.modify_room);
        if (modifyRoomButton != null) {
            modifyRoomButton.setOnClickListener(v -> modifyRoom());
        }

        deleteRoomButton = findViewById(R.id.delete_room);
        if (deleteRoomButton != null) {
            deleteRoomButton.setOnClickListener(v -> deleteRoom());
        }

        getRoomButton = findViewById(R.id.get_room);
        if (getRoomButton != null) {
            getRoomButton.setOnClickListener(v -> getRoom());
        }

        getRoomsButton = findViewById(R.id.get_rooms);
        if (getRoomsButton != null) {
            getRoomsButton.setOnClickListener(v -> getRooms());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        //TODO: cancel Call<T>
    }

    private void createRoom() {
        room = new Room("kitchen" + index++, new RoomMeta("9m2"));
        ApiClient.getInstance().addRoom(room, new Callback<Result<Room>>() {
            @Override
            public void onResponse(@NonNull  Call<Result<Room>> call, @NonNull Response<Result<Room>> response) {
                if (response.isSuccessful()) {
                    Result<Room> result = response.body();
                    if (result != null) {
                        room.setId(result.getResult().getId());
                        showResult(room.toString());
                        toggleButtons(true);
                    } else {
                        showResult("null");
                    }
                } else {
                    handleError(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<Room>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    private void modifyRoom() {
        room.getMeta().setSize("6m2");
        ApiClient.getInstance().modifyRoom(room, new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Boolean>> call, @NonNull Response<Result<Boolean>> response) {
                if (response.isSuccessful()) {
                    Result<Boolean> result = response.body();
                    showResult(result != null ? result.getResult().toString() : "null");
                } else {
                    handleError(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<Boolean>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    private void deleteRoom() {
        ApiClient.getInstance().deleteRoom(room.getId(), new Callback<Result<Boolean>>() {
            @Override
            public void onResponse(@NonNull  Call<Result<Boolean>> call, @NonNull Response<Result<Boolean>> response) {
                if (response.isSuccessful()) {
                    Result<Boolean> result = response.body();
                    showResult(result != null ? result.getResult().toString() : "null");
                    toggleButtons(false);
                } else {
                    handleError(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<Boolean>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    private void getRoom() {
        ApiClient.getInstance().getRoom(room.getId(), new Callback<Result<Room>>() {
            @Override
            public void onResponse(@NonNull Call<Result<Room>> call, @NonNull Response<Result<Room>> response) {
                if (response.isSuccessful()) {
                    Result<Room> result = response.body();
                    resultTextView.setText(result != null ? result.getResult().toString() : "null");
                } else {
                    handleError(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<Room>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    private void getRooms() {
        ApiClient.getInstance().getRooms(new Callback<Result<List<Room>>>() {
            @Override
            public void onResponse(@NonNull Call<Result<List<Room>>> call, @NonNull Response<Result<List<Room>>> response) {
                if (response.isSuccessful()) {
                    Result<List<Room>> result = response.body();
                    showResult(result != null ? TextUtils.join(",", result.getResult()) : "null");
                } else {
                    handleError(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<List<Room>>> call, @NonNull Throwable t) {
                handleUnexpectedError(t);
            }
        });
    }

    private void showResult(String result) {
        String format = getResources().getString(R.string.result);
        resultTextView.setText(String.format(format, result));
    }

    private void toggleButtons(boolean enabled) {
        createRoomButton.setEnabled(!enabled);
        modifyRoomButton.setEnabled(enabled);
        deleteRoomButton.setEnabled(enabled);
        getRoomButton.setEnabled(enabled);
        getRoomsButton.setEnabled(enabled);
    }

    private <T> void handleError(Response<T> response) {
        Error error = ApiClient.getInstance().getError(response.errorBody());
        String text = getResources().getString(R.string.error_message, error.getDescription().get(0), error.getCode());
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
    }

    private void handleUnexpectedError(Throwable t) {
        String LOG_TAG = "edu.itba.example.api";
        Log.e(LOG_TAG, t.toString());
    }
}
