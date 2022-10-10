package nl.hva.task02.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nl.hva.task02.R
import nl.hva.task02.model.Game
import nl.hva.task02.databinding.ItemGameBinding
import java.text.SimpleDateFormat
import java.util.*

class GameBacklogAdapter(private val gameBacklog: List<Game>): RecyclerView.Adapter<GameBacklogAdapter.ViewHolder>() {
    // context needed to access resources
    private lateinit var context: Context

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = ItemGameBinding.bind(itemView)

        // formatter to get date format "12 January 1997"
        private val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

        fun databind(game: Game) {
            binding.tvGameTitle.text = game.title
            binding.tvPlatform.text = game.platform
            binding.tvReleaseDate.text = context.resources.getString(R.string.item_game_release, formatter.format(game.release))
        }
    }

    /**
     * Needed to get the Recycler View's context
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        context = recyclerView.context
    }

    /**
     * Creates and returns a ViewHolder object, inflating a standard layout called simple_list_item_1.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        )
    }

    /**
     * Returns the size of the list
     */
    override fun getItemCount(): Int {
        return gameBacklog.size
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(gameBacklog[position])
    }
}