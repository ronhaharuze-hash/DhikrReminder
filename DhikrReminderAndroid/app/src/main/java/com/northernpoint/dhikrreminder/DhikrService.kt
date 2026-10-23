package com.northernpoint.dhikrreminder

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.speech.tts.TextToSpeech
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

class DhikrService : Service(), TextToSpeech.OnInitListener {
    companion object {
        const val ACTION_START = "com.northernpoint.dhikrreminder.START"
        private const val CHANNEL_ID = "dhikr_spoken_reminders"
        private const val NOTIFICATION_ID = 501
        private const val INTERVAL_MS = 5 * 60 * 1000L
    }

    private var tts: TextToSpeech? = null
    private var timer: Timer? = null
    private var ttsReady = false
    private var index = 0
    private var wakeLock: PowerManager.WakeLock? = null
    private val phrases = listOf(
        "أستغفر الله",
        "الله أكبر",
        "سبحان الله",
        "الحمد لله",
        "لا إله إلا الله"
    )

    override fun onCreate() {
        super.onCreate()
        createChannel()
        tts = TextToSpeech(this, this)
        val pm = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DhikrReminder:Speech").apply {
            setReferenceCounted(false)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, makeNotification())
        if (wakeLock?.isHeld != true) wakeLock?.acquire(10 * 60 * 1000L)
        schedule()
        return START_STICKY
    }

    private fun schedule() {
        timer?.cancel()
        timer = Timer("DhikrReminderTimer", true)
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                // Renew the partial wake lock so the timer and speech can run while the display is off.
                if (wakeLock?.isHeld != true) {
                    wakeLock?.acquire(10 * 60 * 1000L)
                } else {
                    wakeLock?.acquire(10 * 60 * 1000L)
                }
                if (ttsReady) {
                    val phrase = phrases[index % phrases.size]
                    index++
                    tts?.speak(phrase, TextToSpeech.QUEUE_FLUSH, null, "dhikr-${System.currentTimeMillis()}")
                }
            }
        }, INTERVAL_MS, INTERVAL_MS)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale("ar"))
            ttsReady = result != TextToSpeech.LANG_MISSING_DATA &&
                    result != TextToSpeech.LANG_NOT_SUPPORTED
        }
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, "Spoken dhikr reminders", NotificationManager.IMPORTANCE_LOW
            ).apply { description = "Keeps spoken dhikr reminders active every five minutes." }
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
        }
    }

    private fun makeNotification(): Notification {
        val open = PendingIntent.getActivity(
            this, 0, Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= 23) PendingIntent.FLAG_IMMUTABLE else 0)
        )
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            Notification.Builder(this, CHANNEL_ID) else Notification.Builder(this)
        return builder
            .setContentTitle("Dhikr Reminder is active")
            .setContentText("Spoken dhikr repeats every 5 minutes")
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentIntent(open)
            .setOngoing(true)
            .build()
    }

    override fun onDestroy() {
        timer?.cancel()
        timer = null
        tts?.stop()
        tts?.shutdown()
        tts = null
        if (wakeLock?.isHeld == true) wakeLock?.release()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
