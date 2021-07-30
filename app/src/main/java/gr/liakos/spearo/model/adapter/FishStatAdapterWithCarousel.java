package gr.liakos.spearo.model.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import java.util.List;
import java.util.Map;

import gr.liakos.spearo.ActSpearoStatsMain;
import gr.liakos.spearo.R;
import gr.liakos.spearo.enums.Season;
import gr.liakos.spearo.model.object.Fish;
import gr.liakos.spearo.model.object.FishAverageStatistic;
import gr.liakos.spearo.model.viewholder.FishStatisticViewHolderBasic;
import gr.liakos.spearo.model.viewholder.FishStatisticViewHolderFull;
import gr.liakos.spearo.util.Constants;
import gr.liakos.spearo.util.MetricConverter;
import gr.liakos.spearo.util.SpearoUtils;
import gr.liakos.spearo.util.helper.DialogViewsHelper;

public class FishStatAdapterWithCarousel extends ArrayAdapter<FishAverageStatistic> {

    boolean isMetric;
    boolean isPremiumDiagrams;

    int layoutResourceId;
    List<FishAverageStatistic> fishStats;
    Activity activity;

    SpearoUtils util;

    public FishStatAdapterWithCarousel(Activity activity, int layoutResourceId, List<FishAverageStatistic> stats, boolean isMetric, boolean isPremiumDiagrams) {
        super(activity.getApplicationContext(), layoutResourceId, stats);
        this.layoutResourceId = layoutResourceId;
        this.fishStats = stats;
        this.activity = activity;
        this.isMetric = isMetric;
        this.isPremiumDiagrams = isPremiumDiagrams;
        this.util = new SpearoUtils(activity);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

            FishStatisticViewHolderFull holder = null;
            if (convertView == null || !(convertView.getTag() instanceof FishStatisticViewHolderFull)) {
                convertView = activity.getLayoutInflater().inflate(R.layout.fish_stat_row_full, parent, false);
                holder = new FishStatisticViewHolderFull();
                setBasicViews(holder, convertView);
                setCarouselViews(holder, convertView, parent);
                convertView.setTag(holder);
            } else {
                holder = (FishStatisticViewHolderFull) convertView.getTag();
            }

            final FishAverageStatistic fishStat = fishStats.get(position);

            basicConvertViewActions(fishStat, holder, convertView);

            if (isPremiumDiagrams || position < 2) {
                holder.getCarouselView().setVisibility(View.VISIBLE);
                holder.getNoPremiumLayout().setVisibility(View.GONE);

                holder.getCarouselView().setViewListener(carouselViewListener(fishStat, holder));

                holder.getCarouselView().setPageCount(3);
                holder.getCarouselView().setSlideInterval(0);
                holder.getCarouselView().setAnimateOnBoundary(true);
                holder.getCarouselView().setElevation(10f);


               holder.getLineChartMonthsTitle().setText(activity.getResources().getString(R.string.fish_per_month));
               holder.getLineChartSummerTitle().setText(activity.getResources().getString(R.string.fish_per_hour_summer));
               holder.getLineChartWinterTitle().setText(activity.getResources().getString(R.string.fish_per_hour_winter));

            }else{
                holder.getCarouselView().setVisibility(View.GONE);
                holder.getNoPremiumLayout().setVisibility(View.VISIBLE);

                Button noPremiumButton = holder.getNoPremiumLayout().findViewById(R.id.goPremium);
                noPremiumButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!SpearoUtils.isOnline(getContext())){
                            ((ActSpearoStatsMain)activity).snack(R.string.network_error);
                            return;
                        }

                        ((ActSpearoStatsMain) activity).purchasePremiumDiagrams();

                    }
                });

            }

            return convertView;
    }

    private void setCarouselViews(FishStatisticViewHolderFull holder,View convertView,  ViewGroup parent ) {
        holder.setNoPremiumLayout(convertView.findViewById(R.id.no_premium_carousel));

        CarouselView carouselView = convertView.findViewById(R.id.carouselDiagramsView);
        holder.setCarouselView(carouselView);

        View lineChartMonths = activity.getLayoutInflater().inflate(R.layout.diagram_fish_per_month, parent, false);
        TextView monthTitle = lineChartMonths.findViewById(R.id.lineChartMonthsTitle);
        holder.setLineChartMonths(lineChartMonths);
        holder.setLineChartMonthsTitle(monthTitle);

        View lineChartSummer = activity.getLayoutInflater().inflate(R.layout.diagram_fish_per_hour_summer, parent, false);
        TextView summerTitle = lineChartSummer.findViewById(R.id.lineChartSummerTitle);
        holder.setLineChartSummer(lineChartSummer);
        holder.setLineChartSummerTitle(summerTitle);

        View lineChartWinter = activity.getLayoutInflater().inflate(R.layout.diagram_fish_per_hour_winter, parent, false);
        TextView winterTitle = lineChartWinter.findViewById(R.id.lineChartWinterTitle);
        holder.setLineChartWinter(lineChartWinter);
        holder.setLineChartWinterTitle(winterTitle);
    }

    private void setBasicViews(FishStatisticViewHolderBasic holder, View convertView){
        holder.setCommonName((TextView) convertView
                .findViewById(R.id.statTextCommonName));
        holder.setRecordWeight((TextView) convertView
                .findViewById(R.id.statTextRecordWeight));
        holder.setTotalCatches((TextView) convertView
                .findViewById(R.id.statTextTotalCatches));
        holder.setAvgDepth((TextView) convertView
                .findViewById(R.id.statTextAvgDepth));
        holder.setAvgWeight((TextView) convertView
                .findViewById(R.id.statTextAvgWeight));
        holder.setMostCommonSummerHour((TextView) convertView
                .findViewById(R.id.statTextCommonSummerHour));
        holder.setMostCommonWinterHour((TextView) convertView
                .findViewById(R.id.statTextCommonWinterHour));
        holder.setFishIcon((ImageView) convertView
                .findViewById(R.id.statImageViewFish));
        holder.setWorldRecordWeight((TextView) convertView
                .findViewById(R.id.statTextWorldRecord));
    }

    private void basicConvertViewActions(FishAverageStatistic fishStat, FishStatisticViewHolderBasic holder, View convertView) {
        Fish fish = fishStat.getFish();
        holder.getCommonName().setText(fish.getCommonName());

        String worldRecordWeightStr = Constants.MINUS_SEP;
        if (fish.getRecordCatchWeight() > 0) {
            worldRecordWeightStr = MetricConverter.convertWeightFromValueStr(isMetric, fish.getRecordCatchWeight());
        }
        holder.getWorldRecordWeight().setText(worldRecordWeightStr);
        holder.getFishIcon().setImageDrawable(util.getDrawableFromFish(fish));

        String totalCatches = String.valueOf(fishStat.getTotalCatches());
        holder.getTotalCatches().setText(totalCatches);

        String avgWeigthStr = MetricConverter.convertWeightFromValueStr(isMetric, fishStat.getAverageWeight());
        String personalRecordWeightStr = MetricConverter.convertWeightFromValueStr(isMetric, fishStat.getRecordWeight());
        String avgDepthStr = MetricConverter.convertDepthFromValueStr(isMetric, fishStat.getAverageDepth());

        holder.getRecordWeight().setText(personalRecordWeightStr);
        holder.getAvgWeight().setText(avgWeigthStr);
        holder.getAvgDepth().setText(avgDepthStr);

        Integer mostCommonSummerHour = fishStat.getMostCommonSummerHour();
        String summerCatchHourFor = Constants.MINUS_SEP;
        if (mostCommonSummerHour != null && mostCommonSummerHour > 0) {
            summerCatchHourFor = SpearoUtils.catchHourFor(mostCommonSummerHour);
        }

        holder.getMostCommonSummerHour().setText(summerCatchHourFor);

        Integer mostCommonWinterHour = fishStat.getMostCommonWinterHour();
        String winterCatchHourFor = Constants.MINUS_SEP;
        if (mostCommonWinterHour != null && mostCommonWinterHour > 0) {
            winterCatchHourFor = SpearoUtils.catchHourFor(mostCommonWinterHour);
        }

        holder.getMostCommonWinterHour().setText(winterCatchHourFor);

        if (fish.getConcern().getWeight() > 3) {
            convertView.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.stat_row_high_risk_selector, null));
        } else {
            convertView.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.stat_row_selector, null));
        }

        convertView.setOnClickListener(listener(fish, personalRecordWeightStr, worldRecordWeightStr, summerCatchHourFor, winterCatchHourFor, avgDepthStr, avgWeigthStr, totalCatches));

    }

    private ViewListener carouselViewListener(FishAverageStatistic fishStat, FishStatisticViewHolderFull holder){

        return new ViewListener() {
            @Override
            public View setViewForPosition(int position) {

                if (position == 0) {
                    View monthlyView = holder.getLineChartMonths();//activity.getLayoutInflater().inflate(R.layout.diagram_fish_per_month, null);
                     createLineChartMonthly(monthlyView, fishStat.getCatchesPerMonth());
                    return monthlyView;
                } else if (position == 1){
                    View summerView = holder.getLineChartSummer();//activity.getLayoutInflater().inflate(R.layout.diagram_fish_per_hour_summer, null);
                    createLineChartSeason(summerView, fishStat.getCatchesPerHourPerSeason().get(Season.SUMMER), "Summer");
                    return summerView;
                } else{
                    View winterView = holder.getLineChartWinter();//activity.getLayoutInflater().inflate(R.layout.diagram_fish_per_hour_winter, null);
                    createLineChartSeason(winterView, fishStat.getCatchesPerHourPerSeason().get(Season.WINTER), "Winter");
                    return winterView;
                }
            }
        };
    }

    private OnClickListener listener(final Fish fish, final String recordWeightStr, final String worldRecordWeightStr, final String mostCommonSummerHour, final String mostCommonWinterHour,
                                     final String avgDepthStr, final String avgWeigthStr, final String totalCatches) {
        return new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
                View dialogView = activity.getLayoutInflater().inflate(R.layout.custom_legend_dialog, null);
                dialogBuilder.setView(dialogView);
                Resources resources = activity.getApplicationContext().getResources();

                TextView title = (TextView) dialogView.findViewById(R.id.legend_title);
                title.setText(fish.getCommonName());

                TextView subtitle = (TextView) dialogView.findViewById(R.id.legend_subtitle);
                subtitle.setText(SpearoUtils.getStringResourceByName(activity.getApplicationContext(), fish.getConcern().getDesc()));

                TextView info = (TextView) dialogView.findViewById(R.id.textInfoExplained);
                info.setText(resources.getString(R.string.legend_info) + Constants.COLON_SEP + fish.getCommonName());

                TextView family = (TextView) dialogView.findViewById(R.id.textFishFamilyExplained);
                family.setVisibility(View.GONE);

                TextView record = (TextView) dialogView.findViewById(R.id.textRecordExplained);
                record.setText(resources.getString(R.string.legend_record) + Constants.COLON_SEP + recordWeightStr);
                TextView wRecord = (TextView) dialogView.findViewById(R.id.textWorldRecordExplained);
                wRecord.setText(resources.getString(R.string.legend_world_record) + Constants.COLON_SEP + worldRecordWeightStr);

                TextView hourSummer = (TextView) dialogView.findViewById(R.id.textHourSummerExplained);
                hourSummer.setText(resources.getString(R.string.legend_summer) + Constants.COLON_SEP + mostCommonSummerHour);

                TextView hourWinter = (TextView) dialogView.findViewById(R.id.textHourWinterExplained);
                hourWinter.setText(resources.getString(R.string.legend_winter) + Constants.COLON_SEP + mostCommonWinterHour);

                TextView depth = (TextView) dialogView.findViewById(R.id.textDepthExplained);
                depth.setText(resources.getString(R.string.legend_depth) + Constants.COLON_SEP + avgDepthStr);

                TextView weight = (TextView) dialogView.findViewById(R.id.textWeightExplained);
                weight.setText(resources.getString(R.string.legend_weight) + Constants.COLON_SEP + avgWeigthStr);

                TextView num = (TextView) dialogView.findViewById(R.id.textFishNumExplained);
                num.setText(resources.getString(R.string.legend_num) + Constants.COLON_SEP + totalCatches);


                dialogBuilder.setNegativeButton(resources.getString(R.string.close), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        };
    }

    private void createLineChartSeason(View lineChartView, Map<Integer, Integer> catchesPerHour, String season) {
        new DialogViewsHelper().createSeasonLineChartFor(activity, lineChartView, catchesPerHour, season);
    }

    private void createLineChartMonthly(View dialogView, Map<Integer, Integer> catchesPerMonth) {
        new DialogViewsHelper().createMonthlyLineChartFor(activity, dialogView, catchesPerMonth);
    }

}
