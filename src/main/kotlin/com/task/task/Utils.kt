package com.task.task

import java.util.*

object Utils {
    private var uniqueCount = 1


        val uniqueId: String
            get() {
                val calendar = Calendar.getInstance()
                var uniqueId: String
                val hr = calendar[Calendar.HOUR_OF_DAY]
                val min = calendar[Calendar.MINUTE]
                val sec = calendar[Calendar.SECOND]
                val day = calendar[Calendar.DATE]
                val month = 1 + calendar[Calendar.MONTH]
                val year = calendar[Calendar.YEAR]
                uniqueId = "" + year
                if (month > 9) {
                    uniqueId += month
                } else {
                    uniqueId += "0$month"
                }
                if (day > 9) {
                    uniqueId += day
                } else {
                    uniqueId += "0$day"
                }
                if (hr > 9) {
                    uniqueId += hr
                } else {
                    uniqueId += "0$hr"
                }
                if (min > 9) {
                    uniqueId += min
                } else {
                    uniqueId += "0$min"
                }
                if (sec > 9) {
                    uniqueId += sec
                } else {
                    uniqueId += "0$sec"
                }
                if (uniqueCount < 9) {
                    uniqueId = uniqueId + "000" + uniqueCount
                    ++uniqueCount
                } else if (uniqueCount < 99) {
                    uniqueId = uniqueId + "00" + uniqueCount
                    ++uniqueCount
                } else if (uniqueCount < 999) {
                    uniqueId = uniqueId + "0" + uniqueCount
                    ++uniqueCount
                } else {
                    uniqueId = uniqueId + "0" + uniqueCount
                    uniqueCount = 1
                }
                return uniqueId
            }

}