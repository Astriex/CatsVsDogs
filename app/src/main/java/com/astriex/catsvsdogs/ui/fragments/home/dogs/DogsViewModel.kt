package com.astriex.catsvsdogs.ui.fragments.home.dogs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astriex.catsvsdogs.data.repository.PhotoRepository
import com.astriex.catsvsdogs.db.Vote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DogsViewModel @Inject constructor(private val repository: PhotoRepository): ViewModel() {

    fun getAllDogs() = repository.getAllDogs()

    fun getDogVotes() = repository.getDogVotes()

    suspend fun saveVote(vote: Vote) = repository.saveVote(vote)

}