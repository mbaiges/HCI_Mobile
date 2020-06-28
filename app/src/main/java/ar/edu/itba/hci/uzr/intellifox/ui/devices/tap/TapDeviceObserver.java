package ar.edu.itba.hci.uzr.intellifox.ui.devices.tap;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.TapDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.TapDeviceState;
import ar.edu.itba.hci.uzr.intellifox.ui.devices.DeviceObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TapDeviceObserver extends DeviceObserver {

    private String amount = "1";
    private String unit = "l";
    private TapDeviceState mystate;

    public TapDeviceObserver(View contextView) {
        super(contextView);
    }

    @Override
    protected void createHolder() {
        this.holder = new TapDeviceViewHolder();
    }

    @Override
    protected void findElements() {
        super.findElements();
        TapDeviceViewHolder h = (TapDeviceViewHolder) holder;

        h.btnML = contextView.findViewById(R.id.btn0);
        h.btnCL = contextView.findViewById(R.id.btn1);
        h.btnDL = contextView.findViewById(R.id.btn2);
        h.btnL = contextView.findViewById(R.id.btn3);
        h.btnDAL = contextView.findViewById(R.id.btn4);
        h.btnHL = contextView.findViewById(R.id.btn5);
        h.btnKL = contextView.findViewById(R.id.btn6);
        h.amount = contextView.findViewById(R.id.howMany);
        h.btnDispence = contextView.findViewById(R.id.btnDispense);
        h.progBarDispensing = contextView.findViewById(R.id.progBarDispensing);
        h.txtDispensing = contextView.findViewById(R.id.txtDispensing);
        h.progBarLoading = contextView.findViewById(R.id.progBarLoading);
    }

    @Override
    protected void setDescription(DeviceState state) {
        if (state != null) {
            TapDeviceState s = (TapDeviceState) state;
            TapDeviceViewHolder h = (TapDeviceViewHolder) holder;

            String status = s.getStatus();


            if (status != null) {
                String closed = contextView.getResources().getString(R.string.dev_tap_state_closed);
                String opened = contextView.getResources().getString(R.string.dev_tap_state_opened);
                String aux = status.equals("closed") ? closed : opened ;
                if (h.description != null){
                    h.description.setText(aux);
                }
            }
        }
    }

    @Override
    protected void setUI(DeviceState state) {
        super.setUI(state);

        mystate = (TapDeviceState) state;

        if (state != null) {
            TapDeviceState s = (TapDeviceState) state;
            TapDeviceViewHolder h = (TapDeviceViewHolder) holder;


            if (state.getStatus().equals("closed")) {
                h.onSwitch.setChecked(false);
            } else {
                h.onSwitch.setChecked(true);
            }

            //Log.v("TapStatus", s.toString());

            if (h.txtDispensing != null && h.progBarLoading != null && h.progBarDispensing != null && h.btnDispence != null) {

                if (s.getQuantity() != null) {
                    h.txtDispensing.setVisibility(View.VISIBLE);
                    h.progBarLoading.setVisibility(View.VISIBLE);
                    h.progBarDispensing.setVisibility(View.VISIBLE);
                    h.btnDispence.setEnabled(false);
                    h.btnDispence.setAlpha(.5f);
                    h.btnDispence.setText(contextView.getResources().getString(R.string.dev_tap_button_dispense_off));
                    h.progBarDispensing.setMax(Integer.parseInt(s.getQuantity()) * 100);
                    h.progBarDispensing.setProgress((int) (s.getDispensedQuantity() * 100));
                    //Log.v("PROGRESS", String.valueOf((int)(s.getDispensedQuantity()*100)));
                } else {
                    h.txtDispensing.setVisibility(View.INVISIBLE);
                    h.progBarDispensing.setVisibility(View.INVISIBLE);
                    h.progBarLoading.setVisibility(View.INVISIBLE);
                    h.btnDispence.setEnabled(true);
                    h.btnDispence.setAlpha(1);
                    h.btnDispence.setText(contextView.getResources().getString(R.string.dev_tap_button_dispense));
                }
            }

            if (h.btnL != null) {
                clearSelections();
                h.btnL.setChecked(true);
            }

            if (h.amount != null) {
                if (!state.getStatus().equals("closed") || h.amount.getText().toString().equals("")) {
                    Log.v("TAP", "switch:" + state.getStatus().equals("closed") + ", ammount:" + h.amount.getText().toString());
                    h.btnDispence.setEnabled(false);
                    h.btnDispence.setAlpha(.5f);
                } else {
                    Log.v("TAP", "switch:" + state.getStatus().equals("closed") + ", ammount:" + h.amount.getText().toString());
                    h.btnDispence.setEnabled(true);
                    h.btnDispence.setAlpha(1);
                }
            }
        }
    }

    @Override
    protected void attachFunctions() {
        super.attachFunctions();
        TapDeviceViewHolder h = (TapDeviceViewHolder) holder;


        if(h.amount != null) {
            h.amount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if( !mystate.getStatus().equals("closed") || h.amount.getText().toString().equals("") ){
                        Log.v("TAP", "switch:" + mystate.getStatus().equals("closed") + ", ammount:" + h.amount.getText().toString());
                        h.btnDispence.setEnabled(false);
                        h.btnDispence.setAlpha(.5f);
                    }else{
                        Log.v("TAP", "switch:" + mystate.getStatus().equals("closed") + ", ammount:" + h.amount.getText().toString());
                        h.btnDispence.setEnabled(true);
                        h.btnDispence.setAlpha(1);
                    }
                }
            });
        }



        if (h.btnDispence != null) {
            h.btnDispence.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    h.txtDispensing.setVisibility(View.VISIBLE);
                    h.progBarLoading.setVisibility(View.VISIBLE);
                    h.progBarDispensing.setVisibility(View.VISIBLE);
                    h.btnDispence.setEnabled(false);
                    h.btnDispence.setAlpha(.5f);
                    h.btnDispence.setText(contextView.getResources().getString(R.string.dev_tap_button_dispense_off));

                    TapDevice d = (TapDevice) h.device;
                    if (d != null) {
                        TapDeviceState s = (TapDeviceState) d.getState();
                        if (s != null) {
                            amount = h.amount.getText().toString();
                            if(!amount.equals("")) {
                                String[] args = new String[2];
                                args[0] = amount;
                                args[1] = unit;
                                //Log.v("Dispensing", amount + unit);
                                ApiClient.getInstance().executeDeviceAction(d.getId(), "dispense", args, new Callback<Result<Object>>() {
                                    @Override
                                    public void onResponse(@NonNull Call<Result<Object>> call, @NonNull Response<Result<Object>> response) {
                                        if (response.isSuccessful()) {
                                            Result<Object> result = response.body();
                                            if (result != null) {
                                                String text = contextView.getResources().getString(R.string.notif_tap_dispensing) + ".";
                                                Snackbar snackbar = Snackbar.make(contextView, text, Snackbar.LENGTH_SHORT);
                                                View sbView = snackbar.getView();
                                                sbView.setBackgroundColor(ContextCompat.getColor(contextView.getContext(), R.color.primary2));
                                                snackbar.show();
                                                h.amount.setText("");
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
                    }
                }
            });
        }

        if (h.btnML != null) {
            h.btnML.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearSelections();
                    h.btnML.setChecked(true);
                    unit = "ml";
                    //Log.v("UNIT", "ml");
                }
            });
        }

        if (h.btnCL != null) {
            h.btnCL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearSelections();
                    h.btnCL.setChecked(true);
                    unit = "cl";
                    //Log.v("UNIT", "cl");
                }
            });
        }

        if (h.btnDL != null) {
            h.btnDL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearSelections();
                    h.btnDL.setChecked(true);
                    unit = "dl";
                    //Log.v("UNIT", "dl");
                }
            });
        }

        if (h.btnL != null) {
            h.btnL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearSelections();
                    h.btnL.setChecked(true);
                    unit = "l";
                    //Log.v("UNIT", "l");
                }
            });
        }

        if (h.btnDAL != null) {
            h.btnDAL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearSelections();
                    h.btnDAL.setChecked(true);
                    unit = "dal";
                    //Log.v("UNIT", "dal");
                }
            });
        }

        if (h.btnHL != null) {
            h.btnHL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearSelections();
                    h.btnHL.setChecked(true);
                    unit = "hl";
                    //Log.v("UNIT", "hl");
                }
            });
        }

        if (h.btnKL != null) {
            h.btnKL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearSelections();
                    h.btnKL.setChecked(true);
                    unit = "kl";
                    //Log.v("UNIT", "kl");
                }
            });
        }

    }

    @Override
    protected String getOnSwitchActionName(Boolean switchStatus) {
        return switchStatus?"open":"close";
    }

    private void clearSelections(){
        TapDeviceViewHolder h = (TapDeviceViewHolder) holder;
        h.btnML.setChecked(false);
        h.btnCL.setChecked(false);
        h.btnDL.setChecked(false);
        h.btnL.setChecked(false);
        h.btnDAL.setChecked(false);
        h.btnHL.setChecked(false);
        h.btnKL.setChecked(false);
    }
}
