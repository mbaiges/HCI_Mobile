package ar.edu.itba.hci.uzr.intellifox.ui.rooms;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import ar.edu.itba.hci.uzr.intellifox.R;

public class RoomArrayAdapter extends ArrayAdapter<Room> {
    public RoomArrayAdapter(Activity context, Room[] objects) {
        super(context, R.layout.room_card_item, objects);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.room_card_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.icon);
            holder.nameTextView = convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Room room = getItem(position);
        if (room != null) {
            holder.imageView.setImageResource(R.drawable.ic_room_rhombus_split);
            holder.imageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.background1));
            holder.nameTextView.setText(room.getName());
        }

        return convertView;
    }
}
