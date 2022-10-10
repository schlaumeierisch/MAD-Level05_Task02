package nl.hva.task02.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nl.hva.task02.model.Game
import nl.hva.task02.repository.GameRepository
import nl.hva.task02.util.DateUtils

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val ioScope = CoroutineScope(Dispatchers.IO)
    private val gameRepository = GameRepository(application.applicationContext)

    val games = gameRepository.getAllGames()
    val addGameError = MutableLiveData<String>()
    val addGameSuccess = MutableLiveData<Boolean>()

    val deleteGameError = MutableLiveData<String>()
    val deletedGame = MutableLiveData<Game>()
    val deletedGames = MutableLiveData<List<Game>>()

    private val dateUtils = DateUtils()

    fun addGame(title: String, platform: String, year: String, month: String, day: String) {
        if (inputIsNotBlank(title, platform, year, month, day)) {
            if (dateUtils.isValidDate(year.toInt(), month.toInt(), day.toInt())) {
                // parameter release: "- 1" is needed because month starts at 0
                val game = Game(
                    title,
                    platform,
                    dateUtils.createDate(year.toInt(), month.toInt() - 1, day.toInt())
                )

                ioScope.launch {
                    gameRepository.addGame(game)
                }
                addGameSuccess.value = true
            } else {
                addGameError.value = "Please fill in a valid date"
            }
        }
    }

    fun addGameAfterDelete(game: Game) {
        ioScope.launch {
            gameRepository.addGame(game)
        }
        addGameSuccess.value = true
    }

    fun addGamesAfterDelete(games: List<Game>) {
        ioScope.launch {
            gameRepository.addGames(games)
        }
        addGameSuccess.value = true
    }

    private fun inputIsNotBlank(
        title: String,
        platform: String,
        year: String,
        month: String,
        day: String
    ): Boolean {
        return when {
            title.isBlank() -> {
                addGameError.value = "Please fill in a title"
                false
            }
            platform.isBlank() -> {
                addGameError.value = "Please fill in a platform"
                false
            }
            year.isBlank() -> {
                addGameError.value = "Please fill in a release year"
                false
            }
            month.isBlank() -> {
                addGameError.value = "Please fill in a release month"
                false
            }
            day.isBlank() -> {
                addGameError.value = "Please fill in a release day"
                false
            }
            else -> true
        }
    }

    fun deleteGame(game: Game) {
        ioScope.launch {
            gameRepository.deleteGame(game)
        }
        deletedGame.value = game
    }

    fun deleteAllGames() {
        if (!games.value.isNullOrEmpty()) {
            val gamesToDelete: List<Game> = games.value!!
            ioScope.launch {
                gameRepository.deleteAllGames()
            }
            deletedGames.value = gamesToDelete
        } else {
            deleteGameError.value = "There are no games to delete"
        }
    }
}