package ar.edu.itba.hci.uzr.intellifox.ui.routines;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import ar.edu.itba.hci.uzr.intellifox.R;

public class RoutinesFragment extends Fragment {

    ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_routines, container, false);
        listView = root.findViewById(R.id.routines_list_view);

        Routine[] values = new Routine[]{
                new Routine(1, "Open all doors", "#FFFFFF"),
                new Routine(2, "Turn on all lights", "#123456"),
                new Routine(3, "Overheat oven", "#555555"),
                new Routine(4, "Play cumbia in all speakers", "#EAEAEA"),
                new Routine(5, "Drop all the water", "#667788")
        };
        RoutineArrayAdapter adapter = new RoutineArrayAdapter(this.getActivity(), values);

        listView.setAdapter(adapter);
        return root;
    }
}