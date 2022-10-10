package nl.hva.task02.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import nl.hva.task02.databinding.FragmentAddGameBinding
import nl.hva.task02.viewmodel.GameViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AddGameFragment : Fragment() {

    private var _binding: FragmentAddGameBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GameViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabSaveGame.setOnClickListener {
            onSaveGame()
        }

        observeAddGameResult()
    }

    private fun onSaveGame() {
        viewModel.addGame(
            binding.etTitle.text.toString(),
            binding.etPlatform.text.toString(),
            binding.etReleaseYear.text.toString(),
            binding.etReleaseMonth.text.toString(),
            binding.etReleaseDay.text.toString()
        )
    }

    private fun observeAddGameResult() {
        viewModel.addGameError.observe(viewLifecycleOwner) { message ->
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }

        viewModel.addGameSuccess.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}