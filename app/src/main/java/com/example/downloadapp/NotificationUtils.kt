package com.example.downloadapp

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

const val NOTIFICATION_ID = 778
const val REQUEST_CODE = 0


    fun NotificationManager.sendNotification(messageBody: String, downloadedFileName: String, applicationContext: Context) {

        val contentIntent = Intent(applicationContext, DetailsActivity::class.java)
        contentIntent.putExtra("downloadedFileName", downloadedFileName)
        contentIntent.putExtra("status","Success")
        val contentPendingIntent = PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT + PendingIntent.FLAG_IMMUTABLE
        )

        val actionIntent = Intent(applicationContext, DownloadCompleteReceiver::class.java)
        actionIntent.putExtra("downloadedFileName", downloadedFileName)
        actionIntent.putExtra("status","Success")
        val actionPendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            REQUEST_CODE,
            actionIntent,
            PendingIntent.FLAG_UPDATE_CURRENT + PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(
            applicationContext,
            applicationContext.getString(R.string.notification_channel_id)
        )
            .setSmallIcon(R.drawable.ic_round_file_download_32)
            .setContentTitle(applicationContext
                .getString(R.string.notification_title))
            .setContentText(messageBody)
            .setContentIntent(contentPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .addAction(
                R.drawable.ic_round_file_download_32,
                applicationContext.getString(R.string.notification_button),
                actionPendingIntent
            )

        notify(NOTIFICATION_ID, builder.build())
    }