package io.lunarchain.lunarcoin.android.storage

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.spongycastle.util.encoders.Hex

class SqliteDbHelper(context: Context?) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private fun getCreateTableScript(tableName: String): String {
        return "CREATE TABLE $tableName (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$FIELD_DATA_KEY TEXT NOT NULL, " +
                "$FIELD_DATA_VAL BLOB " +
                ")"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(getCreateTableScript(TABLE_NAME_ACCOUNT_STATE))
        db?.execSQL(getCreateTableScript(TABLE_NAME_ACCOUNT))
        db?.execSQL(getCreateTableScript(TABLE_NAME_BLOCK))
        db?.execSQL(getCreateTableScript(TABLE_NAME_BLOCK_INDEX))
        db?.execSQL(getCreateTableScript(TABLE_NAME_BLOCK_TRANSACTION))
        db?.execSQL(getCreateTableScript(TABLE_NAME_BEST_BLOCK))
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    companion object {
        private const val DATABASE_NAME = "test"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME_ACCOUNT_STATE = "account_state"
        const val TABLE_NAME_ACCOUNT = "account"
        const val TABLE_NAME_BLOCK = "block"
        const val TABLE_NAME_BLOCK_INDEX = "block_index"
        const val TABLE_NAME_BLOCK_TRANSACTION = "block_transaction"
        const val TABLE_NAME_BEST_BLOCK = "best_block"

        const val FIELD_DATA_KEY = "data_key"
        const val FIELD_DATA_VAL = "data_val"
    }

    fun getValue(tableName: String, key: ByteArray): ByteArray? {
        val cursor = readableDatabase.query(tableName, arrayOf(FIELD_DATA_VAL),
                "$FIELD_DATA_KEY=?", arrayOf(Hex.toHexString(key)),
                null, null, null)

        cursor.use {
            if (it.moveToNext()) {
                return it.getBlob(it.getColumnIndex(FIELD_DATA_VAL))
            } else {
                return null
            }
        }
    }

    fun putValue(name: String, key: ByteArray, value: ByteArray) {
        val values = ContentValues()
        val keyStr = Hex.toHexString(key)
        values.put("data_key", keyStr)
        values.put("data_val", value)

        val existValue = getValue(name, key)
        if (existValue != null) {
            writableDatabase.update(name, values, "$FIELD_DATA_KEY=?", arrayOf(keyStr))
        } else {
            writableDatabase.insert(name, null, values)
        }
    }

    fun deleteValue(name: String, key: ByteArray) {
        val values = ContentValues()
        val keyStr = Hex.toHexString(key)
        values.put("data_key", keyStr)

        writableDatabase.delete(name, "$FIELD_DATA_KEY=?", arrayOf(keyStr))
    }

    fun listKeys(name: String): Set<ByteArray> {
        val result: MutableSet<ByteArray> = hashSetOf()

        val cursor = readableDatabase.query(name, arrayOf(FIELD_DATA_KEY),
                null, null,
                null, null, null)

        cursor.use {
            while (it.moveToNext()) {
                result.add(it.getBlob(it.getColumnIndex(FIELD_DATA_KEY)))
            }
        }
        return result
    }

}