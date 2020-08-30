package com.overswayit.githubapi.util

import android.content.Context
import com.overswayit.githubapi.R
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun fromString(date: String): Date? {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        return simpleDateFormat.parse(date)
    }

    fun toStatusTimeString(context: Context, date: Date?): String? {
        if (date == null) {
            return ""
        }

        val secondsFromNow: Long =
            (Date().time - date.time) / 1000

        if (secondsFromNow < 300) {
            return context.resources.getString(R.string.status_time_now)
        }

        if (secondsFromNow > 86400) {
            return context.resources.getString(
                R.string.status_time_days_ago,
                (secondsFromNow / 86400).toString()
            )
        }

        return if (secondsFromNow > 3600) {
            context.resources.getString(
                R.string.status_time_hours_ago,
                (secondsFromNow / 3600).toString()
            )
        } else context.resources.getString(
            R.string.status_time_minutes_ago,
            (secondsFromNow / 60).toString()
        )
    }
}