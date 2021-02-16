package gr.liakos.spearo.model.adapter;

import gr.liakos.spearo.R;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomSpinnerAdapter  extends ArrayAdapter<String> { 
	
	Activity act;
	
	List<String> catchHours;
	
	public CustomSpinnerAdapter(Activity act, int txtViewResourceId, String[] catchHoursStr, List<String> catchHours) { 
		super(act.getApplicationContext(), txtViewResourceId, catchHoursStr); 
		this.act = act;
		this.catchHours = catchHours;
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
		View mySpinner = inflater.inflate(R.layout.spinner_item, parent, false); 
		TextView main_text = (TextView) mySpinner .findViewById(R.id.spinnerItemText); 
		String hour = catchHours.get(position);
		main_text.setText(hour); 
		return mySpinner; 
		} 
} 



