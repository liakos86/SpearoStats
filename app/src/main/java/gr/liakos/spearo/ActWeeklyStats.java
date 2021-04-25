package gr.liakos.spearo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import gr.liakos.spearo.enums.StatMode;
import gr.liakos.spearo.model.Database;
import gr.liakos.spearo.model.adapter.FishStatAdapter;
import gr.liakos.spearo.model.adapter.WeeklyStatsRecyclerViewAdapter;
import gr.liakos.spearo.model.bean.FishNumericStatistic;
import gr.liakos.spearo.model.bean.FishStatistic;
import gr.liakos.spearo.model.object.Fish;
import gr.liakos.spearo.model.object.FishAverageStatistic;
import gr.liakos.spearo.model.object.FishingSession;
import gr.liakos.spearo.util.FishingHelper;

/**
 *
 * Stats for the last 7 days.
 *
 */
public class ActWeeklyStats  extends Activity {

    List<FishNumericStatistic> weeklyStats = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_weekly_stats);
        setListView();
        setButton();
    }

    void setButton() {
        Button returnToMain = findViewById(R.id.button_close_weekly_stats);
        returnToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToMain();
            }
        });
    }

    void returnToMain() {
        Intent intent = new Intent(this, ActSpearoStatsMain.class);
        startActivity(intent);
        finish();
    }

    void setListView() {
        //List<FishingSession> weeklySessions = new Database(this).fetchFishingSessionsFromDb(AlarmManager.INTERVAL_DAY * 7);
        //WeeklyStatsRecyclerViewAdapter fishStatAdapter;
       // List<FishNumericStatistic> stats = FishingHelper.getWeeklyStats(weeklySessions);
        WeeklyStatsRecyclerViewAdapter fishStatAdapter = new WeeklyStatsRecyclerViewAdapter(this, R.layout.fish_week_stat_row, weeklyStats);
        RecyclerView fishStatsListView = findViewById(R.id.recyclerViewWeeklyStats);
        fishStatsListView.setAdapter(fishStatAdapter);
        fishStatsListView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Toast.makeText(this, "new intent", Toast.LENGTH_LONG).show();

        String weeklyStatsJson = intent.getStringExtra("weeklyStats");
        if (weeklyStatsJson == null){
            return;
        }

        weeklyStats = (List<FishNumericStatistic>) new Gson().fromJson(weeklyStatsJson, new TypeToken<List<FishNumericStatistic>>() {}.getType());

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
