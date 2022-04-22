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
import gr.liakos.spearo.enums.SpearGunBrand;

public class SpeargunBrandsSpinnerAdapter extends ArrayAdapter<SpearGunBrand> {

	Activity act;

	List<SpearGunBrand> gunBrands;

	public SpeargunBrandsSpinnerAdapter(Activity act, int txtViewResourceId, List<SpearGunBrand> brands) {
		super(act.getApplicationContext(), txtViewResourceId, brands);
		this.act = act;
		this.gunBrands = brands;
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
		SpearGunBrand brand = gunBrands.get(position);
		Resources resources = act.getResources();
		TextView main_text = mySpinner .findViewById(R.id.text_main_seen);
		mySpinner.findViewById(R.id.sub_text_seen).setVisibility(View.GONE);
		main_text.setText(resources.getString(brand.getBrandName()));

		ImageView left_icon = mySpinner .findViewById(R.id.left_pic);
		left_icon.setImageDrawable(ResourcesCompat.getDrawable(resources, brand.getDrawableId(), null));
		
		return mySpinner; 
		} 
} 



