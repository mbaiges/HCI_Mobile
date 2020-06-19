package ar.edu.itba.hci.uzr.intellifox.ui.routines;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceArrayAdapter;
import ar.edu.itba.hci.uzr.intellifox.api.models.routine.Routine;
import ar.edu.itba.hci.uzr.intellifox.api.models.routine.RoutineArrayAdapter;
import ar.edu.itba.hci.uzr.intellifox.ui.device_types.DeviceTypesDevicesViewModel;

public class RoutinesFragment extends Fragment {

    RoutinesViewModel routinesViewModel;
    ListView listView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_routines, container, false);
        listView = root.findViewById(R.id.routines_list_view);

        routinesViewModel =
                ViewModelProviders.of(this).get(RoutinesViewModel.class);

        routinesViewModel.getRoutines().observe(getViewLifecycleOwner(), new Observer<Set<Routine>>() {
            @Override
            public void onChanged(@Nullable Set<Routine> routines) {
                Routine[] routinesArray = new Routine[routines.size()];
                int i = 0;
                for (Routine r : routines) {
                    routinesArray[i++] = r;
                }
                RoutineArrayAdapter adapter = new RoutineArrayAdapter(getActivity(), routinesArray);
                listView.setAdapter(adapter);
            }
        });

        return root;
    }
}