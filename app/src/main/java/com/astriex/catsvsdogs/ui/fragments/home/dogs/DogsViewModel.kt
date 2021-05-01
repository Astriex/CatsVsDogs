package com.astriex.catsvsdogs.ui.fragments.home.dogs

import androidx.lifecycle.ViewModel
import com.astriex.catsvsdogs.data.repository.PhotoRepository
import com.astriex.catsvsdogs.db.Vote
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DogsViewModel @Inject constructor(private val repository: PhotoRepository) : ViewModel() {

    fun getAllDogs(query: String) = repository.getSearchResults(query)

    fun getDogVotes() = repository.getDogVotes()

    suspend fun saveVote(vote: Vote) = repository.saveVote(vote)

}