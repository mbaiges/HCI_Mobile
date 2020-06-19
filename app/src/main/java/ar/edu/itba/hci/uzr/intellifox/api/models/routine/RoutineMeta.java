package ar.edu.itba.hci.uzr.intellifox.api.models.routine;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class RoutineMeta {
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("favourites")
    @Expose
    private Boolean favourites;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean getFavourites() {
        return favourites;
    }

    public void setFavourites(Boolean favourites) {
        this.favourites = favourites;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoutineMeta that = (RoutineMeta) o;
        return Objects.equals(color, that.color) &&
                Objects.equals(favourites, that.favourites);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, favourites);
    }
}
