package com.example.downloadapp

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 183
    private var downloadFileName = ""

    private lateinit var notificationManager: NotificationManager

    private lateinit var customButton : LoadingButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        createChannel(
            applicationContext.getString(R.string.notification_channel_id),
            applicationContext.getString(R.string.notification_channel_name)
        )

        notificationManager = ContextCompat.getSystemService(
            application,
            NotificationManager::class.java
        ) as NotificationManager

        customButton = findViewById<LoadingButton>(R.id.custom_button)

        customButton.setOnClickListener {
            val radioGroup = findViewById<RadioGroup>(R.id.downloadRadioGroup)
            val radioButton = findViewById<RadioButton>(radioGroup.checkedRadioButtonId)
            val selectedId = radioGroup.indexOfChild(radioButton)
            downloadFileName = radioButton.text.toString()
            if(selectedId==-1){
                Toast.makeText(applicationContext, getString(R.string.select_download_option), Toast.LENGTH_LONG).show()
            }
            else{
                notificationManager.cancelAll()
                download(selectedId)
                customButton.startAnimation()
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if(downloadID == id){
                customButton.stopAnimation()
                Toast.makeText(applicationContext, "Done!",Toast.LENGTH_SHORT).show()
                notificationManager.sendNotification(
                    applicationContext.getString(R.string.notification_description),
                    downloadFileName,
                    application
                )
            }
        }
    }

    private fun download(selected: Int) {
        val request =
            DownloadManager.Request(Uri.parse(URLS[selected]))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private val URLS = listOf(
            "https://github.com/bumptech/glide/archive/refs/heads/master.zip",
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip",
            "https://github.com/square/retrofit/archive/refs/heads/master.zip"
        )
    }

    private fun createChannel(channelId: String, channelName: String) {
        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.apply {
            setShowBadge(false)
            enableLights(true)
            lightColor = Color.BLUE
            enableVibration(false)
            description = applicationContext.getString(R.string.notification_description)
        }
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)

    }

}