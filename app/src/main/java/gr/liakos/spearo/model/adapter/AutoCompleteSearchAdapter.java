package gr.liakos.spearo.model.adapter;

import gr.liakos.spearo.ActSpearoStatsMain;
import gr.liakos.spearo.R;
import gr.liakos.spearo.fragment.FrgFishingSessions;
import gr.liakos.spearo.model.object.Fish;
import gr.liakos.spearo.model.viewholder.FishViewHolder;
import gr.liakos.spearo.util.SpearoUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import static gr.liakos.spearo.util.StringUtils.removeDiacriticalMarks;
import static gr.liakos.spearo.util.StringUtils.stringContainedInFishNames;

public class AutoCompleteSearchAdapter
extends ArrayAdapter<Fish>{
	
	Filter filter;
	List<Fish> allFish;
	List<Fish> filteredList;
	List<Fish> tempFishList;
	
	FrgFishingSessions fragment;
	
    public AutoCompleteSearchAdapter(FrgFishingSessions fragment, List<Fish> allFish) {
    	super(fragment.getActivity().getApplicationContext(), R.layout.fish_row_auto_complete, allFish);
    	this.fragment = fragment;
    	this.allFish = allFish;
    	this.tempFishList= new ArrayList<>(allFish);
    	this.filteredList = new ArrayList<>();
    }
    
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

        	FishViewHolder holder = null;
            if (convertView == null || !(convertView.getTag() instanceof FishViewHolder)) {
                convertView = fragment.requireActivity().getLayoutInflater().inflate(R.layout.fish_row_auto_complete, parent, false);
                holder = new FishViewHolder();
                holder.setCommonName( convertView.findViewById(R.id.textCommonName));
                holder.setFishFamily( convertView.findViewById(R.id.textFishFamily));
                holder.setFishIcon( convertView.findViewById(R.id.imageViewFish));
            } else {
                holder = (FishViewHolder) convertView.getTag();
            }
            
            final Fish fish = getItem(position);
            
            holder.getCommonName().setText( fish.getCommonName());
            holder.getFishFamily().setText(fragment.requireActivity().getResources().getString( R.string.concat_fish_family, fish.getLatinName(), fish.getFishFamily().getName()));
            holder.getFishIcon().setImageDrawable(new SpearoUtils(fragment.requireActivity()).getDrawableFromFish(fish));
            
            	convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						setSelectedFish(fish);
					}
				});
            
            return convertView;
        }
        
        void setSelectedFish(Fish fish) {
        	fragment.setSelectedFish(fish);
            SpearoUtils.hideSoftKeyboard(null, fragment);
		}

        @Override
        public Filter getFilter() {
            if (filter == null) {
                filter = fishFilter;
            }
            return filter;
        }

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
                for (Fish fish: new ArrayList<>(filterList)) {
                     add(fish);
                     notifyDataSetChanged();
                 }
             }
         }

    };
    
    boolean match(String latinname, String commonname, String secondary, String searched){
        List<String> accepted = new ArrayList<>();
        accepted.add(latinname.toLowerCase(Locale.ENGLISH));
        accepted.add(removeDiacriticalMarks(commonname.toLowerCase(Locale.getDefault())));
        accepted.add(removeDiacriticalMarks(secondary.toLowerCase(new Locale("el"))));
    	String searchedFiltered = removeDiacriticalMarks(searched.toLowerCase(Locale.getDefault()));
    	return stringContainedInFishNames(accepted, searchedFiltered);
    }

};