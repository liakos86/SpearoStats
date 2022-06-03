package gr.liakos.spearo.model.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import gr.liakos.spearo.R;
import gr.liakos.spearo.enums.SpeargunType;
import gr.liakos.spearo.model.viewholder.SingleTextViewHolder;

public class SpearGunTypesSpinnerAdapter extends ArrayAdapter<SpeargunType> {

	Activity act;

	List<SpeargunType> speargunTypes;

	public SpearGunTypesSpinnerAdapter(Activity act, int txtViewResourceId, List<SpeargunType> speargunTypes) {
		super(act.getApplicationContext(), txtViewResourceId, speargunTypes);
		this.act = act;
		this.speargunTypes = speargunTypes;
	} 

	@Override 
	public View getDropDownView(int position, View cnvtView, ViewGroup prnt) { 
		return getCustomView(position, cnvtView, prnt); 
	} 
	
	@Override 
	public View getView(int pos, View cnvtView, ViewGroup prnt) { 
		return getCustomView(pos, cnvtView, prnt); 
	}
	
	public View getCustomView(int position, View convertView, ViewGroup parent) {
		SingleTextViewHolder holder = null;
		if (convertView == null || !(convertView.getTag() instanceof SingleTextViewHolder)) {
			convertView = act.getLayoutInflater().inflate(R.layout.spinner_item, parent, false);
			holder = new SingleTextViewHolder();
			holder.mainTextView = convertView
					.findViewById(R.id.spinnerItemText);
			convertView.setTag(holder);
		} else {
			holder = (SingleTextViewHolder) convertView.getTag();
		}

		SpeargunType speargunType = speargunTypes.get(position);
		Resources resources = act.getResources();

		holder.mainTextView.setText(resources.getString(speargunType.getTextId()));

		return convertView;
		} 
} 



