package ar.edu.itba.hci.uzr.intellifox.api.models.device;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.models.routine.Routine;

public class DeviceArrayAdapter extends ArrayAdapter<Routine> {
    public DeviceArrayAdapter(Activity context, Routine[] objects) {
        super(context, R.layout.device_card_item, objects);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        DeviceViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.device_card_item, parent, false);
            holder = new DeviceViewHolder();
            holder.imageView = convertView.findViewById(R.id.icon);
            holder.nameTextView = convertView.findViewById(R.id.name);
            final ConstraintLayout expandableView = convertView.findViewById(R.id.expandableView);
            final Button arrowBtn = convertView.findViewById(R.id.arrowBtn);
            final CardView cardView = convertView.findViewById(R.id.cardView);
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
            holder.expandableView = expandableView;
            holder.arrowBtn = arrowBtn;
            holder.cardView = cardView;
            convertView.setTag(holder);
        } else {
            holder = (DeviceViewHolder) convertView.getTag();
        }

        Routine routine = getItem(position);
        if (routine != null) {
            ColorFilter filter = new PorterDuffColorFilter(Color.parseColor(routine.getColor()), PorterDuff.Mode.SRC_IN);
            holder.imageView.setColorFilter(filter);
            holder.nameTextView.setText(routine.getName());
        }

        return convertView;
    }
}
