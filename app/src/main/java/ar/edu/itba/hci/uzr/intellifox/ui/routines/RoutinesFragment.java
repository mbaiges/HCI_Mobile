package ar.edu.itba.hci.uzr.intellifox.ui.routines;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.Set;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.models.routine.Routine;
import ar.edu.itba.hci.uzr.intellifox.api.models.routine.RoutineArrayAdapter;

public class RoutinesFragment extends Fragment {

    RoutinesViewModel routinesViewModel;
    View listView;
    TextView noRoutinesText;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_routines, container, false);
        listView = root.findViewById(R.id.routines_list_view);

        routinesViewModel = ViewModelProviders.of(this).get(RoutinesViewModel.class);

        noRoutinesText = root.findViewById(R.id.routines_empty_routines_text);

        routinesViewModel.getRoutines().observe(getViewLifecycleOwner(), new Observer<Set<Routine>>() {
            @Override
            public void onChanged(@Nullable Set<Routine> routines) {
                if (routines != null) {
                    if(noRoutinesText != null){
                        noRoutinesText.setVisibility((routines.size() > 0)?View.INVISIBLE : View.VISIBLE);
                    }
                    Routine[] routinesArray = new Routine[routines.size()];
                    int i = 0;
                    for (Routine r : routines) {
                        routinesArray[i++] = r;
                    }
                    RoutineArrayAdapter adapter = new RoutineArrayAdapter(getActivity(), root, routinesArray);
                    int orientation = getResources().getConfiguration().orientation;
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        // In landscape
                        ((GridView) listView).setAdapter(adapter);
                    } else {
                        // In portrait
                        ((ListView) listView).setAdapter(adapter);
                    }
                }
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        routinesViewModel.scheduleFetching();
    }

    @Override
    public void onPause() {
        super.onPause();
        routinesViewModel.stopFetching();
    }
}