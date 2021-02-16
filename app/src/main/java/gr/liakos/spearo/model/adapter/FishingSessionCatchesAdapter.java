package gr.liakos.spearo.model.adapter;

import gr.liakos.spearo.R;
import gr.liakos.spearo.fragment.FrgFishingSessions;
import gr.liakos.spearo.model.object.FishCatch;
import gr.liakos.spearo.model.viewholder.SessionFishCatchHolder;
import gr.liakos.spearo.util.Constants;
import gr.liakos.spearo.util.MetricConverter;
import gr.liakos.spearo.util.SpearoUtils;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class FishingSessionCatchesAdapter extends ArrayAdapter<FishCatch> {

    int layoutResourceId;
    List<FishCatch> fishCatches;
    Activity activity;
    FrgFishingSessions sessionsFragment;

    public FishingSessionCatchesAdapter(FrgFishingSessions sessionsFragment, Activity activity, int layoutResourceId,
                                        List<FishCatch> catches) {
        super(activity.getApplicationContext(), layoutResourceId, catches);
        this.layoutResourceId = layoutResourceId;
        this.fishCatches = catches;
        this.activity = activity;
        this.sessionsFragment = sessionsFragment;
    }

    public FishingSessionCatchesAdapter(Activity activity,
                                        int layoutResourceId, List<FishCatch> fishCatches) {
    	super(activity.getApplicationContext(), layoutResourceId, fishCatches);
        this.layoutResourceId = layoutResourceId;
        this.fishCatches = fishCatches;
        this.activity = activity;
	}

	@Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SessionFishCatchHolder holder = null;
        if (convertView == null || !(convertView.getTag() instanceof SessionFishCatchHolder)) {
            convertView = activity.getLayoutInflater().inflate(layoutResourceId, parent, false);
            holder = new SessionFishCatchHolder();
            holder.iconFishCatch = (ImageView) convertView
                    .findViewById(R.id.fishCatchIcon);
            holder.textFishCatch = (TextView) convertView
                    .findViewById(R.id.catchText);
            holder.textFishCatchWeigthAndDepth = (TextView) convertView
                    .findViewById(R.id.catchWeigthAndDepth);
            holder.deleteCatch = (ImageView) convertView
                    .findViewById(R.id.deleteCatch);
            convertView.setTag(holder);
        } else {
            holder = (SessionFishCatchHolder) convertView.getTag();
        }
        
        final FishCatch fishCatch = fishCatches.get(position);

       holder.iconFishCatch.setImageDrawable(new SpearoUtils(activity).getDrawableFromFish( fishCatch.getFish()));
       holder.textFishCatch.setText(fishCatch.getFish().getCommonName());
       SharedPreferences app_preferences = activity.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
       boolean isMetric = !app_preferences.getBoolean(Constants.IMPERIAL, false);
       
       boolean weightExists = fishCatch.getWeight() > 0;
       boolean depthExists = fishCatch.getDepthMeters() > 0;
       boolean hourExists = fishCatch.getCatchHour()!=null && fishCatch.getCatchHour() > 0;
       String catchWeightString = weightExists ? MetricConverter.convertWeightFromValueStr(isMetric,  fishCatch.getWeight()) : Constants.EMPTY;
       catchWeightString += catchWeightString.length()>0 &&(depthExists || hourExists) ?  Constants.COMMA_SEP : Constants.EMPTY;
       String catchDepthString = depthExists ? MetricConverter.convertDepthFromValueStr(isMetric,  fishCatch.getDepthMeters()) : Constants.EMPTY;
       catchDepthString += catchDepthString.length()>0 && hourExists ?  Constants.COMMA_SEP : Constants.EMPTY;
       String catchHourString = hourExists ?   SpearoUtils.catchHourFor(fishCatch.getCatchHour()) : Constants.EMPTY;
       String finalCatchInfoText = catchWeightString + catchDepthString + catchHourString;
       holder.textFishCatchWeigthAndDepth.setText(finalCatchInfoText.length() > 0 ? finalCatchInfoText : activity.getResources().getString(R.string.no_catch_data));
       
       if (sessionsFragment ==null){
    	   holder.deleteCatch.setVisibility(View.GONE);
       }else{
    	   holder.deleteCatch.setVisibility(View.VISIBLE);
    	   holder.deleteCatch.setOnClickListener(listenerFor(fishCatch));
       }
       
       return convertView;
    }
    
    OnClickListener listenerFor(final FishCatch fishCatch) {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sessionsFragment.deleteTransientFishCatch(fishCatch);
			}
		};
	}

}
