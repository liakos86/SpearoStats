package gr.liakos.spearo.model.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.core.content.res.ResourcesCompat;

import java.util.List;

import gr.liakos.spearo.R;
import gr.liakos.spearo.enums.SpearGunBrand;
import gr.liakos.spearo.model.viewholder.SpeargunHolder;

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
		SpeargunHolder holder = null;
		if (convertView == null || !(convertView.getTag() instanceof SpeargunHolder)) {
			convertView = act.getLayoutInflater().inflate(R.layout.spinner_item_with_pic_desc, parent, false);
			holder = new SpeargunHolder();
			holder.brandTxt = convertView
					.findViewById(R.id.text_main_seen);
			holder.brandImg = convertView
					.findViewById(R.id.left_pic);
			convertView.setTag(holder);
		} else {
			holder = (SpeargunHolder) convertView.getTag();
		}

		SpearGunBrand brand = gunBrands.get(position);
		Resources resources = act.getResources();

		holder.brandTxt.setText(resources.getString(brand.getBrandName()));
		holder.brandImg.setImageDrawable(ResourcesCompat.getDrawable(resources, brand.getDrawableId(), null));
		
		return convertView;
		} 
} 



