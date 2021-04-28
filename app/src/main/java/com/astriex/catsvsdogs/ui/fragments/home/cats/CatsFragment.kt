package com.astriex.catsvsdogs.ui.fragments.home.cats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.astriex.catsvsdogs.R
import com.astriex.catsvsdogs.databinding.FragmentCatsBinding
import com.astriex.catsvsdogs.db.Vote
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CatsFragment : Fragment(R.layout.fragment_cats), CatVoteListener {
    private val viewModel by viewModels<CatsViewModel>()
    private var _binding: FragmentCatsBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCatsBinding.bind(view)

        val adapter = CatsPhotoAdapter(this, requireContext())
        binding.recyclerView.adapter = adapter

        viewModel.getCatPhotos("cat").observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    override fun saveCatVote() {
        var oldVote: Vote? = null
        CoroutineScope(Dispatchers.IO).launch {
            oldVote = viewModel.getCatVotes()
            viewModel.saveVote(Vote(oldVote!!.count.plus(1), oldVote!!.catOrDog))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}