package com.astriex.catsvsdogs.ui.fragments.home.dogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.astriex.catsvsdogs.R
import com.astriex.catsvsdogs.databinding.FragmentDogsBinding
import com.astriex.catsvsdogs.db.Vote
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DogsFragment : Fragment(R.layout.fragment_dogs), DogVoteListener {
    private val viewModel by viewModels<DogsViewModel>()
    private var _binding: FragmentDogsBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dogs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentDogsBinding.bind(view)

        val adapter = DogsPhotoAdapter(this)
        binding.recyclerView.adapter = adapter

        viewModel.getAllDogs().observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    override fun saveDogVote() {
        var oldVote: Vote? = null
        CoroutineScope(Dispatchers.IO).launch {
            oldVote = viewModel.getDogVotes()
            viewModel.saveVote(Vote(oldVote!!.count.plus(1), oldVote!!.catOrDog))
        }
    }
}