package ar.edu.itba.hci.uzr.intellifox.api.models.routine;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import ar.edu.itba.hci.uzr.intellifox.R;

public class RoutineArrayAdapter extends ArrayAdapter<Routine> {
    public RoutineArrayAdapter(Activity context, Routine[] objects) {
        super(context, R.layout.device_card_item, objects);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        RoutineViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.routine_card_item, parent, false);
            holder = new RoutineViewHolder();
            holder.imageView = convertView.findViewById(R.id.icon);
            holder.nameTextView = convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (RoutineViewHolder) convertView.getTag();
        }

        Routine routine = getItem(position);
        if (routine != null) {
            ColorFilter filter = new PorterDuffColorFilter(Color.parseColor(routine.getMeta().getColor()), PorterDuff.Mode.SRC_IN);
            holder.imageView.setColorFilter(filter);
            holder.nameTextView.setText(routine.getName());
        }

        return convertView;
    }
}
