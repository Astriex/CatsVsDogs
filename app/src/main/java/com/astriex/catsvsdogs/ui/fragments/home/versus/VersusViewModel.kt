package com.astriex.catsvsdogs.ui.fragments.home.versus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astriex.catsvsdogs.data.repository.PhotoRepository
import com.astriex.catsvsdogs.db.Vote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VersusViewModel @Inject constructor(
    private val repository: PhotoRepository
): ViewModel() {

    suspend fun getCatPhoto() = repository.getCatPhoto()

    suspend fun getDogPhoto() = repository.getDogPhoto()

    fun getCatVotes() = repository.getCatVotes()

    fun getDogVotes() = repository.getDogVotes()

    suspend fun saveVote(vote: Vote) = repository.saveVote(vote)

}