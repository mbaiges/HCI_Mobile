package ar.edu.itba.hci.uzr.intellifox.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.Locale;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.settings.SharedPreferencesSetting;

public class SettingsFragment extends Fragment {

    private Switch nightModeSwitch;
    private EditText editText;
    private Button editButton;
    private Switch languageSwitch;
    public static final String MyPREFERENCES = "nightModePrefs";
    public static final String KEY_ISNIGHTMODE = "isNightMode";

    SharedPreferences sharedPreferences;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        sharedPreferences = SharedPreferencesSetting.getInstance();

        nightModeSwitch = root.findViewById(R.id.nightModeSwitch);
        nightModeSwitch.setChecked(sharedPreferences.getBoolean(KEY_ISNIGHTMODE, true));
        nightModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if(isChecked){
                    //Log.d("THEME", "Is going to be dark");
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }else{
                    //Log.d("THEME", "Is going to be white");
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                SaveNightModeState(isChecked);
            }
        });

        editText = root.findViewById(R.id.editTextTextPersonName);
        if (editText != null) {
            editButton = root.findViewById(R.id.button2);
            if (editButton != null) {
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newAddress = editText.getText().toString();
                        Log.d("ADDRESS_CHANGED", newAddress);
                        ApiClient.getInstance().setBaseURL(newAddress);
                    }
                });
            }
        }


        return root;
    }

    private void SaveNightModeState(boolean nightMode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_ISNIGHTMODE, nightMode);
        editor.apply();
    }


}