package gr.liakos.spearo.model.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gr.liakos.spearo.R;
import gr.liakos.spearo.model.bean.FishNumericStatistic;
import gr.liakos.spearo.model.object.FishAverageStatistic;
import gr.liakos.spearo.util.SpearoUtils;


public class WeeklyStatsRecyclerViewAdapter extends RecyclerView.Adapter {

    List<FishNumericStatistic> stats;
    Context mContext;
    int layoutResourceId;

    public WeeklyStatsRecyclerViewAdapter(Context context, int layoutResourceId, List<FishNumericStatistic> stats) {
        this.stats = stats;
        mContext = context;
        this.layoutResourceId = layoutResourceId;
    }

    public class WeeklyStatsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public ImageView fishIcon;
        public TextView weeklyStatFishNameText;
        public TextView weeklyCatchesNumText;

        public WeeklyStatsViewHolder(View v) {
            super(v);

            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
            weeklyCatchesNumText = v.findViewById(R.id.weeklyCatchesNumText);
            weeklyStatFishNameText = v.findViewById(R.id.weeklyStatFishNameText);
            fishIcon = v.findViewById(R.id.statImageViewFish);
        }

        public void setData(FishNumericStatistic item) {
            fishIcon.setImageDrawable(new SpearoUtils(mContext).getDrawableFromFish(item.getFish()));
            weeklyCatchesNumText.setText(item.getTotalCatches() + "");
            weeklyStatFishNameText.setText(item.getFish().getCommonName());
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    @Override
    public WeeklyStatsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.fish_week_stat_row, parent, false);

        return new WeeklyStatsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((WeeklyStatsViewHolder)holder).setData(stats.get(position));
    }

    @Override
    public int getItemCount() {

        return stats.size();
    }

}
