package nl.hva.task02.repository

import android.content.Context
import androidx.lifecycle.LiveData
import nl.hva.task02.dao.GameDao
import nl.hva.task02.database.GameBacklogRoomDatabase
import nl.hva.task02.model.Game

class GameRepository(context: Context) {
    private var gameDao: GameDao

    init {
        val reminderRoomDatabase = GameBacklogRoomDatabase.getDatabase(context)
        gameDao = reminderRoomDatabase!!.gameDao()
    }

    fun getAllGames(): LiveData<List<Game>> = gameDao.getAllGames()

    fun addGame(game: Game) = gameDao.addGame(game)

    fun addGames(games: List<Game>) = gameDao.addGames(games)

    fun deleteGame(game: Game) = gameDao.deleteGame(game)

    fun deleteAllGames() = gameDao.deleteAllGames()
}