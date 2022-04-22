package gr.liakos.spearo.model.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import java.util.List;

import gr.liakos.spearo.R;
import gr.liakos.spearo.fragment.FrgFishingSessions;
import gr.liakos.spearo.model.object.Fish;
import gr.liakos.spearo.model.object.FishCatch;
import gr.liakos.spearo.model.object.Speargun;
import gr.liakos.spearo.model.viewholder.SessionFishCatchHolder;
import gr.liakos.spearo.model.viewholder.SpeargunHolder;
import gr.liakos.spearo.util.Constants;
import gr.liakos.spearo.util.MetricConverter;
import gr.liakos.spearo.util.SpearoUtils;


public class SpeargunListViewAdapter extends ArrayAdapter<Speargun> {

    int layoutResourceId;
    List<Speargun> spearguns;
    Activity activity;

    public SpeargunListViewAdapter(Activity activity, int layoutResourceId,
                                   List<Speargun> spearguns) {
        super(activity.getApplicationContext(), layoutResourceId, spearguns);
        this.layoutResourceId = layoutResourceId;
        this.spearguns = spearguns;
        this.activity = activity;
    }

	@Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SpeargunHolder holder = null;
        if (convertView == null || !(convertView.getTag() instanceof SpeargunHolder)) {
            convertView = activity.getLayoutInflater().inflate(layoutResourceId, parent, false);
            holder = new SpeargunHolder();
            holder.brandImg = convertView
                    .findViewById(R.id.statImageViewGunBrand);
            holder.brandTxt = convertView
                    .findViewById(R.id.statTextBrandName);
            holder.modelTxt = convertView
                    .findViewById(R.id.statTextGunModelLength);
            holder.recordTxt = convertView
                    .findViewById(R.id.statTextRecordWeight);
            holder.catchesNumTxt = convertView
                    .findViewById(R.id.statTextTotalSpeargunCatches);

            convertView.setTag(holder);
        } else {
            holder = (SpeargunHolder) convertView.getTag();
        }
        
        final Speargun speargun = spearguns.get(position);

        Resources resources = activity.getResources();

        holder.brandImg.setImageDrawable(ResourcesCompat.getDrawable(resources, speargun.getBrand().getDrawableId(), null));

       String brand = resources.getString(speargun.getBrand().getBrandName());
        holder.brandTxt.setText(brand);

        String brandWithModelWithLength = Constants.EMPTY;
       if (speargun.getModel()!=null || speargun.getLength()!=null) {
           String model = speargun.getModel();
           Integer length = speargun.getLength();

           if (length != null){
               brandWithModelWithLength += Constants.SPACE + length;
           }
           if (model != null){
               if (brandWithModelWithLength.length() > 1){
                   brandWithModelWithLength += Constants.COMMA_SEP;
               }

               brandWithModelWithLength += model;
           }
       }

        holder.modelTxt.setText(brandWithModelWithLength);
        holder.catchesNumTxt.setText(String.valueOf(speargun.getCaughtFish().size()));

        if (speargun.getCaughtFish().isEmpty()){
           holder.recordTxt.setText(Constants.MINUS);
       }else{
            FishCatch recordCatchFish = speargun.getCaughtFish().get(0);
            Double recordCatch = recordCatchFish.getWeight();
            if (recordCatch != null && recordCatch > 0) {
                String recordTxt = recordCatchFish.getFish().getCommonName() + Constants.COMMA_SEP + recordCatch.toString();
                holder.recordTxt.setText(recordTxt);
            }else{
                holder.recordTxt.setText(Constants.MINUS);
            }
       }

       return convertView;
    }

}
