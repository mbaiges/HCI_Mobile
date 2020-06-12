package ar.edu.itba.hci.uzr.intellifox.ui.rooms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import ar.edu.itba.hci.uzr.intellifox.R;

public class RoomsFragment extends Fragment {

    GridView gridView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_rooms, container, false);
        gridView = root.findViewById(R.id.rooms_grid_view);

        Room[] values = new Room[]{
                new Room(1, "Mati's Room"),
                new Room(2, "Garden"),
                new Room(3, "Kitchen"),
                new Room(4, "Gabi's Playroom"),
                new Room(5, "Loundry Room")
        };
        RoomArrayAdapter adapter = new RoomArrayAdapter(this.getActivity(), values);

        gridView.setAdapter(adapter);
        return root;
    }
}