package org.team_alilm.global.util

import java.text.SimpleDateFormat

object DateFormatter {

    fun dateFormatter(date: Long) : String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)
    }
}