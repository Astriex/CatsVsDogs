package com.astriex.catsvsdogs.ui.fragments.home.cats

import androidx.lifecycle.ViewModel
import com.astriex.catsvsdogs.data.repository.PhotoRepository
import com.astriex.catsvsdogs.db.Vote
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CatsViewModel @Inject constructor(private val repository: PhotoRepository): ViewModel() {

    fun getCatVotes() = repository.getCatVotes()

    suspend fun saveVote(vote: Vote) = repository.saveVote(vote)

    fun getCatPhotos(query: String) = repository.getSearchResults(query)
}