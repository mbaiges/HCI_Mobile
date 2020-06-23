package ar.edu.itba.hci.uzr.intellifox.ui.routine;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceArrayAdapter;
import ar.edu.itba.hci.uzr.intellifox.api.models.routine.Routine;
import ar.edu.itba.hci.uzr.intellifox.api.models.routine_action.RoutineAction;
import ar.edu.itba.hci.uzr.intellifox.api.models.routine_action.RoutineActionArrayAdapter;


public class RoutineFragment extends Fragment {

    static final String ROUTINE_ID_ARG = "routine_id";
    RoutineViewModel routineViewModel;
    View listView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_routine, container, false);
        listView = root.findViewById(R.id.routines_routine_detail_list);

        routineViewModel =
                ViewModelProviders.of(this).get(RoutineViewModel.class);

        Bundle bundle = this.getArguments();
        String routineId = null;
        if (bundle != null) {
            routineId = bundle.getString(ROUTINE_ID_ARG, null);
        }

        routineViewModel.getRoutine().observe(getViewLifecycleOwner(), new Observer<Routine>() {
            @Override
            public void onChanged(Routine routine) {
                if (routine != null) {
                    String routineName = routine.getName();
                    if (routineName != null) {
                        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(routineName);
                    }
                }
            }
        });

        if (routineId != null) {
            routineViewModel.getRoutine().observe(getViewLifecycleOwner(), new Observer<Routine>() {
                @Override
                public void onChanged(@Nullable Routine routine) {
                    if (routine != null) {
                        RoutineAction[] routineActions = routine.getActions();
                        if (routineActions != null) {
                            RoutineActionArrayAdapter adapter = new RoutineActionArrayAdapter(getActivity(), routineActions);
                            int orientation = getResources().getConfiguration().orientation;
                            /*
                            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                                // In landscape
                                ((GridView) listView).setAdapter(adapter);
                            } else {
                                // In portrait
                                ((ListView) listView).setAdapter(adapter);
                            }
                            */
                            ((ListView) listView).setAdapter(adapter);
                        }
                    }
                }
            });
            routineViewModel.init(routineId);
        }

        return root;
    }
}