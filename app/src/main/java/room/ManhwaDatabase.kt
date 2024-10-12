package room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Manhwa::class], version = 1)
abstract class ManhwaDatabase: RoomDatabase(){
    abstract fun manhwaDao(): ManhwaDao

    companion object {
        @Volatile
        private var INSTANCE: ManhwaDatabase? = null

        fun getDatabase(context: Context): ManhwaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ManhwaDatabase::class.java,
                    "manhwa_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}