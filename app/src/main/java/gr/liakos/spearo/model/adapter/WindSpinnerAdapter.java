package gr.liakos.spearo.model.adapter;

import gr.liakos.spearo.R;
import gr.liakos.spearo.enums.Wind;

import java.util.List;

import android.app.Activity;
import android.content.res.Resources;
import androidx.core.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WindSpinnerAdapter  extends ArrayAdapter<String> { 
	
	Activity act;
	
	List<Wind> winds;
	
	public WindSpinnerAdapter(Activity act, int txtViewResourceId, String[] phasesStr, List<Wind> _winds) { 
		super(act.getApplicationContext(), txtViewResourceId, phasesStr); 
		this.act = act;
		this.winds = _winds;
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
		Wind wind = winds.get(position);
		Resources resources = act.getResources();
		TextView main_text = mySpinner .findViewById(R.id.text_main_seen);
		TextView subSpinner = mySpinner .findViewById(R.id.sub_text_seen);
		main_text.setText(resources.getString(wind.getFullDesc()));
		subSpinner.setText(resources.getString(wind.getShortDesc()));

		ImageView left_icon = mySpinner .findViewById(R.id.left_pic);
		left_icon.setImageDrawable(ResourcesCompat.getDrawable(resources, wind.getSpinnerDrawable(), null));
		
		return mySpinner; 
		} 
} 



