package gr.liakos.spearo.custom.notification;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;

import java.util.List;

import gr.liakos.spearo.ActSpearoStatsMain;
import gr.liakos.spearo.ActWeeklyStats;
import gr.liakos.spearo.R;
import gr.liakos.spearo.model.Database;
import gr.liakos.spearo.model.bean.FishNumericStatistic;
import gr.liakos.spearo.model.object.FishingSession;
import gr.liakos.spearo.util.FishingHelper;

public class NotificationHelper {

    private final Context mContext;
    private static final String NOTIFICATION_CHANNEL_ID = "10001";

    NotificationHelper(Context context) {
        mContext = context;
    }

    void createNotification()
    {

        Toast.makeText(mContext, "alert", Toast.LENGTH_LONG).show();

//        List<FishingSession> weeklySessions = new Database(mContext).fetchFishingSessionsFromDb(AlarmManager.INTERVAL_DAY * 7);
//        List<FishNumericStatistic> stats = FishingHelper.getWeeklyStats(weeklySessions);
        Intent intent = new Intent(mContext , ActWeeklyStats.class);
        intent.putExtra("weeklyStats", true);


        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,
                0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID);
        mBuilder.setSmallIcon(R.drawable.stats);
        mBuilder.setContentTitle(mContext.getResources().getString(R.string.weekly_stats))
                .setContentText(mContext.getResources().getString(R.string.weekly_stats_analyzed))
                .setAutoCancel(true)
                .addAction(R.drawable.dive_30, mContext.getResources().getString(R.string.open), resultPendingIntent)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(0 /* Request Code */, mBuilder.build());
    }
}
