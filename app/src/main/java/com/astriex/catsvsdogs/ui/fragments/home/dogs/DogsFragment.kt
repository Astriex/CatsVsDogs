package com.astriex.catsvsdogs.ui.fragments.home.dogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.astriex.catsvsdogs.R
import com.astriex.catsvsdogs.data.networking.unsplashPhotos.UnsplashPhoto
import com.astriex.catsvsdogs.data.networking.unsplashPhotos.UnsplashPhotoLoadStateAdapter
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

        val adapter = DogsPhotoAdapter(this, requireContext())
        binding.recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
            header = UnsplashPhotoLoadStateAdapter { adapter.retry() },
            footer = UnsplashPhotoLoadStateAdapter { adapter.retry() }
        )
        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                btnRetry.isVisible = loadState.source.refresh is LoadState.Error
                tvError.isVisible = loadState.source.refresh is LoadState.Error

                btnRetry.setOnClickListener {
                    adapter.retry()
                }
            }
        }

        viewModel.getAllDogs("dog").observe(viewLifecycleOwner) {
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

    override fun openDetails(photo: UnsplashPhoto) {
        val action = DogsFragmentDirections.actionDogsFragmentToDetailsFragment(photo)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}