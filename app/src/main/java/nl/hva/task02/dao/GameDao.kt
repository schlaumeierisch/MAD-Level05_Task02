package nl.hva.task02.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import nl.hva.task02.model.Game

@Dao
interface GameDao {
    @Query("SELECT * FROM games ORDER BY `release`")
    fun getAllGames(): LiveData<List<Game>>

    @Insert
    fun addGame(game: Game)

    @Insert
    fun addGames(games: List<Game>)

    @Delete
    fun deleteGame(game: Game)

    @Query("DELETE FROM games")
    fun deleteAllGames()
}