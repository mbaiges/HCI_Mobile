package ar.edu.itba.hci.uzr.intellifox.api.models.device;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceMeta that = (DeviceMeta) o;
        return Objects.equals(favourites, that.favourites);
    }

    @Override
    public int hashCode() {
        return Objects.hash(favourites);
    }
}
