package ar.edu.itba.hci.uzr.intellifox.ui.devices.speaker;

import android.media.Image;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import ar.edu.itba.hci.uzr.intellifox.ui.devices.DeviceViewHolder;

public class SpeakerDeviceViewHolder extends DeviceViewHolder {
    ProgressBar progressBar;
    TextView txtProgressHigh;
    TextView txtProgressLow;
    TextView txtSongName;
    TextView txtSongGenre;
    TextView txtSongPlaylist;
    ImageButton btnPlay;
    ImageButton btnPrevSong;
    ImageButton btnNextSong;
    TextView txtVolume;
    ImageButton btnVolumeDown;
    ImageButton btnVolumeUp;
}
