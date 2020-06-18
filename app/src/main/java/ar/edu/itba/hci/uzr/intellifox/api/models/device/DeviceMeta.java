package ar.edu.itba.hci.uzr.intellifox.api.models.device;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceMeta {

    @SerializedName("favourites")
    @Expose
    private Boolean favourites;

    public DeviceMeta(Boolean favourites) {
        this.favourites = favourites;
    }

    public Boolean getFavourites() {
        return favourites;
    }

    public void setFavourites(Boolean favourites) {
        this.favourites = favourites;
    }

    @Override
    public String toString() {
        return "DeviceMeta{" +
                "favourites=" + favourites +
                '}';
    }
}
