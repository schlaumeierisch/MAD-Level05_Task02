package nl.hva.task02.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import nl.hva.task02.dao.GameDao
import nl.hva.task02.model.Game
import nl.hva.task02.util.Converters

@Database(entities = [Game::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class GameBacklogRoomDatabase: RoomDatabase() {
    abstract fun gameDao(): GameDao

    companion object {
        private const val DATABASE_NAME = "GAME_DATABASE"

        @Volatile
        private var gameBacklogRoomDatabaseInstance: GameBacklogRoomDatabase? = null

        fun getDatabase(context: Context): GameBacklogRoomDatabase? {
            if (gameBacklogRoomDatabaseInstance == null) {
                synchronized(GameBacklogRoomDatabase::class.java) {
                    if (gameBacklogRoomDatabaseInstance == null) {
                        gameBacklogRoomDatabaseInstance = Room.databaseBuilder(
                            context.applicationContext,
                            GameBacklogRoomDatabase::class.java, DATABASE_NAME
                        ).build()
                    }
                }
            }
            return gameBacklogRoomDatabaseInstance
        }
    }
}