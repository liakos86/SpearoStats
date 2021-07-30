package gr.liakos.spearo.util.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import gr.liakos.spearo.R;
import gr.liakos.spearo.util.DateUtils;

public class DialogViewsHelper {

    public void createMonthlyLineChartFor(Activity activity, View dialogView, Map<Integer, Integer> catchesPerMonth){
        LineChart lineChart = dialogView.findViewById(R.id.stats_diagram_per_month);

        lineChart.getDescription().setEnabled(false);

        lineChart.getAxisLeft().setAxisMinimum(0);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {

            @Override
            public String getFormattedValue(float value) {
                return DateUtils.getMonthName((int) value, activity);
            }
        });

        xAxis.setLabelCount(12, true);

        ArrayList<Entry> values = new ArrayList<>();
        values.add(new Entry(Calendar.JANUARY, catchesPerMonth.get(Calendar.JANUARY)));
        values.add(new Entry(Calendar.FEBRUARY, catchesPerMonth.get(Calendar.FEBRUARY)));
        values.add(new Entry(Calendar.MARCH, catchesPerMonth.get(Calendar.MARCH)));
        values.add(new Entry(Calendar.APRIL, catchesPerMonth.get(Calendar.APRIL)));
        values.add(new Entry(Calendar.MAY, catchesPerMonth.get(Calendar.MAY)));
        values.add(new Entry(Calendar.JUNE, catchesPerMonth.get(Calendar.JUNE)));
        values.add(new Entry(Calendar.JULY, catchesPerMonth.get(Calendar.JULY)));
        values.add(new Entry(Calendar.AUGUST, catchesPerMonth.get(Calendar.AUGUST)));
        values.add(new Entry(Calendar.SEPTEMBER, catchesPerMonth.get(Calendar.SEPTEMBER)));
        values.add(new Entry(Calendar.OCTOBER, catchesPerMonth.get(Calendar.OCTOBER)));
        values.add(new Entry(Calendar.NOVEMBER, catchesPerMonth.get(Calendar.NOVEMBER)));
        values.add(new Entry(Calendar.DECEMBER, catchesPerMonth.get(Calendar.DECEMBER)));

        LineDataSet lowLineDataSet = getDefaultDataSet(values, activity.getApplicationContext(), activity.getApplicationContext().getResources().getString(R.string.catches_per_month));
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lowLineDataSet);
        LineData lineData = new LineData(dataSets);

        lineChart.animateY(3000, Easing.EaseOutBack);

        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    private LineDataSet getDefaultDataSet(ArrayList<Entry> values, Context applicationContext, String legend) {
        LineDataSet lowLineDataSet = new LineDataSet(values, legend);
        lowLineDataSet.setDrawCircles(true);
        lowLineDataSet.setCircleRadius(4);
        lowLineDataSet.setDrawValues(true);
        lowLineDataSet.setLineWidth(3);
        lowLineDataSet.setColor(Color.RED);
        lowLineDataSet.setCircleColor(Color.RED);
        lowLineDataSet.setDrawFilled(true);
        lowLineDataSet.setFillColor(ContextCompat.getColor(applicationContext, R.color.interval_blue));
        //lowLineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        return lowLineDataSet;
    }

    public void createSeasonLineChartFor(Activity activity, View lineChartView, Map<Integer, Integer> catchesPerHour, String season) {

        LineChart lineChart;

        if ("Summer".equals(season)) {
            lineChart = lineChartView.findViewById(R.id.stats_diagram_summer);
        }else{
            lineChart = lineChartView.findViewById(R.id.stats_diagram_winter);
        }

        lineChart.getDescription().setEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {

            @Override
            public String getFormattedValue(float value) {
                return (int)value + ":00";
            }
        });

        lineChart.getAxisLeft().setAxisMinimum(0f);

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 6; i<21; i++){
            values.add(new Entry(i, catchesPerHour.get(i)));
        }

        LineDataSet lowLineDataSet = getDefaultDataSet(values, activity.getApplicationContext(), activity.getApplicationContext().getResources().getString(R.string.catches_per_season));
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lowLineDataSet);
        LineData lineData = new LineData(dataSets);
        lineChart.animateY(3000, Easing.EaseOutBack);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

}
