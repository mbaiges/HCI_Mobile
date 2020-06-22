package ar.edu.itba.hci.uzr.intellifox.ui.devices.speaker;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.SpeakerDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.SpeakerDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.SpeakerSong;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.TapDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.TapDeviceState;
import ar.edu.itba.hci.uzr.intellifox.ui.devices.DeviceObserver;
import ar.edu.itba.hci.uzr.intellifox.ui.devices.tap.TapDeviceViewHolder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpeakerDeviceObserver extends DeviceObserver {

    public SpeakerDeviceObserver(View contextView) {
        super(contextView);
    }

    private Integer volume;
    private String genre;

    @Override
    protected void createHolder() {
        this.holder = new SpeakerDeviceViewHolder();
    }

    @Override
    protected void findElements() {
        super.findElements();
        SpeakerDeviceViewHolder h = (SpeakerDeviceViewHolder) holder;

        h.progressBar = contextView.findViewById(R.id.progressBar);
        h.txtProgressHigh = contextView.findViewById(R.id.txtProgressHigh);
        h.txtProgressLow = contextView.findViewById(R.id.txtProgressLow);

        h.btnPlay = contextView.findViewById(R.id.btnPlay);
        h.btnPrevSong = contextView.findViewById(R.id.btnPrevSong);
        h.btnNextSong = contextView.findViewById(R.id.btnNextSong);

        h.btnClassical = contextView.findViewById(R.id.btnClassical);
        h.btnCountry = contextView.findViewById(R.id.btnCountry);
        h.btnDance = contextView.findViewById(R.id.btnDance);
        h.btnLatina = contextView.findViewById(R.id.btnLatina);
        h.btnPop = contextView.findViewById(R.id.btnPop);
        h.btnRock = contextView.findViewById(R.id.btnRock);

        h.txtVolume = contextView.findViewById(R.id.txtVolume);
        h.btnVolumeDown = contextView.findViewById(R.id.btnVolumeDown);
        h.btnVolumeUp = contextView.findViewById(R.id.btnVolumeUp);

    }

    @Override
    protected void setDescription(DeviceState state) {
        if (state != null) {
            SpeakerDeviceState s = (SpeakerDeviceState) state;
            SpeakerDeviceViewHolder h = (SpeakerDeviceViewHolder) holder;

            Log.v("INFO:", "Setting Description");

            String status = s.getStatus();
            String stateStatus;

            switch (status){
                case "stopped":
                    stateStatus = "status: " + contextView.getResources().getString(R.string.dev_speaker_state_stopped) + "\n";
                    break;
                case "playing":
                    stateStatus = "status: " + contextView.getResources().getString(R.string.dev_speaker_state_playing) + "\n";
                    break;
                case "paused":
                    stateStatus = "status: " + contextView.getResources().getString(R.string.dev_speaker_state_paused) + "\n";
                    break;
                default:
                    stateStatus = "Null \n";
                    break;
            }

            SpeakerSong stateSong = s.getSong();
            String auxDec = stateStatus;
            if(stateSong != null){
                String stateSongTitle = "Title: " + stateSong.getTitle() + "\n";
                String stateSongAlbum = "Album: " + stateSong.getAlbum() + "\n";
                String stateSongArtist = "Artist: " + stateSong.getArtist() + "\n";
                auxDec = auxDec + stateSongTitle + stateSongAlbum + stateSongArtist;
            }

            if (h.description != null){
                h.description.setText(auxDec);
            }
        }
    }

    @Override
    protected void setUI(DeviceState state) {
        super.setUI(state);
        if (state != null) {
            SpeakerDeviceState s = (SpeakerDeviceState) state;
            SpeakerDeviceViewHolder h = (SpeakerDeviceViewHolder) holder;

            SpeakerSong stateSong = s.getSong();
            if(stateSong != null){
                String stateSongDuration = stateSong.getDuration();
                String stateSongProgress = stateSong.getProgress();

            }

            String stateGenre = s.getGenre();
            if(h.btnLatina != null){
                clearSelections();
                switch (stateGenre){
                    case "Classical":
                        h.btnClassical.setChecked(true);
                        break;
                    case "cuntry":
                        h.btnCountry.setChecked(true);
                        break;
                    case "latina":
                        h.btnLatina.setChecked(true);
                        break;
                    case "dance":
                        h.btnDance.setChecked(true);
                        break;
                    case "pop":
                        h.btnPop.setChecked(true);
                        break;
                    case "rock":
                        h.btnRock.setChecked(true);
                        break;
                    default:
                        break;
                }
            }

            String stateVolume = s.getVolume();
            if(h.txtVolume != null){
                h.txtVolume.setText(stateVolume);
                volume = Integer.parseInt(s.getVolume());
            }

        }
    }

    @Override
    protected void attachFunctions() {
        super.attachFunctions();
        SpeakerDeviceViewHolder h = (SpeakerDeviceViewHolder) holder;

//        if (h.btnDispence != null) {
//            h.btnDispence.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    TapDevice d = (TapDevice) h.device;
//                    if (d != null) {
//                        TapDeviceState s = (TapDeviceState) d.getState();
//                        if (s != null) {
//                            amount = h.amount.getText().toString();
//                            if(!amount.equals("")) {
//                                String[] args = new String[2];
//                                args[0] = amount;
//                                args[1] = unit;
//                                Log.v("Dispensing", amount + unit);
//                                ApiClient.getInstance().executeDeviceAction(d.getId(), "dispense", args, new Callback<Result<Object>>() {
//                                    @Override
//                                    public void onResponse(@NonNull Call<Result<Object>> call, @NonNull Response<Result<Object>> response) {
//                                        if (response.isSuccessful()) {
//                                            Result<Object> result = response.body();
//
//                                            if (result != null) {
//                                                Boolean success = (Boolean) result.getResult();
//                                                if (success != null) {
//                                                    Log.v("ACTION_SUCCESS", success.toString());
//                                                    h.amount.setText("");
//                                                }
//                                            } else {
//                                                handleError(response);
//                                            }
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onFailure(@NonNull Call<Result<Object>> call, @NonNull Throwable t) {
//                                        handleUnexpectedError(t);
//                                    }
//                                });
//                            }
//                        }
//                    }
//                }
//            });
//        }
//
        if (h.btnClassical != null) {
            h.btnClassical.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearSelections();
                    h.btnClassical.setChecked(true);
                    genre = "Classical";
                    Log.v("GENRE", "Classical");
                }
            });
        }

        if (h.btnCountry != null) {
            h.btnCountry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearSelections();
                    h.btnCountry.setChecked(true);
                    genre = "country";
                    Log.v("GENRE", "Country");
                }
            });
        }

        if (h.btnDance != null) {
            h.btnDance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearSelections();
                    h.btnDance.setChecked(true);
                    genre = "dance";
                    Log.v("GENRE", "Dance");
                }
            });
        }

        if (h.btnLatina != null) {
            h.btnLatina.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearSelections();
                    h.btnLatina.setChecked(true);
                    genre = "latina";
                    Log.v("GENRE", "Latina");
                }
            });
        }

        if (h.btnPop != null) {
            h.btnPop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearSelections();
                    h.btnPop.setChecked(true);
                    genre = "pop";
                    Log.v("GENRE", "Pop");
                }
            });
        }

        if (h.btnRock != null) {
            h.btnRock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearSelections();
                    h.btnRock.setChecked(true);
                    genre = "rock";
                    Log.v("GENRE", "Rock");
                }
            });
        }

        if (h.btnVolumeUp != null) {
            h.btnVolumeUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SpeakerDevice d = (SpeakerDevice) h.device;
                    if (d != null && volume < 10) {
                        volume += 1;
                        Log.v("Volume", "Up");
                        String[] args = new String[1];
                        args[0] = volume.toString();
                        h.txtVolume.setText(volume.toString());
                        ApiClient.getInstance().executeDeviceAction(d.getId(), "setVolume", args, new Callback<Result<Object>>() {
                            @Override
                            public void onResponse(@NonNull Call<Result<Object>> call, @NonNull Response<Result<Object>> response) {
                                if (response.isSuccessful()) {
                                    Result<Object> result = response.body();
                                    if (result != null) {
                                        Object success = (Object) result.getResult();
                                        if (success != null) {
                                            Log.v("ACTION_SUCCESS", success.toString());
                                        }
                                    } else {
                                        handleError(response);
                                    }
                                }
                            }
                            @Override
                            public void onFailure(@NonNull Call<Result<Object>> call, @NonNull Throwable t) {
                                handleUnexpectedError(t);
                            }
                        });
                    }
                }
            });
        }
        if (h.btnVolumeDown != null) {
            h.btnVolumeDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SpeakerDevice d = (SpeakerDevice) h.device;
                    if (d != null && volume > 0) {
                        volume -= 1;
                        Log.v("Volume", "Down");
                        String[] args = new String[1];
                        args[0] = volume.toString();
                        h.txtVolume.setText(volume.toString());
                        ApiClient.getInstance().executeDeviceAction(d.getId(), "setVolume", args, new Callback<Result<Object>>() {
                            @Override
                            public void onResponse(@NonNull Call<Result<Object>> call, @NonNull Response<Result<Object>> response) {
                                if (response.isSuccessful()) {
                                    Result<Object> result = response.body();
                                    if (result != null) {
                                        Object success = (Object) result.getResult();
                                        if (success != null) {
                                            Log.v("ACTION_SUCCESS", success.toString());
                                        }
                                    } else {
                                        handleError(response);
                                    }
                                }
                            }
                            @Override
                            public void onFailure(@NonNull Call<Result<Object>> call, @NonNull Throwable t) {
                                handleUnexpectedError(t);
                            }
                        });
                    }
                }
            });
        }

    }

    @Override
    protected String getOnSwitchActionName(Boolean switchStatus) {
        return switchStatus?"play":"stop";
    }

    private void clearSelections(){
        SpeakerDeviceViewHolder h = (SpeakerDeviceViewHolder) holder;
        h.btnClassical.setChecked(false);
        h.btnCountry.setChecked(false);
        h.btnDance.setChecked(false);
        h.btnLatina.setChecked(false);
        h.btnPop.setChecked(false);
        h.btnRock.setChecked(false);
    }

}
