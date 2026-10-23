package com.northernpoint.dhikrreminder

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 33 &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 10)
        }
        render()
    }

    private fun render() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(dp(24), dp(24), dp(24), dp(24))
            setBackgroundColor(0xFFF5F7F2.toInt())
        }
        val title = TextView(this).apply {
            text = "Dhikr Reminder"
            textSize = 30f
            gravity = Gravity.CENTER
            setTextColor(0xFF14532D.toInt())
        }
        val subtitle = TextView(this).apply {
            text = "Spoken dhikr every 5 minutes\n\nأستغفر الله\nالله أكبر\nسبحان الله\nالحمد لله\nلا إله إلا الله"
            textSize = 20f
            gravity = Gravity.CENTER
            setTextColor(0xFF1F2937.toInt())
            setPadding(0, dp(20), 0, dp(24))
        }
        val start = Button(this).apply {
            text = "Start reminders"
            setOnClickListener {
                val i = Intent(this@MainActivity, DhikrService::class.java).setAction(DhikrService.ACTION_START)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startForegroundService(i) else startService(i)
                Toast.makeText(this@MainActivity, "Dhikr reminders started", Toast.LENGTH_SHORT).show()
            }
        }
        val stop = Button(this).apply {
            text = "Pause reminders"
            setOnClickListener {
                stopService(Intent(this@MainActivity, DhikrService::class.java))
                Toast.makeText(this@MainActivity, "Dhikr reminders paused", Toast.LENGTH_SHORT).show()
            }
        }
        val note = TextView(this).apply {
            text = "Keep the service running for reminders while the screen is locked. Your phone may ask you to allow notifications and unrestricted battery use."
            textSize = 14f
            gravity = Gravity.CENTER
            setPadding(0, dp(22), 0, 0)
            setTextColor(0xFF4B5563.toInt())
        }
        root.addView(title)
        root.addView(subtitle)
        root.addView(start, LinearLayout.LayoutParams(-1, -2))
        root.addView(stop, LinearLayout.LayoutParams(-1, -2))
        root.addView(note)
        setContentView(root)
    }
}
