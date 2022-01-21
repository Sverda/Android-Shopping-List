package com.s24083.shoppinglist.ui.redirectWidget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import com.s24083.shoppinglist.R
import com.s24083.shoppinglist.databinding.ActivityRedirectWidgetConfigBinding

class RedirectWidgetConfigActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRedirectWidgetConfigBinding

    var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRedirectWidgetConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
    }

    private fun done() {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val views = RemoteViews(this.packageName, R.layout.redirect_widget)

        views.setTextViewText(R.id.widget_url, binding.widgetConfigUrl.text)

        appWidgetManager.partiallyUpdateAppWidget(appWidgetId, views)
        val resultValue = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(Activity.RESULT_OK, resultValue)
        finish()
    }

    private fun cancel() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }
}