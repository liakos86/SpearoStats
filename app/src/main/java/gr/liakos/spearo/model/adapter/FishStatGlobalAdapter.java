package gr.liakos.spearo.model.adapter;

import gr.liakos.spearo.R;
//import gr.liakos.spearo.custom.CircularImageView;
import gr.liakos.spearo.model.object.Fish;
import gr.liakos.spearo.model.object.FishAverageStatistic;
import gr.liakos.spearo.model.viewholder.FishStatisticViewHolder;
import gr.liakos.spearo.util.Constants;
import gr.liakos.spearo.util.MetricConverter;
import gr.liakos.spearo.util.SpearoUtils;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import androidx.core.content.res.ResourcesCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class FishStatGlobalAdapter extends ArrayAdapter<FishAverageStatistic> {

	boolean communityStats;
	boolean isMetric;
    public void setCommunityStats(boolean communityStats) {
		this.communityStats = communityStats;
	}

	int layoutResourceId;
    List<FishAverageStatistic> fishStats;
    Activity activity;
    
    SpearoUtils util;

    public FishStatGlobalAdapter(Activity activity, int layoutResourceId, List<FishAverageStatistic> stats, boolean isMetric) {
        super(activity.getApplicationContext(), layoutResourceId, stats);
        this.layoutResourceId = layoutResourceId;
        this.fishStats = stats;
        this.activity = activity;
        this.isMetric = isMetric;
        this.util = new SpearoUtils(activity);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        FishStatisticViewHolder holder = null;
        if (convertView == null || !(convertView.getTag() instanceof FishStatisticViewHolder)) {
            convertView = activity.getLayoutInflater().inflate(R.layout.fish_stat_global_row, parent, false);
            holder = new FishStatisticViewHolder();
            holder.setCommonName((TextView) convertView
                    .findViewById(R.id.statTextCommonName));
            holder.setFishFamily((TextView) convertView
                    .findViewById(R.id.statTextFishFamily));
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
            convertView.setTag(holder);
        } else {
            holder = (FishStatisticViewHolder) convertView.getTag();
        }
        
        final FishAverageStatistic fishStat = fishStats.get(position);
        Fish fish = fishStat.getFish();
		holder.getCommonName().setText(fish.getCommonName());
		holder.getFishFamily().setText(fish.getFishFamily().getName());
        String recordWeightStr = MetricConverter.convertWeightFromValueStr(isMetric, fish.getRecordCatchWeight());
		holder.getWorldRecordWeight().setText(recordWeightStr);
        holder.getFishIcon().setImageDrawable(util.getDrawableFromFish( fish));

        String totalCatches = String.valueOf(fishStat.getTotalCatches());
		holder.getTotalCatches().setText(totalCatches);
        
        String avgWeigthStr = MetricConverter.convertWeightFromValueStr(isMetric, fishStat.getAverageWeight());
        String avgDepthStr = MetricConverter.convertDepthFromValueStr(isMetric, fishStat.getAverageDepth());
		holder.getAvgWeight().setText(avgWeigthStr);
        holder.getAvgDepth().setText(avgDepthStr);
        
        Integer mostCommonSummerHour = fishStat.getMostCommonSummerHour();
        String summerCatchHourFor = Constants.MINUS_SEP;
        if (mostCommonSummerHour != null && mostCommonSummerHour > 0 ){
        	summerCatchHourFor = SpearoUtils.catchHourFor(mostCommonSummerHour);
        }

        holder.getMostCommonSummerHour().setText(summerCatchHourFor);
        
        Integer mostCommonWinterHour = fishStat.getMostCommonWinterHour();
        String winterCatchHourFor = Constants.MINUS_SEP;
        if (mostCommonWinterHour != null && mostCommonWinterHour > 0 ){
        	winterCatchHourFor = SpearoUtils.catchHourFor(mostCommonWinterHour);
        }

        holder.getMostCommonWinterHour().setText(winterCatchHourFor);
        
        if (fish.getConcern().getWeight() > 3){
        	convertView.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.stat_row_high_risk_selector, null));
        }else{
        	convertView.setBackground(ResourcesCompat.getDrawable(activity.getResources(), R.drawable.stat_row_selector, null));
        }
        
        convertView.setOnClickListener(listener(fish, recordWeightStr, summerCatchHourFor, winterCatchHourFor, avgDepthStr, avgWeigthStr, totalCatches));
        
        return convertView;
    }

	private OnClickListener listener(final Fish fish, final String recordWeightStr, final String mostCommonSummerHour, final String mostCommonWinterHour,
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
				family.setText(resources.getString(R.string.legend_family) + Constants.COLON_SEP + fish.getFishFamily().getName());
				
				TextView wRecord = (TextView) dialogView.findViewById(R.id.textWorldRecordExplained);
				wRecord.setText(resources.getString(R.string.legend_world_record) + Constants.COLON_SEP + recordWeightStr);
				
				TextView record = (TextView) dialogView.findViewById(R.id.textRecordExplained);
				record.setVisibility(View.GONE);
				
				
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

}
