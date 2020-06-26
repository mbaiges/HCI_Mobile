package ar.edu.itba.hci.uzr.intellifox.ui.devices.speaker;

import android.media.Image;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import ar.edu.itba.hci.uzr.intellifox.ui.devices.DeviceViewHolder;

public class SpeakerDeviceViewHolder extends DeviceViewHolder {

    ProgressBar progressBar;
    TextView txtProgressHigh;
    TextView txtProgressLow;

    ImageButton btnPlay;
    ImageButton btnPrevSong;
    ImageButton btnNextSong;

    ToggleButton btnClassical;
    ToggleButton btnCountry;
    ToggleButton btnDance;
    ToggleButton btnLatina;
    ToggleButton btnPop;
    ToggleButton btnRock;

    TextView txtVolume;
    ImageButton btnVolumeDown;
    ImageButton btnVolumeUp;

    Button btnShowPlaylist;

}
