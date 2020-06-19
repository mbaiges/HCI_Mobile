package ar.edu.itba.hci.uzr.intellifox.ui.rooms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.List;


import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.models.room.Room;

import ar.edu.itba.hci.uzr.intellifox.api.models.room.RoomArrayAdapter;

public class RoomsFragment extends Fragment {

    RoomsViewModel roomsViewModel;
    GridView gridView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_rooms, container, false);

        roomsViewModel =
                ViewModelProviders.of(this).get(RoomsViewModel.class);

        gridView = root.findViewById(R.id.rooms_grid_view);

        roomsViewModel.getRooms().observe(getViewLifecycleOwner(), new Observer<List<Room>>() {
            @Override
            public void onChanged(@Nullable List<Room> rooms) {
                Room[] roomsArray = new Room[rooms.size()];
                int i = 0;
                for (Room r : rooms) {
                    roomsArray[i++] = r;
                }
                RoomArrayAdapter adapter = new RoomArrayAdapter(getActivity(), roomsArray);
                gridView.setAdapter(adapter);
            }
        });

        return root;
    }
}