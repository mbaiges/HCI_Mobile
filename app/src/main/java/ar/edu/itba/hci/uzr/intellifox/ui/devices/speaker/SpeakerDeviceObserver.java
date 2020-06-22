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
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.SpeakerDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.TapDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.TapDeviceState;
import ar.edu.itba.hci.uzr.intellifox.ui.devices.DeviceObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpeakerDeviceObserver extends DeviceObserver {

    public SpeakerDeviceObserver(View contextView) {
        super(contextView);
    }

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
        h.txtSongName = contextView.findViewById(R.id.txtSongName);
        h.txtSongGenre = contextView.findViewById(R.id.txtSongGenre);
        h.txtSongPlaylist = contextView.findViewById(R.id.txtSongPlaylist);
        h.btnPlay = contextView.findViewById(R.id.btnPlay);
        h.btnPrevSong = contextView.findViewById(R.id.btnPrevSong);
        h.btnNextSong = contextView.findViewById(R.id.btnNextSong);
        h.txtVolume = contextView.findViewById(R.id.txtVolume);
        h.btnVolumeDown = contextView.findViewById(R.id.btnVolumeDown);
        h.btnVolumeUp = contextView.findViewById(R.id.btnVolumeUp);
    }

    @Override
    protected void setDescription(DeviceState state) {
        if (state != null) {
            SpeakerDeviceState s = (SpeakerDeviceState) state;
            SpeakerDeviceViewHolder h = (SpeakerDeviceViewHolder) holder;

            String status = s.getStatus();

//            if (status != null) {
//                String closed = contextView.getResources().getString(R.string.dev_tap_state_closed);
//                String opened = contextView.getResources().getString(R.string.dev_tap_state_opened);
//                String aux = status.equals("closed") ? closed : opened ;
//                if (h.description != null){
//                    h.description.setText(aux);
//                }
//            }
        }
    }

    @Override
    protected void setUI(DeviceState state) {
        super.setUI(state);
        if (state != null) {
            SpeakerDeviceState s = (SpeakerDeviceState) state;
            SpeakerDeviceViewHolder h = (SpeakerDeviceViewHolder) holder;

//            if(h.btnL != null){
//                clearSelections();
//                h.btnL.setChecked(true);
//            }
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
//        if (h.btnML != null) {
//            h.btnML.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    clearSelections();
//                    h.btnML.setChecked(true);
//                    unit = "ml";
//                    Log.v("UNIT", "ml");
//                }
//            });
//        }
//
//        if (h.btnCL != null) {
//            h.btnCL.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    clearSelections();
//                    h.btnCL.setChecked(true);
//                    unit = "cl";
//                    Log.v("UNIT", "cl");
//                }
//            });
//        }
//
//        if (h.btnDL != null) {
//            h.btnDL.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    clearSelections();
//                    h.btnDL.setChecked(true);
//                    unit = "dl";
//                    Log.v("UNIT", "dl");
//                }
//            });
//        }
//
//        if (h.btnL != null) {
//            h.btnL.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    clearSelections();
//                    h.btnL.setChecked(true);
//                    unit = "l";
//                    Log.v("UNIT", "l");
//                }
//            });
//        }
//
//        if (h.btnDAL != null) {
//            h.btnDAL.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    clearSelections();
//                    h.btnDAL.setChecked(true);
//                    unit = "dal";
//                    Log.v("UNIT", "dal");
//                }
//            });
//        }
//
//        if (h.btnHL != null) {
//            h.btnHL.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    clearSelections();
//                    h.btnHL.setChecked(true);
//                    unit = "hl";
//                    Log.v("UNIT", "hl");
//                }
//            });
//        }
//
//        if (h.btnKL != null) {
//            h.btnKL.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    clearSelections();
//                    h.btnKL.setChecked(true);
//                    unit = "kl";
//                    Log.v("UNIT", "kl");
//                }
//            });
//        }

    }

    @Override
    protected String getOnSwitchActionName(Boolean switchStatus) {
        return switchStatus?"play":"stop";
    }

}
