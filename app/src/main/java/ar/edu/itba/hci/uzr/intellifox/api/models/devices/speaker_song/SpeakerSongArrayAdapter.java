package ar.edu.itba.hci.uzr.intellifox.api.models.devices.speaker_song;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Error;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.device_type.DeviceType;
import ar.edu.itba.hci.uzr.intellifox.api.models.routine_action.RoutineAction;
import ar.edu.itba.hci.uzr.intellifox.api.models.routine_action.RoutineActionDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.routine_action.RoutineActionViewHolder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpeakerSongArrayAdapter extends ArrayAdapter<SpeakerSong> {


    public SpeakerSongArrayAdapter(Context context, SpeakerSong[] objects) {
        super(context, R.layout.song_list_item, objects);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        SpeakerSongViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.song_list_item, parent, false);
            holder = new SpeakerSongViewHolder();
            holder.titleTextView = convertView.findViewById(R.id.txtTitleContent);
            holder.albumTextView = convertView.findViewById(R.id.txtAlbumContent);
            holder.artistTextView = convertView.findViewById(R.id.txtArtistContent);
            holder.durationTextView = convertView.findViewById(R.id.txtDurationContent);
            convertView.setTag(holder);
        } else {
            holder = (SpeakerSongViewHolder) convertView.getTag();
        }

        SpeakerSong speakerSong = getItem(position);
        if (speakerSong != null) {
            if (holder.titleTextView != null) {
                holder.titleTextView.setText(speakerSong.getArtist());
            }
            if (holder.albumTextView != null) {
                holder.albumTextView.setText(speakerSong.getAlbum());
            }
            if (holder.artistTextView != null) {
                holder.artistTextView.setText(speakerSong.getArtist());
            }
            if (holder.durationTextView != null) {
                holder.durationTextView.setText(speakerSong.getArtist());
            }
        }

        return convertView;
    }

}
