package ru.ifmo.ctddev.skripnikov.lesson6;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

public class PlayService extends Service{

    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        mediaPlayer = MediaPlayer.create(this, R.raw.fuck);
        mediaPlayer.setLooping(true);
        int icon = R.drawable.fuck;
        long when = System.currentTimeMillis();

        Notification notification = new Notification(icon, "Fuck!!!", when);
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(), 0, notificationIntent, 0);
        notification.setLatestEventInfo(getBaseContext(), "Fuck", "", contentIntent);
        startForeground(1, notification);
    }

    @Override
    public void onStart(Intent intent, int id) {
        mediaPlayer.start();
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
