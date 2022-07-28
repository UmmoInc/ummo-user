package xyz.ummo.bite.localdatabase.maindatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import xyz.ummo.bite.localdatabase.Dao.ProfileDao
import xyz.ummo.bite.localdatabase.models.Profile

@Database
(entities = [Profile::class],version=1, exportSchema = false)
abstract class Database : RoomDatabase(){
    abstract  fun getProfileDao(): ProfileDao
    //initialize database
    companion object {
        @Volatile// other threads can immediately see when a thread changes this instance
        private var INSTANCE: xyz.ummo.bite.localdatabase.maindatabase.Database?=null
        fun getDatabase(context: Context): xyz.ummo.bite.localdatabase.maindatabase.Database {
            val tempInstance = INSTANCE
 if(tempInstance!=null){
     return tempInstance

 }

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    xyz.ummo.bite.localdatabase.maindatabase.Database::class.java,
                    "main_database")
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                return instance
            }
        }

    }

        }




