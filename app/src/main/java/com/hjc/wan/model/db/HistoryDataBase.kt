package com.hjc.wan.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [History::class], version = 1)
abstract class HistoryDataBase : RoomDatabase() {

    // 必须包含一个具有0个参数且返回带@Dao注释的类的抽象方法
    abstract fun getHistoryDao(): HistoryDao

    companion object {
        private var appDataBase: HistoryDataBase? = null

        fun getInstance(context: Context): HistoryDataBase {
            if (appDataBase == null) {
                synchronized(HistoryDataBase::class.java) {
                    appDataBase = Room.databaseBuilder(
                        context.applicationContext,
                        HistoryDataBase::class.java,
                        "user_database"
                    ).allowMainThreadQueries().build()
                }
            }
            return appDataBase!!
        }
    }
}