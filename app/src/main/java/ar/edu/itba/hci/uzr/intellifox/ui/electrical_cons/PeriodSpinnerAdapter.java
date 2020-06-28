package ar.edu.itba.hci.uzr.intellifox.ui.electrical_cons;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.models.room.Room;

public class PeriodSpinnerAdapter extends ArrayAdapter<Integer> {

    private Context context;
    private Integer[] values;

    Map<Integer, Integer> properPeriodNames;

    LayoutInflater inflater;

    public PeriodSpinnerAdapter(Context context, int textViewResourceId,
                                Integer[] values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
        this.inflater = (LayoutInflater.from(context));

        if (properPeriodNames == null) {
            properPeriodNames = new HashMap<Integer, Integer>() {{
                put(1, R.string.electrical_cons_select_period_day);
                put(7, R.string.electrical_cons_select_period_week);
                put(30, R.string.electrical_cons_select_period_month);
                put(365, R.string.electrical_cons_select_period_year);
            }};
        }
    }

    @Override
    public int getCount(){
        return values.length;
    }

    @Override
    public Integer getItem(int position){
        return values[position];
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.electrical_cons_period_item, null);

        TextView label = convertView.findViewById(R.id.txtTitle);
        label.setTextColor(ContextCompat.getColor(context, R.color.text));
        Integer resource = properPeriodNames.get(values[position]);

        if (resource != null) {
            label.setText(resource);
        }

        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(ContextCompat.getColor(context, R.color.text));
        Integer resource = properPeriodNames.get(values[position]);

        if (resource != null) {
            label.setText(resource);
        }

        return label;
    }
}
