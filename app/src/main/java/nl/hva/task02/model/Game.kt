package nl.hva.task02.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "games")
data class Game(
    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "platform")
    val platform: String,

    @ColumnInfo(name = "release")
    val release: Date,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long? = null
)
