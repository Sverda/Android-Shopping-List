package com.s24083.shoppinglist.ui.redirectWidget

import android.app.Activity
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.s24083.shoppinglist.R
import com.s24083.shoppinglist.databinding.ActivityRedirectWidgetConfigBinding
import com.s24083.shoppinglist.ui.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL
import java.util.*


class RedirectWidgetConfigActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRedirectWidgetConfigBinding
    private var mediaPlayer = MediaPlayer()
    private var currentlyPlayingTitle = "Scott Holmes Music - Lasting Memories.mp3"

    var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRedirectWidgetConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.widgetConfigRandomImage.setOnClickListener {
            drawRandomImage()
        }
        binding.widgetConfigSoundsGroup.setOnCheckedChangeListener { group, checkedId ->
            mediaPlayer.stop()
            mediaPlayer.reset()
            val radioButton: RadioButton = group.findViewById(checkedId)
            currentlyPlayingTitle = radioButton.text.toString() + ".mp3"
            val afd = assets.openFd(currentlyPlayingTitle)
            mediaPlayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length);
        }
        binding.widgetConfigPlay.setOnClickListener {
            playSound()
        }
        binding.widgetConfigStop.setOnClickListener {
            stopSound()
        }
        binding.widgetConfigDone.setOnClickListener {
            done()
        }
        binding.widgetConfigCancel.setOnClickListener {
            cancel()
        }

        // Find the widget id from the intent.
        appWidgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            cancel()
            return
        }

        val afd = assets.openFd(currentlyPlayingTitle)
        mediaPlayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length);
    }

    private fun drawRandomImage() {
        GlobalScope.launch(Dispatchers.IO) {
            val url = URL("https://picsum.photos/650/160")
            val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            launch(Dispatchers.Main) {
                binding.widgetConfigImage.setImageBitmap(bmp)
            }
        }
    }

    private fun playSound() {
        if (!mediaPlayer.isPlaying){
            mediaPlayer.prepare();
            mediaPlayer.start()
        }
    }

    private fun stopSound() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
    }

    private fun done() {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val views = RemoteViews(this.packageName, R.layout.redirect_widget)

        binding.widgetConfigUrl.text?.toString()?.let { url ->
            var validatedUrl = url
            if (!validatedUrl.startsWith("http://") || !validatedUrl.startsWith("https://")) {
                validatedUrl = "http://$url"
            }

            views.setTextViewText(R.id.widget_url, validatedUrl)
            // Create a pending intent from the detail activity intent
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("url", validatedUrl)
            val pendingIntent = PendingIntent.getActivity(
                this,
                UUID.randomUUID().hashCode(),
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT)
            views.setOnClickPendingIntent(R.id.widget_redirect, pendingIntent);
            views.setTextViewText(R.id.widget_ambient_title, currentlyPlayingTitle)
        }
        binding.widgetConfigImage.drawable?.let { drawable ->
            views.setImageViewBitmap(R.id.widget_background, drawable.toBitmap())
        }

        appWidgetManager.updateAppWidget(appWidgetId, views)
        val resultValue = Intent()
            .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(Activity.RESULT_OK, resultValue)
        finish()
    }

    private fun cancel() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }
}