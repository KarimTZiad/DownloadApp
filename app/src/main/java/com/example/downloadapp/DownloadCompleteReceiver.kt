package com.example.downloadapp

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat

class DownloadCompleteReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val openIntent = Intent(context, DetailsActivity::class.java)
        openIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        openIntent.putExtra("downloadedFileName", intent.getStringExtra("downloadedFileName"))
        openIntent.putExtra("status", intent.getStringExtra("status"))

        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.cancelAll()

        context.startActivity(openIntent)
    }
}