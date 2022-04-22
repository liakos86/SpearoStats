package gr.liakos.spearo.model.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import java.util.List;

import gr.liakos.spearo.R;
import gr.liakos.spearo.model.object.Speargun;
import gr.liakos.spearo.model.viewholder.GunLengthHolder;
import gr.liakos.spearo.model.viewholder.SpeargunHolder;
import gr.liakos.spearo.util.Constants;


public class SpeargunLengthsAdapter extends ArrayAdapter<String> {

    int layoutResourceId;
    List<String> lengths;
    Activity activity;

    public SpeargunLengthsAdapter(Activity activity, int layoutResourceId,
                                  List<String> lengths) {
        super(activity.getApplicationContext(), layoutResourceId, lengths);
        this.layoutResourceId = layoutResourceId;
        this.lengths = lengths;
        this.activity = activity;
    }

	@Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        GunLengthHolder holder = null;
        if (convertView == null || !(convertView.getTag() instanceof GunLengthHolder)) {
            convertView = activity.getLayoutInflater().inflate(R.layout.gun_length_row, parent, false);
            holder = new GunLengthHolder();
            holder.gunLengthTextView = convertView
                    .findViewById(R.id.spearGunLengthTextView);
            convertView.setTag(holder);
        } else {
            holder = (GunLengthHolder) convertView.getTag();
        }
        
        String speargunLength = lengths.get(position);

        holder.gunLengthTextView.setText(speargunLength);
       return convertView;
    }

}
