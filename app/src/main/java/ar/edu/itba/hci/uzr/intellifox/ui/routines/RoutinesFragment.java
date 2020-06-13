package ar.edu.itba.hci.uzr.intellifox.ui.routines;

import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import ar.edu.itba.hci.uzr.intellifox.R;

public class RoutinesFragment extends Fragment {

    ListView listView;
    ConstraintLayout expandableView;
    Button arrowBtn;
    CardView cardView;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setContentView(R.layout.device_card_item);
        View root = inflater.inflate(R.layout.fragment_routines, container, false);
        listView = root.findViewById(R.id.routines_list_view);

        // ---------------------------------------------------------

        expandableView = getActivity().findViewById(R.id.expandableView);
        arrowBtn = getActivity().findViewById(R.id.arrowBtn);
        cardView = getActivity().findViewById(R.id.cardView);

        arrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandableView.getVisibility()==View.GONE){
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expandableView.setVisibility(View.VISIBLE);
                    arrowBtn.setBackgroundResource(R.drawable.ic_chevron_up);
                } else {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    expandableView.setVisibility(View.GONE);
                    arrowBtn.setBackgroundResource(R.drawable.ic_chevron_down);
                }
            }
        });


        // ----------------------------------------------------------

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