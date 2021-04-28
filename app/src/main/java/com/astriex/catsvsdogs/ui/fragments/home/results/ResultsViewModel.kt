package com.astriex.catsvsdogs.ui.fragments.home.results

import androidx.lifecycle.ViewModel
import com.astriex.catsvsdogs.data.repository.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResultsViewModel @Inject constructor(private val repository: PhotoRepository) : ViewModel() {

    fun loadVotes() = repository.getVotes()
}