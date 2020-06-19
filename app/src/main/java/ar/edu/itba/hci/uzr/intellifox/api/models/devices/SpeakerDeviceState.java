package ar.edu.itba.hci.uzr.intellifox.api.models.devices;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceState;

public class SpeakerDeviceState extends DeviceState {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("volume")
    @Expose
    private String volume;
    @SerializedName("genre")
    @Expose
    private String genre;
    @SerializedName("song")
    @Expose
    private SpeakerSong song;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public SpeakerSong getSong() {
        return song;
    }

    public void setSong(SpeakerSong song) {
        this.song = song;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpeakerDeviceState that = (SpeakerDeviceState) o;
        return Objects.equals(status, that.status) &&
                Objects.equals(volume, that.volume) &&
                Objects.equals(genre, that.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, volume, genre, song);
    }
}
