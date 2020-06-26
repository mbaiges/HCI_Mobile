package ar.edu.itba.hci.uzr.intellifox.ui.favourites;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.Set;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.Device;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.DeviceArrayAdapter;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.MinimumComparableDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.routine.Routine;
import ar.edu.itba.hci.uzr.intellifox.api.models.routine.RoutineArrayAdapter;


public class FavouritesFragment extends Fragment {

    FavouritesViewModel favouritesViewModel;
    ListView devicesListView, routinesListView;

    TextView noFavouritesDevicesText;
    TextView noFavouritesRoutinesText;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_favourites, container, false);
        devicesListView = root.findViewById(R.id.favourite_devices_list_view);
        routinesListView = root.findViewById(R.id.favourite_routines_list_view);

        //Log.v("DEVICES_LIST_VIEW",(devicesListView != null)?"Found":"Not Found");
        //Log.v("ROUTINES_LIST_VIEW",(routinesListView != null)?"Found":"Not Found");

        favouritesViewModel = ViewModelProviders.of(this).get(FavouritesViewModel.class);

        noFavouritesDevicesText = root.findViewById(R.id.favourites_empty_devices_text);
        noFavouritesRoutinesText = root.findViewById(R.id.favourites_empty_routines_text);

        favouritesViewModel.getDevices().observe(getViewLifecycleOwner(), new Observer<Set<MinimumComparableDevice>>() {
            @Override
            public void onChanged(@Nullable Set<MinimumComparableDevice> minimumComparableDevices) {
                if (minimumComparableDevices != null) {
                    Device[] devicesArray = new Device[minimumComparableDevices.size()];
                    int i = 0;
                    for (MinimumComparableDevice mcd : minimumComparableDevices) {
                        devicesArray[i++] = mcd.getDevice();
                    }

                    if (noFavouritesDevicesText != null) {
                        noFavouritesDevicesText.setVisibility((devicesArray.length > 0)?View.INVISIBLE : View.VISIBLE);
                    }

                    DeviceArrayAdapter adapter = new DeviceArrayAdapter(getActivity(), devicesArray);
                    devicesListView.setAdapter(adapter);
                }
            }
        });

        favouritesViewModel.getRoutines().observe(getViewLifecycleOwner(), new Observer<Set<Routine>>() {
            @Override
            public void onChanged(@Nullable Set<Routine> routines) {
                if (routines != null && routinesListView != null) {
                    Routine[] routinesArray = new Routine[routines.size()];
                    int i = 0;
                    for (Routine r : routines) {
                        routinesArray[i++] = r;
                    }

                    if (noFavouritesRoutinesText != null) {
                        noFavouritesRoutinesText.setVisibility((routinesArray.length > 0)?View.INVISIBLE : View.VISIBLE);
                    }

                    RoutineArrayAdapter adapter = new RoutineArrayAdapter(getActivity(), root ,routinesArray);
                    routinesListView.setAdapter(adapter);
                }
            }
        });

        /*
        favouritesViewModel.getRoutines().observe(getViewLifecycleOwner(), new Observer<Set<Routine>>() {
            @Override
            public void onChanged(@Nullable Set<Routine> routines) {
                if (routines != null && routinesListView != null) {
                    ListAdapter a = routinesListView.getAdapter();
                    if (routinesListView.getAdapter() == null) {
                        Routine[] routinesArray = new Routine[routines.size()];
                        int i = 0;
                        for (Routine r : routines) {
                            routinesArray[i++] = r;
                        }
                        RoutineArrayAdapter adapter = new RoutineArrayAdapter(getActivity(), routinesArray);
                        routinesListView.setAdapter(adapter);
                    }
                    else {
                        int i = 0;
                    }
                }
            }
        });
*/

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        favouritesViewModel.scheduleFetching();
    }

    @Override
    public void onPause() {
        super.onPause();
        favouritesViewModel.stopFetching();
    }
}