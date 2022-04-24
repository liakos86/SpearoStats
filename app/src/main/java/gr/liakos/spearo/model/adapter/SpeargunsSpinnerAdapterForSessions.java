package gr.liakos.spearo.model.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import java.util.List;

import gr.liakos.spearo.R;
import gr.liakos.spearo.model.object.Speargun;
import gr.liakos.spearo.util.Constants;

public class SpeargunsSpinnerAdapterForSessions extends ArrayAdapter<Speargun> {

	Activity act;

	List<Speargun> guns;

	public SpeargunsSpinnerAdapterForSessions(Activity act, int txtViewResourceId, List<Speargun> guns) {
		super(act.getApplicationContext(), txtViewResourceId, guns);
		this.act = act;
		this.guns = guns;
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
		LayoutInflater inflater = act.getLayoutInflater(); 
		View mySpinner = inflater.inflate(R.layout.spinner_item_with_pic, parent, false); 
		Speargun gun = guns.get(position);

		TextView main_text = mySpinner .findViewById(R.id.text_main_seen);
		TextView subSpinner = mySpinner .findViewById(R.id.sub_text_seen);
		ImageView left_icon = mySpinner.findViewById(R.id.left_pic);

		if (gun.getGunId()<0){
			main_text.setText(gun.getModel());
			subSpinner.setVisibility(View.GONE);
			left_icon.setVisibility(View.GONE);
		}else {
			left_icon.setVisibility(View.VISIBLE);
			subSpinner.setVisibility(View.VISIBLE);
			Resources resources = act.getResources();

			String main = resources.getString(gun.getBrand().getBrandName());
			main = gun.getModel() != null ? main + Constants.SPACE + gun.getModel() : main;
			main_text.setText(main);

			String sub = resources.getString(gun.getType().getTextId()) + Constants.SPACE + gun.getLength();
			subSpinner.setText(sub);
			left_icon.setImageDrawable(ResourcesCompat.getDrawable(resources, gun.getBrand().getDrawableId(), null));
		}

		return mySpinner; 
		}

} 
