package com.astriex.catsvsdogs.ui.fragments.home.versus

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.astriex.catsvsdogs.R
import com.astriex.catsvsdogs.data.cats.CatResponse
import com.astriex.catsvsdogs.data.dogs.DogResponse
import com.astriex.catsvsdogs.databinding.FragmentVersusBinding
import com.astriex.catsvsdogs.db.Vote
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

@AndroidEntryPoint
class VersusFragment : Fragment(R.layout.fragment_versus) {
    private var _binding: FragmentVersusBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel by viewModels<VersusViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_versus, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentVersusBinding.bind(view)

        binding.ivCatVote.setOnClickListener {
            loadImages()
            saveCatVote()
        }

        binding.ivDogVote.setOnClickListener {
            loadImages()
            saveDogVote()
        }
    }

    private fun saveDogVote() {
        var oldVote: Vote? = null
        CoroutineScope(Dispatchers.IO).launch {
            oldVote = viewModel.getDogVotes()
            viewModel.saveVote(Vote(oldVote!!.count.plus(1), oldVote!!.catOrDog))
        }
    }

    private fun saveCatVote() {
        var oldVote: Vote? = null
        CoroutineScope(Dispatchers.IO).launch {
            oldVote = viewModel.getCatVotes()
            viewModel.saveVote(Vote(oldVote!!.count.plus(1), oldVote!!.catOrDog))
        }
    }

    override fun onResume() {
        super.onResume()
        loadImages()
    }

    private fun loadImages() {
        CoroutineScope(Dispatchers.Main).launch {
            val resultCat = viewModel.getCatPhoto()
            val resultDog = viewModel.getDogPhoto()
            handleCatResponse(resultCat)
            handleDogResponse(resultDog)
        }

    }

    private fun handleCatResponse(result: Response<CatResponse>) {
        if (result.isSuccessful) {
            val photo = result.body()?.get(0)
            Glide.with(this).load(photo!!.url).transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.ivCat)
        } else {
            Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleDogResponse(result: Response<DogResponse>) {
        if (result.isSuccessful) {
            val photo = result.body()?.get(0)
            Glide.with(this).load(photo!!.url).transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.ivDog)
        } else {
            Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
        }
    }
}