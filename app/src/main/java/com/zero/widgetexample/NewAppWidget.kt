package com.zero.widgetexample

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import java.text.DateFormat
import java.util.*


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

private const val mSharedFile: String = BuildConfig.APPLICATION_ID
private const val COUNT_KEY = "count"

@SuppressLint("UnspecifiedImmutableFlag")
internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.new_app_widget)
    views.setTextViewText(R.id.appwidget_id, widgetText)

    val pref = context.getSharedPreferences(mSharedFile, 0)
    val editor = pref.edit()

    var count = pref.getInt(COUNT_KEY + appWidgetId, 0)
    count++

    editor.putInt(COUNT_KEY + appWidgetId, count).apply()

    val currentTime: String = DateFormat.getTimeInstance(DateFormat.SHORT).format(Date())
    val outputUpdate = "$count @$currentTime"

    views.setTextViewText(R.id.appwidget_update, outputUpdate)

    val intentUpdate = Intent(context, NewAppWidget::class.java)
    intentUpdate.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE

    val idsArray = intArrayOf(appWidgetId)
    intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idsArray)

    val pendingUpdate = PendingIntent.getBroadcast(
        context,
        appWidgetId,
        intentUpdate,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    views.setOnClickPendingIntent(R.id.update_btn, pendingUpdate)
    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}