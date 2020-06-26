package ar.edu.itba.hci.uzr.intellifox.ui.devices.speaker;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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
    private boolean playing;

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

        h.btnShowPlaylist = contextView.findViewById(R.id.btnGetPlaylist);
        h.txtSongs = contextView.findViewById(R.id.txtSongs);

    }

    @Override
    protected void setDescription(DeviceState state) {
        if (state != null) {
            SpeakerDeviceState s = (SpeakerDeviceState) state;
            SpeakerDeviceViewHolder h = (SpeakerDeviceViewHolder) holder;

            //Log.v("INFO:", "Setting Description");

            String status = s.getStatus();
            String stateStatus;

            switch (status){
                case "stopped":
                    stateStatus =  contextView.getResources().getString(R.string.dev_speaker_state_stopped);
                    break;
                case "playing":
                    stateStatus =contextView.getResources().getString(R.string.dev_speaker_state_playing);
                    break;
                case "paused":
                    stateStatus = contextView.getResources().getString(R.string.dev_speaker_state_paused);
                    break;
                default:
                    stateStatus = "Null \n";
                    break;
            }

            SpeakerSong stateSong = s.getSong();


                String auxDec = "";
                if (h.icon != null) {  //si esta el icono
                    auxDec = stateStatus;
                } else {
                    if (stateSong != null) {
                        String stateSongTitle = contextView.getResources().getString(R.string.dev_speaker_stateName_title) + ": " + stateSong.getTitle() + "\n";
                        String stateSongAlbum = contextView.getResources().getString(R.string.dev_speaker_stateName_album) + ": " + stateSong.getAlbum() + "\n";
                        String stateSongArtist = contextView.getResources().getString(R.string.dev_speaker_stateName_artist) + ": " + stateSong.getArtist() + "\n";
                        auxDec = stateStatus + " - " + stateSongTitle + stateSongAlbum + stateSongArtist;
                    }
                    else
                        auxDec = stateStatus;
                }
                if (h.description != null) {
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

            String status = state.getStatus();
            if (status != null) {
                if (holder.onSwitch != null) {
                    holder.onSwitch.setChecked(status.equals("playing") || status.equals("paused"));
                }
            }


            if(h.btnPlay != null){
                playing = s.getStatus().equals("playing");
                if(playing){
                    h.btnPlay.setImageResource(R.drawable.ic_pause);
                }else{
                    h.btnPlay.setImageResource(R.drawable.ic_play);
                }
            }

            SpeakerSong stateSong = s.getSong();
            if(stateSong != null){
                String stateSongDuration = stateSong.getDuration();
                String stateSongProgress = stateSong.getProgress();
                Integer minsTotal = calculateEquivalentProgress(stateSongDuration);
                Integer minsPassed = calculateEquivalentProgress(stateSongProgress);
                //Log.v("PASSED", minsPassed.toString());
                //Log.v("TOTAL", minsTotal.toString());

                if(h.txtProgressLow != null){
                    h.progressBar.setMax(minsTotal);
                    h.txtProgressLow.setText(stateSongProgress);
                    h.txtProgressHigh.setText(stateSongDuration);
                    h.progressBar.setProgress(minsPassed);
                }
            }

            String stateGenre = s.getGenre();
            if(h.btnLatina != null){
                clearSelections();
                switch (stateGenre){
                    case "classical":
                        h.btnClassical.setChecked(true);
                        break;
                    case "country":
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

        if (h.btnPlay != null) {
            h.btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SpeakerDevice d = (SpeakerDevice) h.device;
                    String action;
                    if (d != null) {
                        if(playing){
                            action = "pause";
                        }else{
                            action = "resume";
                        }
                        //Log.v("SONG", "togglePlay");
                        ApiClient.getInstance().executeDeviceAction(d.getId(), action, new String[0], new Callback<Result<Object>>() {
                            @Override
                            public void onResponse(@NonNull Call<Result<Object>> call, @NonNull Response<Result<Object>> response) {
                                if (response.isSuccessful()) {
                                    Result<Object> result = response.body();
                                    if (result != null) {
                                        String text = contextView.getResources().getString(playing?R.string.notif_speaker_paused:R.string.notif_speaker_resumed)  + ".";
                                        Snackbar snackbar = Snackbar.make(contextView, text, Snackbar.LENGTH_SHORT);
                                        View sbView = snackbar.getView();
                                        sbView.setBackgroundColor(ContextCompat.getColor(contextView.getContext(), R.color.primary2));
                                        snackbar.show();
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

        if (h.btnPrevSong != null) {
            h.btnPrevSong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SpeakerDevice d = (SpeakerDevice) h.device;
                    if (d != null) {
                        //Log.v("SONG", "previous");
                        ApiClient.getInstance().executeDeviceAction(d.getId(), "previousSong", new String[0], new Callback<Result<Object>>() {
                            @Override
                            public void onResponse(@NonNull Call<Result<Object>> call, @NonNull Response<Result<Object>> response) {
                                if (response.isSuccessful()) {
                                    Result<Object> result = response.body();
                                    if (result != null) {
                                        String text = contextView.getResources().getString(R.string.notif_speaker_changed_song)  + ".";
                                        Snackbar snackbar = Snackbar.make(contextView, text, Snackbar.LENGTH_SHORT);
                                        View sbView = snackbar.getView();
                                        sbView.setBackgroundColor(ContextCompat.getColor(contextView.getContext(), R.color.primary2));
                                        snackbar.show();
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

        if (h.btnNextSong != null) {
            h.btnNextSong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SpeakerDevice d = (SpeakerDevice) h.device;
                    if (d != null) {
                        //Log.v("SONG", "next");
                        ApiClient.getInstance().executeDeviceAction(d.getId(), "nextSong", new String[0], new Callback<Result<Object>>() {
                            @Override
                            public void onResponse(@NonNull Call<Result<Object>> call, @NonNull Response<Result<Object>> response) {
                                if (response.isSuccessful()) {
                                    Result<Object> result = response.body();
                                    if (result != null) {
                                        String text = contextView.getResources().getString(R.string.notif_speaker_changed_song)  + ".";
                                        Snackbar snackbar = Snackbar.make(contextView, text, Snackbar.LENGTH_SHORT);
                                        View sbView = snackbar.getView();
                                        sbView.setBackgroundColor(ContextCompat.getColor(contextView.getContext(), R.color.primary2));
                                        snackbar.show();
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

        if (h.btnClassical != null) {
            h.btnClassical.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SpeakerDevice d = (SpeakerDevice) h.device;
                    if (d != null) {
                        clearSelections();
                        genre = "classical";
                        //Log.v("GENRE", "Classical");
                        String[] args = new String[1];
                        args[0] = genre;
                        h.btnClassical.setChecked(true);

                        ApiClient.getInstance().executeDeviceAction(d.getId(), "setGenre", args, new Callback<Result<Object>>() {
                            @Override
                            public void onResponse(@NonNull Call<Result<Object>> call, @NonNull Response<Result<Object>> response) {
                                if (response.isSuccessful()) {
                                    Result<Object> result = response.body();
                                    if (result != null) {
                                        String text = contextView.getResources().getString(R.string.notif_speaker_changed_genre)  + " " + contextView.getResources().getString(R.string.dev_speaker_genre_clasical) + ".";
                                        Snackbar snackbar = Snackbar.make(contextView, text, Snackbar.LENGTH_SHORT);
                                        View sbView = snackbar.getView();
                                        sbView.setBackgroundColor(ContextCompat.getColor(contextView.getContext(), R.color.primary2));
                                        snackbar.show();
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

        if (h.btnCountry != null) {
            h.btnCountry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SpeakerDevice d = (SpeakerDevice) h.device;
                    if (d != null) {
                        clearSelections();
                        genre = "country";
                        //Log.v("GENRE", "Country");
                        String[] args = new String[1];
                        args[0] = genre;
                        h.btnCountry.setChecked(true);

                        ApiClient.getInstance().executeDeviceAction(d.getId(), "setGenre", args, new Callback<Result<Object>>() {
                            @Override
                            public void onResponse(@NonNull Call<Result<Object>> call, @NonNull Response<Result<Object>> response) {
                                if (response.isSuccessful()) {
                                    Result<Object> result = response.body();
                                    if (result != null) {
                                        String text = contextView.getResources().getString(R.string.notif_speaker_changed_genre)  + " " + contextView.getResources().getString(R.string.dev_speaker_genre_country) + ".";
                                        Snackbar snackbar = Snackbar.make(contextView, text, Snackbar.LENGTH_SHORT);
                                        View sbView = snackbar.getView();
                                        sbView.setBackgroundColor(ContextCompat.getColor(contextView.getContext(), R.color.primary2));
                                        snackbar.show();
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

        if (h.btnDance != null) {
            h.btnDance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SpeakerDevice d = (SpeakerDevice) h.device;
                    if (d != null) {
                        clearSelections();
                        genre = "dance";
                        //Log.v("GENRE", "Dance");
                        String[] args = new String[1];
                        args[0] = genre;
                        h.btnDance.setChecked(true);

                        ApiClient.getInstance().executeDeviceAction(d.getId(), "setGenre", args, new Callback<Result<Object>>() {
                            @Override
                            public void onResponse(@NonNull Call<Result<Object>> call, @NonNull Response<Result<Object>> response) {
                                if (response.isSuccessful()) {
                                    Result<Object> result = response.body();
                                    if (result != null) {
                                        String text = contextView.getResources().getString(R.string.notif_speaker_changed_genre)  + " " + contextView.getResources().getString(R.string.dev_speaker_genre_dance) + ".";
                                        Snackbar snackbar = Snackbar.make(contextView, text, Snackbar.LENGTH_SHORT);
                                        View sbView = snackbar.getView();
                                        sbView.setBackgroundColor(ContextCompat.getColor(contextView.getContext(), R.color.primary2));
                                        snackbar.show();
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

        if (h.btnLatina != null) {
            h.btnLatina.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SpeakerDevice d = (SpeakerDevice) h.device;
                    if (d != null) {
                        clearSelections();
                        genre = "latina";
                        //Log.v("GENRE", "Latina");
                        String[] args = new String[1];
                        args[0] = genre;
                        h.btnLatina.setChecked(true);

                        ApiClient.getInstance().executeDeviceAction(d.getId(), "setGenre", args, new Callback<Result<Object>>() {
                            @Override
                            public void onResponse(@NonNull Call<Result<Object>> call, @NonNull Response<Result<Object>> response) {
                                if (response.isSuccessful()) {
                                    Result<Object> result = response.body();
                                    if (result != null) {
                                        String text = contextView.getResources().getString(R.string.notif_speaker_changed_genre)  + " " + contextView.getResources().getString(R.string.dev_speaker_genre_latina) + ".";
                                        Snackbar snackbar = Snackbar.make(contextView, text, Snackbar.LENGTH_SHORT);
                                        View sbView = snackbar.getView();
                                        sbView.setBackgroundColor(ContextCompat.getColor(contextView.getContext(), R.color.primary2));
                                        snackbar.show();
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

        if (h.btnPop != null) {
            h.btnPop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SpeakerDevice d = (SpeakerDevice) h.device;
                    if (d != null) {
                        clearSelections();
                        genre = "pop";
                        //Log.v("GENRE", "Pop");
                        String[] args = new String[1];
                        args[0] = genre;
                        h.btnPop.setChecked(true);

                        ApiClient.getInstance().executeDeviceAction(d.getId(), "setGenre", args, new Callback<Result<Object>>() {
                            @Override
                            public void onResponse(@NonNull Call<Result<Object>> call, @NonNull Response<Result<Object>> response) {
                                if (response.isSuccessful()) {
                                    Result<Object> result = response.body();
                                    if (result != null) {
                                        String text = contextView.getResources().getString(R.string.notif_speaker_changed_genre)  + " " + contextView.getResources().getString(R.string.dev_speaker_genre_pop) + ".";
                                        Snackbar snackbar = Snackbar.make(contextView, text, Snackbar.LENGTH_SHORT);
                                        View sbView = snackbar.getView();
                                        sbView.setBackgroundColor(ContextCompat.getColor(contextView.getContext(), R.color.primary2));
                                        snackbar.show();
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

        if (h.btnRock != null) {
            h.btnRock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SpeakerDevice d = (SpeakerDevice) h.device;
                    if (d != null) {

                        clearSelections();
                        genre = "rock";
                        //Log.v("GENRE", "Rock");
                        String[] args = new String[1];
                        args[0] = genre;
                        h.btnRock.setChecked(true);

                        ApiClient.getInstance().executeDeviceAction(d.getId(), "setGenre", args, new Callback<Result<Object>>() {
                            @Override
                            public void onResponse(@NonNull Call<Result<Object>> call, @NonNull Response<Result<Object>> response) {
                                if (response.isSuccessful()) {
                                    Result<Object> result = response.body();
                                    if (result != null) {
                                        String text = contextView.getResources().getString(R.string.notif_speaker_changed_genre)  + " " + contextView.getResources().getString(R.string.dev_speaker_genre_rock) + ".";
                                        Snackbar snackbar = Snackbar.make(contextView, text, Snackbar.LENGTH_SHORT);
                                        View sbView = snackbar.getView();
                                        sbView.setBackgroundColor(ContextCompat.getColor(contextView.getContext(), R.color.primary2));
                                        snackbar.show();
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

        if (h.btnVolumeUp != null) {
            h.btnVolumeUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SpeakerDevice d = (SpeakerDevice) h.device;
                    if (d != null && volume < 10) {
                        volume += 1;
                        //Log.v("Volume", "Up");
                        String[] args = new String[1];
                        args[0] = volume.toString();
                        h.txtVolume.setText(volume.toString());
                        ApiClient.getInstance().executeDeviceAction(d.getId(), "setVolume", args, new Callback<Result<Object>>() {
                            @Override
                            public void onResponse(@NonNull Call<Result<Object>> call, @NonNull Response<Result<Object>> response) {
                                if (response.isSuccessful()) {
                                    Result<Object> result = response.body();
                                    if (result != null) {
                                        String text = contextView.getResources().getString(R.string.notif_speaker_changed_volume) + ".";
                                        Snackbar snackbar = Snackbar.make(contextView, text, Snackbar.LENGTH_SHORT);
                                        View sbView = snackbar.getView();
                                        sbView.setBackgroundColor(ContextCompat.getColor(contextView.getContext(), R.color.primary2));
                                        snackbar.show();
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
                        //Log.v("Volume", "Down");
                        String[] args = new String[1];
                        args[0] = volume.toString();
                        h.txtVolume.setText(volume.toString());
                        ApiClient.getInstance().executeDeviceAction(d.getId(), "setVolume", args, new Callback<Result<Object>>() {
                            @Override
                            public void onResponse(@NonNull Call<Result<Object>> call, @NonNull Response<Result<Object>> response) {
                                if (response.isSuccessful()) {
                                    Result<Object> result = response.body();
                                    if (result != null) {
                                        String text = contextView.getResources().getString(R.string.notif_speaker_changed_volume) + ".";
                                        Snackbar snackbar = Snackbar.make(contextView, text, Snackbar.LENGTH_SHORT);
                                        View sbView = snackbar.getView();
                                        sbView.setBackgroundColor(ContextCompat.getColor(contextView.getContext(), R.color.primary2));
                                        snackbar.show();
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


        if (h.btnShowPlaylist != null) {
            h.btnShowPlaylist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SpeakerDevice d = (SpeakerDevice) h.device;
                    if (d != null && volume > 0) {

                        ApiClient.getInstance().executeGetSpeakerPlaylist(d.getId(), new String[0], new Callback<Result<List<SpeakerSong>>>() {
                            @Override
                            public void onResponse(@NonNull Call<Result<List<SpeakerSong>>> call, @NonNull Response<Result<List<SpeakerSong>>> response) {
                                if (response.isSuccessful()) {
                                    Result<List<SpeakerSong>> result = response.body();
                                    if (result != null) {
                                        //LinkedTreeMap<SpeakerSong> songs = (LinkedTreeMap<SpeakerSong>)result.getResult();

                                        Log.v("HOLA", result.getResult().toString());

                                        List<SpeakerSong> songList = result.getResult();

                                        String listString = "";
                                        for (SpeakerSong s : songList)
                                        {
                                            String aux = s.toString();
                                            listString += s + "\n";
                                        }

                                        Log.v("SONGS", listString);

                                        h.txtSongs.setText(listString);

                                    } else {
                                        handleError(response);
                                    }
                                }
                            }
                            @Override
                            public void onFailure(@NonNull Call<Result<List<SpeakerSong>>> call, @NonNull Throwable t) {
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

    private Integer calculateEquivalentProgress(String TimeMark){
        String[] time = TimeMark.split(":");
        Integer mins = Integer.parseInt(time[0]);
        Integer secs = Integer.parseInt(time[1]);
        return mins * 60 + mins;
    }

}
