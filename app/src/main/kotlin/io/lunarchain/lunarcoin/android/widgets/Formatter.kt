package io.lunarchain.lunarcoin.android.widgets

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class Formatter {

    companion object {

        fun formatNumber(number: Long): String = DecimalFormat(",###").format(number)

        fun formatTime(time: Long): String = SimpleDateFormat("yyyy-MM-dd HH:m:s").format(Date(time))

    }
}