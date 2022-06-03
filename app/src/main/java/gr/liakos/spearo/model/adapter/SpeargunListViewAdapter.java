package gr.liakos.spearo.model.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.core.content.res.ResourcesCompat;

import java.util.List;

import gr.liakos.spearo.R;
import gr.liakos.spearo.SpearoApplication;
import gr.liakos.spearo.model.object.FishCatch;
import gr.liakos.spearo.model.object.Speargun;
import gr.liakos.spearo.model.viewholder.SpeargunHolder;
import gr.liakos.spearo.util.Constants;
import gr.liakos.spearo.util.MetricConverter;


public class SpeargunListViewAdapter extends ArrayAdapter<Speargun> {

    int layoutResourceId;
    List<Speargun> spearguns;
    Activity activity;
    boolean isMetric;

    public SpeargunListViewAdapter(Activity activity, int layoutResourceId,
                                   List<Speargun> spearguns, boolean isMetric) {
        super(activity.getApplicationContext(), layoutResourceId, spearguns);
        this.layoutResourceId = layoutResourceId;
        this.spearguns = spearguns;
        this.activity = activity;
        this.isMetric = isMetric;
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
        holder.brandTxt.setText(getBrandModelLengthFor(speargun));
        holder.catchesNumTxt.setText(resources.getString(R.string.gun_caught_fish, speargun.getCaughtFish().size()));
        holder.recordTxt.setText(getRecordTextFor(speargun));

        convertView.setOnLongClickListener(longClickListener(speargun));
       return convertView;
    }

    String getRecordTextFor(Speargun speargun) {
        Resources resources = activity.getResources();
        if (speargun.getCaughtFish().isEmpty()){
            return resources.getString(R.string.no_record_yet);
        }else{
            FishCatch recordCatchFish = speargun.getCaughtFish().get(0);
            Double recordCatch = recordCatchFish.getWeight();
            if (recordCatch != null && recordCatch > 0) {
                String recordWeightStr = MetricConverter.convertWeightFromValueStr(isMetric, recordCatch);
                String recordTxt = recordWeightStr + Constants.COMMA_SEP + recordCatchFish.getFish().getCommonName();
                return resources.getString(R.string.record_prefix)  + recordTxt;
            }else{
                return resources.getString(R.string.no_record_yet);
            }
        }
    }

    private View.OnLongClickListener longClickListener(Speargun gun) {
       return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                confirmDeleteAlertDialog(gun);
                return false;
            }
        };
    }

    String getBrandModelLengthFor(Speargun speargun){
        Resources resources = activity.getResources();
        String brandWithModelWithLength = resources.getString(speargun.getBrand().getBrandName());
        String model = speargun.getModel();
        if (model != null){
            if (model.length() > 12){
                model = model.substring(0, 11) + Constants.DOT;
            }
            brandWithModelWithLength += Constants.SPACE + model;
        }

        String gunTypeInParenthesis = Constants.LEFT_PAREN + resources.getString(speargun.getType().getTextId()) + Constants.RIGHT_PAREN;

       return brandWithModelWithLength + Constants.SPACE + speargun.getLength() + Constants.SPACE + gunTypeInParenthesis;
    }

    void confirmDeleteAlertDialog(final Speargun gun) {
        String brand = activity.getResources().getString(gun.getBrand().getBrandName()) + Constants.SPACE + gun.getLength();

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        Resources res = activity.getResources();
        dialogBuilder.setPositiveButton(res.getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ((SpearoApplication) activity.getApplication()).deleteSpearGun(gun);
            }
        })
                .setNegativeButton(res.getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }

                })
                .setMessage(res.getString(R.string.delete_speargun_entry_alert, brand));

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

    }

}
