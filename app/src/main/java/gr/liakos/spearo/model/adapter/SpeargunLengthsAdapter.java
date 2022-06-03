package gr.liakos.spearo.model.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import gr.liakos.spearo.R;
import gr.liakos.spearo.model.viewholder.SingleTextViewHolder;


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
        SingleTextViewHolder holder = null;
        if (convertView == null || !(convertView.getTag() instanceof SingleTextViewHolder)) {
            convertView = activity.getLayoutInflater().inflate(R.layout.spinner_item, parent, false);
            holder = new SingleTextViewHolder();
            holder.mainTextView = convertView
                    .findViewById(R.id.spinnerItemText);
            convertView.setTag(holder);
        } else {
            holder = (SingleTextViewHolder) convertView.getTag();
        }
        
        String speargunLength = lengths.get(position);

        holder.mainTextView.setText(speargunLength);
       return convertView;
    }

}
