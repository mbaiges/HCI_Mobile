package ar.edu.itba.hci.uzr.intellifox.ui.help_and_feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import ar.edu.itba.hci.uzr.intellifox.R;

public class HelpAndFeedFragment extends Fragment {

    private HelpAndFeedViewModel helpAndFeedViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        helpAndFeedViewModel =
                ViewModelProviders.of(this).get(HelpAndFeedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_help_and_feed, container, false);
//        final TextView textView = root.findViewById(R.id.text_about_us);
//        helpAndFeedViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }
}