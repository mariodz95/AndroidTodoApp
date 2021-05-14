package com.example.todoapp.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.compose.material.R
import androidx.core.app.NotificationCompat
import com.example.todoapp.MainActivity

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra("notificationId", 0)
        val message = intent.getStringExtra("notificationText")
        val requestCode = intent.getIntExtra("requestCode", 0)

        val mainIntent  = Intent(context, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(context,requestCode, mainIntent, 0)

        val myNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            var channelName = "My Notification"
            var importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel("channel_id", channelName, importance)
            myNotificationManager.createNotificationChannel(channel)
        }

        val actionIntent = PendingIntent.getBroadcast(context, requestCode, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, "channel_id" )
            .setContentTitle("Hey, new task awaiting you!")
            .setContentText(message)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.notification_icon_background)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(contentIntent)
            .build()

        myNotificationManager.notify(notificationId, notification)
    }
}