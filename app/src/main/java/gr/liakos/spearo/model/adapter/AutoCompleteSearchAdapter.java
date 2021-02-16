package gr.liakos.spearo.model.adapter;

import gr.liakos.spearo.R;
import gr.liakos.spearo.fragment.FrgFishingSessions;
import gr.liakos.spearo.model.object.Fish;
import gr.liakos.spearo.model.viewholder.FishViewHolder;
import gr.liakos.spearo.util.Constants;
import gr.liakos.spearo.util.SpearoUtils;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

public class AutoCompleteSearchAdapter
extends ArrayAdapter<Fish>{
	
	Filter filter;
	List<Fish> allFish;
	List<Fish> filteredList;
	List<Fish> tempFishList;
	
	FrgFishingSessions fragment;
	
    public AutoCompleteSearchAdapter(FrgFishingSessions fragment, List<Fish> allFish) {
    	super(fragment.getActivity().getApplicationContext(), R.layout.fish_row, allFish);
    	this.fragment = fragment;
    	this.allFish = allFish;
    	this.tempFishList= new ArrayList<Fish>(allFish);
    	this.filteredList = new ArrayList<Fish>();
    }
    
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

        	FishViewHolder holder = null;
            if (convertView == null || !(convertView.getTag() instanceof FishViewHolder)) {
                convertView = fragment.getActivity().getLayoutInflater().inflate(R.layout.fish_row, parent, false);
                holder = new FishViewHolder();
                holder.setCommonName((TextView) convertView.findViewById(R.id.textCommonName));
                holder.setFishFamily((TextView) convertView.findViewById(R.id.textFishFamily));
                holder.setFishIcon((ImageView) convertView.findViewById(R.id.imageViewFish));
            } else {
                holder = (FishViewHolder) convertView.getTag();
            }
            
            final Fish fish = getItem(position);
            
            holder.getCommonName().setText( fish.getCommonName());
            holder.getFishFamily().setText(fish.getLatinName() + Constants.COMMA_SEP + fish.getFishFamily().getName());
            holder.getFishIcon().setImageDrawable(new SpearoUtils(fragment.getActivity()).getDrawableFromFish(fish));
            
            	convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						setSelectedFish(fish);
					}
				});
            
            return convertView;
        }
        
        void setSelectedFish(Fish fish) {
        	((FrgFishingSessions)fragment).setSelectedFish(fish);
			SpearoUtils.hideSoftKeyboard(fragment.getActivity());
		}

        @Override
        public Filter getFilter() {
            if (filter == null) {
                filter = fishFilter;
            }
            return filter;
        }


// filtering the auto complete string and searching store name, address, phone and postal code
        Filter fishFilter = new Filter() {
	

		@Override
        protected FilterResults performFiltering(CharSequence constraint) {
			
			if (constraint != null && constraint.toString().length() > 1) {
				String constraintString = constraint.toString().trim();
				String searchString = constraintString.toLowerCase(Locale.getDefault());
                filteredList.clear();
                for (Fish fish : tempFishList) {
                 		
                	if (match(fish.getLatinName(), fish.getCommonName(), fish.getSecondaryCommonNameForSearch(), searchString)){
                		filteredList.add(fish);
                     }
                	 
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                filterResults.count = filteredList.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
		}
			
        @SuppressWarnings("unchecked")
		@Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
        	
        	 List<Fish> filterList = (ArrayList<Fish>) results.values;
             if (results != null && results.count > 0) {
                 clear();
//                 for (Fish fish: filterList) {
                for (Fish fish: new ArrayList<Fish>(filterList)) {
                     add(fish);
                     notifyDataSetChanged();
                 }
             }
         }

    };
    
    boolean match(String latinname, String commonname, String secondary, String searched){
    	latinname = latinname.toLowerCase(Locale.ENGLISH);
    	commonname = removeDiacriticalMarks(commonname.toLowerCase(Locale.getDefault()));
    	secondary = removeDiacriticalMarks(secondary.toLowerCase(new Locale("el")));
    	searched = removeDiacriticalMarks(searched.toLowerCase(Locale.getDefault()));
    	
    	return latinname.contains(searched) ||
  		commonname.contains(searched) ||
  			secondary.contains(searched);
    }
    
    public static String removeDiacriticalMarks(String string) {
        return Normalizer.normalize(string, Form.NFD)
            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
    

};