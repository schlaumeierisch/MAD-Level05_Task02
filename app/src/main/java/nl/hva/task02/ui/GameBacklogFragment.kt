package nl.hva.task02.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import nl.hva.task02.R
import nl.hva.task02.databinding.FragmentGameBacklogBinding
import nl.hva.task02.model.Game
import nl.hva.task02.viewmodel.GameViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class GameBacklogFragment : Fragment() {

    private var _binding: FragmentGameBacklogBinding? = null
    private val binding get() = _binding!!

    private val games = arrayListOf<Game>()
    private val gameBacklogAdapter = GameBacklogAdapter(games)

    private val viewModel: GameViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBacklogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // add a menu to the current fragment & add menu items to it
        // different actions can be assigned to each menu item
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_game_backlog, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_remove_all_games -> {
                        onDeleteAllGames()
                        true
                    }
                    else -> true
                }
            }
        }, viewLifecycleOwner)

        initRv()
        observeAddGameResult()
        observeDeleteGameResult()

        binding.fabAddGame.setOnClickListener {
            findNavController().navigate(R.id.action_gameBacklogFragment_to_addGameFragment)
        }
    }

    private fun initRv() {
        binding.rvGameBacklog.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvGameBacklog.adapter = gameBacklogAdapter

        createItemTouchHelper().attachToRecyclerView(binding.rvGameBacklog)
    }

    private fun onDeleteAllGames() {
        viewModel.deleteAllGames()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeAddGameResult() {
        viewModel.games.observe(viewLifecycleOwner) { games ->
            this@GameBacklogFragment.games.clear()
            this@GameBacklogFragment.games.addAll(games)
            gameBacklogAdapter.notifyDataSetChanged()
        }
    }

    private fun observeDeleteGameResult() {
        // game deleted message & undo functionality
        viewModel.deletedGame.observe(viewLifecycleOwner) { game ->
            Snackbar.make(binding.rvGameBacklog, getString(R.string.snackbar_remove_game, game.title), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.snackbar_undo)) {
                    viewModel.addGameAfterDelete(game)
                }.show()
        }

        // all games deleted message & undo functionality
        viewModel.deletedGames.observe(viewLifecycleOwner) { games ->
            Snackbar.make(binding.rvGameBacklog, getString(R.string.snackbar_remove_games), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.snackbar_undo)) {
                    viewModel.addGamesAfterDelete(games)
                }.show()
        }

        // error message when user tries to delete all games but the backlog is already empty
        viewModel.deleteGameError.observe(viewLifecycleOwner) { message ->
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Create a touch helper to recognize when a user swipes an item from a recycler view.
     * An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
     * and uses callbacks to signal when a user is performing these actions.
     */
    private fun createItemTouchHelper(): ItemTouchHelper {
        // Callback which is used to create the ItemTouch helper. Only enables left swipe.
        // Use ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) to also enable right swipe.
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // Enables or Disables the ability to move items up and down.
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // Callback triggered when a user swiped an item.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val game = games[position]

                viewModel.deleteGame(game)
            }
        }

        return ItemTouchHelper(callback)
    }
}