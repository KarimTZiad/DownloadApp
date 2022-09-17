package com.example.downloadapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        findViewById<Button>(R.id.okayButton).setOnClickListener {
            finish()
        }
        findViewById<TextView>(R.id.fileNameValueTextView).text = intent.getStringExtra("downloadedFileName")
        findViewById<TextView>(R.id.statusValueTextView).text = intent.getStringExtra("status")
    }
}