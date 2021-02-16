package gr.liakos.spearo.model.adapter;

import gr.liakos.spearo.R;
import gr.liakos.spearo.enums.WindVolume;
import gr.liakos.spearo.util.Constants;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class WindVolumeSpinnerAdapter  extends ArrayAdapter<String> {

    Activity act;

    List<WindVolume> windVolumes;

    String[] volumeStrings;

    public WindVolumeSpinnerAdapter(Activity act, int txtViewResourceId, String[] volumesArray, List<WindVolume> _windVolumes) {
        super(act.getApplicationContext(), txtViewResourceId, volumesArray);
        this.act = act;
        this.windVolumes = _windVolumes;
        this.volumeStrings = volumesArray;
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
        mySpinner.findViewById(R.id.left_pic).setVisibility(View.GONE);
        TextView main_text = mySpinner .findViewById(R.id.text_main_seen);
        main_text.setText(volumeStrings[position]);
        TextView sub_text = mySpinner .findViewById(R.id.sub_text_seen);
        if (position == 0) {
            sub_text.setVisibility(View.VISIBLE);
            sub_text.setText(act.getResources().getString(R.string.not_known));
        }else{
            sub_text.setVisibility(View.GONE);
        }
        return mySpinner;
    }
}



