package io.lunarchain.lunarcoin.android.model.wallet

data class Record(
    var id: String,
    var type: String,
    var typeName: String,
    var amount: Long,
    var time: Long
)
