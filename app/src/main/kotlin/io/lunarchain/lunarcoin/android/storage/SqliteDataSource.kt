package io.lunarchain.lunarcoin.android.storage

import io.lunarchain.lunarcoin.storage.DataSource

class SqliteDataSource(override val name: String, val dbHelper: SqliteDbHelper): DataSource<ByteArray, ByteArray> {
    override fun get(key: ByteArray): ByteArray? {
        return dbHelper.getValue(name, key)
    }

    override fun put(key: ByteArray, value: ByteArray) {
        dbHelper.putValue(name, key, value)
    }

    override fun delete(key: ByteArray) {
        dbHelper.deleteValue(name, key)
    }

    override fun flush(): Boolean {
        //DO NOTHING
        return true
    }

    override fun init() {
        //DO NOTHING
    }

    override fun isAlive(): Boolean {
        return true
    }

    override fun close() {
        //DO NOTHING
    }

    override fun updateBatch(rows: Map<ByteArray, ByteArray?>) {
        for (row in rows) {
            if (row.value!=null){ put(row.key, row.value!!)}
        }
    }

    override fun keys(): Set<ByteArray> {
        return dbHelper.listKeys(name)
    }

    override fun reset() {
        //DO NOTHING
    }
}