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

		if (gun.getGunId()<0){
			main_text.setText(gun.getModel());
		}else {

			Resources resources = act.getResources();
			main_text.setText(resources.getString(gun.getBrand().getBrandName()));
			subSpinner.setText(gun.getModel() + "/" + gun.getLength());

			ImageView left_icon = mySpinner.findViewById(R.id.left_pic);
			left_icon.setImageDrawable(ResourcesCompat.getDrawable(resources, gun.getBrand().getDrawableId(), null));
		}

		return mySpinner; 
		}

} 
