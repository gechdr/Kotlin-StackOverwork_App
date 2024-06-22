package com.example.tugasm7_6958

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class,QuestionEntity::class,AnswerEntity::class], version = 5)
abstract class AppDatabase: RoomDatabase(){
    abstract fun userDao(): UserDao
    abstract fun questionDao(): QuestionDao
    abstract fun answerDao(): AnswerDao

    companion object {
        private var _database: AppDatabase? = null

        fun build(context: Context?): AppDatabase {
            if(_database == null){
                _database = Room.databaseBuilder(context!!,AppDatabase::class.java,"tugasm7").fallbackToDestructiveMigration().build()
            }
            return _database!!
        }
    }
}