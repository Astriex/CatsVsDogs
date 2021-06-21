package com.astriex.catsvsdogs.ui.fragments.home.versus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.astriex.catsvsdogs.R
import com.astriex.catsvsdogs.data.networking.cats.catsVersus.CatResponse
import com.astriex.catsvsdogs.data.networking.dogs.dogsVersus.DogResponse
import com.astriex.catsvsdogs.databinding.FragmentVersusBinding
import com.astriex.catsvsdogs.db.Vote
import com.astriex.catsvsdogs.util.hide
import com.astriex.catsvsdogs.util.isConnected
import com.astriex.catsvsdogs.util.show
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

@AndroidEntryPoint
class VersusFragment : Fragment(R.layout.fragment_versus) {
    private lateinit var circularProgressDrawableCats: CircularProgressDrawable
    private lateinit var circularProgressDrawableDogs: CircularProgressDrawable
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

        setupGlideCircularProgressDrawable()

        setupListeners()

        binding.ivCat.setOnClickListener {
            saveCatVote()
            loadImages()
        }

        binding.ivDog.setOnClickListener {
            saveDogVote()
            loadImages()
        }
    }

    private fun setupListeners() {
        binding.btnRetry.setOnClickListener {
            loadImages()
        }
    }

    private fun setupGlideCircularProgressDrawable() {
        circularProgressDrawableCats = CircularProgressDrawable(requireContext())
        circularProgressDrawableCats.apply {
            setStyle(CircularProgressDrawable.LARGE)
            setColorSchemeColors(R.color.pieChartCats, R.color.pieChartDogs, R.color.colorAccent)
            strokeWidth = 5f
            centerRadius = 30f
            start()
        }

        circularProgressDrawableDogs = CircularProgressDrawable(requireContext())
        circularProgressDrawableDogs.apply {
            setStyle(CircularProgressDrawable.LARGE)
            setColorSchemeColors(R.color.pieChartCats, R.color.pieChartDogs, R.color.colorAccent)
            strokeWidth = 5f
            centerRadius = 30f
            start()
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
        if (isConnected()) {
            showOnlineView()
            loadOnlineImages()
        } else {
            showOfflineView()
        }
    }

    private fun showOfflineView() {
        binding.apply {
            ivDog.hide()
            ivCat.hide()
            cvCat.hide()
            cvDog.hide()
            tvError.show()
            btnRetry.show()
        }
    }

    private fun loadOnlineImages() {
        CoroutineScope(Dispatchers.Main).launch {
            val resultCat = viewModel.getCatPhoto()
            val resultDog = viewModel.getDogPhoto()
            handleCatResponse(resultCat)
            handleDogResponse(resultDog)
        }
    }

    private fun showOnlineView() {
        binding.apply {
            ivDog.show()
            ivCat.show()
            cvCat.show()
            cvDog.show()
            tvError.hide()
            btnRetry.hide()
        }
    }

    private fun handleCatResponse(result: Response<CatResponse>) {
        if (result.isSuccessful) {
            val photo = result.body()?.get(0)
            Glide.with(this)
                .load(photo!!.url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(circularProgressDrawableCats)
                .error(R.drawable.ic_error)
                .timeout(5000)
                .into(binding.ivCat)
        } else {
            Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleDogResponse(result: Response<DogResponse>) {
        if (result.isSuccessful) {
            val photo = result.body()?.get(0)
            Glide.with(this)
                .load(photo!!.url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(circularProgressDrawableDogs)
                .error(R.drawable.ic_error)
                .timeout(5000)
                .into(binding.ivDog)
        } else {
            Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}