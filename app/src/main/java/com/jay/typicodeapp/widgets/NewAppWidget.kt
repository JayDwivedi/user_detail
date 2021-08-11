package com.jay.typicodeapp.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import androidx.core.content.ContextCompat.startActivity
import com.jay.typicodeapp.R
import com.jay.typicodeapp.features.home.MainActivity


/**
 * Implementation of App Widget functionality.
 */
class NewAppWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.new_app_widget)
    views.setTextViewText(R.id.appwidget_text, widgetText)


    views.setOnClickPendingIntent(R.id.battery_button, getPendingIntent(context, 0))


    // Construct an Intent object includes web adresss.
    // Construct an Intent object includes web adresss.
    val intentCloud = Intent(Intent.ACTION_VIEW, Uri.parse("http://erenutku.com"))
    // In widget we are not allowing to use intents as usually. We have to use PendingIntent instead of 'startActivity'
    // In widget we are not allowing to use intents as usually. We have to use PendingIntent instead of 'startActivity'
    val pendingIntentCloud = PendingIntent.getActivity(context, 0, intentCloud, 0)
    // Here the basic operations the remote view can do.
    // Here the basic operations the remote view can do.
    views.setOnClickPendingIntent(R.id.cloud_button, pendingIntentCloud)


    views.setOnClickPendingIntent(R.id.calendar_button,  getPendingIntent(context, 1))
    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}

private fun getPendingIntent(context: Context, value: Int): PendingIntent {
    //1
    val intent = Intent(context, MainActivity::class.java)

    //4
    return PendingIntent.getActivity(context, value, intent, 0)
}