package ar.edu.itba.hci.uzr.intellifox.ui.electrical_cons;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import ar.edu.itba.hci.uzr.intellifox.R;
import ar.edu.itba.hci.uzr.intellifox.api.ApiClient;
import ar.edu.itba.hci.uzr.intellifox.api.Result;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.log.DeviceLogRecord;
import ar.edu.itba.hci.uzr.intellifox.api.models.device.log.DeviceLogRecordArrayAdapter;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.VacuumDevice;
import ar.edu.itba.hci.uzr.intellifox.api.models.devices.VacuumDeviceState;
import ar.edu.itba.hci.uzr.intellifox.api.models.room.Room;
import ar.edu.itba.hci.uzr.intellifox.ui.devices.vacuum.RoomSpinnerAdapter;
import ar.edu.itba.hci.uzr.intellifox.wrappers.ElectricalInfoRecord;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ElectricalConsFragment extends Fragment {

    private static final String ELECTRICAL_CONSUMPTION_TAG = "ELECTRICAL_CONSUMPTION";

    private PieChart pieChart;
    private TextView totalConsTextView;
    private Spinner periodSpinner;
    private Map<String, Pair<Integer, Integer>> typeInfo;
    private View root;

    private int days = 1;

    private ElectricalConsViewModel electricalConsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_electrical_cons, container, false);

        pieChart = root.findViewById(R.id.pieChart);
        totalConsTextView = root.findViewById(R.id.totalCons);
        periodSpinner = root.findViewById(R.id.periodSpinner);

        if (pieChart != null) {

            if (typeInfo == null) {
                typeInfo = new HashMap<String, Pair<Integer, Integer>>() {
                    {
                        put("faucet", new Pair<>(R.string.dev_faucet, R.drawable.ic_device_water_pump));
                        put("ac", new Pair<>(R.string.dev_ac, R.drawable.ic_device_air_conditioner));
                        put("alarm", new Pair<>(R.string.dev_alarm, R.drawable.ic_device_alarm_light_outline));
                        put("blinds", new Pair<>(R.string.dev_blinds, R.drawable.ic_device_blinds));
                        put("door", new Pair<>(R.string.dev_door, R.drawable.ic_device_door));
                        put("refrigerator", new Pair<>(R.string.dev_refrigerator, R.drawable.ic_device_fridge_outline));
                        put("lamp", new Pair<>(R.string.dev_lamp, R.drawable.ic_device_lightbulb_outline));
                        put("vacuum", new Pair<>(R.string.dev_vacuum, R.drawable.ic_device_robot_vacuum));
                        put("speaker", new Pair<>(R.string.dev_speaker, R.drawable.ic_device_speaker));
                        put("oven", new Pair<>(R.string.dev_oven, R.drawable.ic_device_toaster_oven));
                    }
                };
            }

            pieChart.setVisibility(View.VISIBLE);
            electricalConsViewModel =
                    ViewModelProviders.of(this).get(ElectricalConsViewModel.class);
            electricalConsViewModel.search(days);

            displayPieEntries(new LinkedList<>());

            electricalConsViewModel.getElectricalInfoRecords().observe(getViewLifecycleOwner(), new Observer<List<ElectricalInfoRecord>>() {
                @Override
                public void onChanged(@Nullable List<ElectricalInfoRecord> electricalInfoRecordsList) {
                    if (electricalInfoRecordsList != null) {
                        displayPieEntries(electricalInfoRecordsList);
                    }
                }
            });

            if (periodSpinner != null) {
                Integer[] periodOptions = {1, 7, 30, 365};

                PeriodSpinnerAdapter adapter = new PeriodSpinnerAdapter(root.getContext(),
                        android.R.layout.simple_spinner_item,
                        periodOptions);

                periodSpinner.setAdapter(adapter);
                periodSpinner.setSelection(Arrays.binarySearch(periodOptions, days));
                periodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                        Integer daysSelected = adapter.getItem(position);

                        if (daysSelected != null) {
                            days = daysSelected;
                        }

                        electricalConsViewModel.search(days);
                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> adapter) {
                    }
                });
            }
        }

        return root;
    }

    private void displayPieEntries(List<ElectricalInfoRecord> electricalInfoRecordsList) {
        Map<String, Double> consumptionsPerType =
                electricalInfoRecordsList.stream()
                        .collect(Collectors.groupingBy(ElectricalInfoRecord::getDeviceTypeName,
                                Collectors.summingDouble(e -> e.getHours() * e.getWattagePerHour())));

        List<PieEntry> pieEntries = consumptionsPerType.entrySet().stream().filter(e -> e.getValue() > 0.0).map(c -> new PieEntry((float)((double) c.getValue()), root.getContext().getResources().getString(Objects.requireNonNull(typeInfo.get(c.getKey())).first))).collect(Collectors.toList());

        Optional<Double> op = consumptionsPerType.values().stream().reduce(Double::sum);
        if (totalConsTextView != null) {
            if (op.isPresent()) {
                totalConsTextView.setText(getString(R.string.electrical_cons_total_cons, BigDecimal.valueOf(op.get()).setScale(2, RoundingMode.UP).floatValue() ));
            }
            else {
                totalConsTextView.setText(getString(R.string.electrical_cons_total_cons, BigDecimal.valueOf(0.0).setScale(2, RoundingMode.UP).floatValue() ));
            }
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries, null);;
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueLinePart1OffsetPercentage(90.f);
        pieDataSet.setValueLinePart1Length(1f);
        pieDataSet.setValueLinePart2Length(.2f);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(14f);

        pieChart.setData(pieData);

        pieChart.setDrawEntryLabels(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setHoleRadius(30f);
        pieChart.setTransparentCircleAlpha(64);
        pieChart.setTransparentCircleRadius(35f);
        pieChart.setHoleColor(ContextCompat.getColor(root.getContext(), R.color.background2));

        Legend legend = pieChart.getLegend();

        legend.setTextColor(ContextCompat.getColor(root.getContext(), R.color.text));

        legend.setXEntrySpace(7f);
        legend.setYEntrySpace(0f);
        legend.setYOffset(0f);
        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        legend.setWordWrapEnabled(true);

        pieChart.animateXY(5000, 5000);
        pieChart.invalidate();
    }
}