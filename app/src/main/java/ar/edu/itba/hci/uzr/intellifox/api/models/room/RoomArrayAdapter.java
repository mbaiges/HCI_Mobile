package ar.edu.itba.hci.uzr.intellifox.api.models.room;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;

import java.util.HashMap;

import ar.edu.itba.hci.uzr.intellifox.api.models.room.Room;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.models.room.RoomViewHolder;

public class RoomArrayAdapter extends ArrayAdapter<Room> {

    static final String ROOM_ID_ARG = "room_id";

    static private HashMap<String, Integer> iconsConverter;

    public RoomArrayAdapter(Activity context, Room[] objects) {
        super(context, R.layout.room_card_item, objects);
        if (iconsConverter == null){
            iconsConverter = new HashMap<String, Integer>() {
                {
                    put("mdi-rhombus-split", R.drawable.ic_room_rhombus_split);
                    put("mdi-flower-tulip-outline", R.drawable.ic_menu_flower_tulip_outline);
                    put("mdi-shower", R.drawable.ic_room_bathroom);
                    put("mdi-bed-outline", R.drawable.ic_room_bed_outline);
                    put("mdi-table-chair", R.drawable.ic_room_table_chair);
                    put("mdi-silverware-fork-knife", R.drawable.ic_room_silverware_fork_knife);

                };
            };
        }
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        RoomViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.room_card_item, parent, false);
            holder = new RoomViewHolder();
            holder.card = convertView.findViewById(R.id.card);
            holder.imageView = convertView.findViewById(R.id.icon);
            holder.nameTextView = convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (RoomViewHolder) convertView.getTag();
        }

        Room room = getItem(position);
        if (room != null) {
            Bundle args = new Bundle();
            args.putString(ROOM_ID_ARG, room.getId());
            if (holder.card != null) {
                holder.card.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_room, args));
            }
            Integer ico = iconsConverter.get(room.getMeta().getIcon());
            if (ico != null) {
                holder.imageView.setImageResource(ico);
                holder.imageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.background1));
            }
            holder.nameTextView.setText(room.getName());
        }

        return convertView;
    }
}
