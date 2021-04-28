package com.astriex.catsvsdogs.ui.fragments.home.cats

import androidx.lifecycle.ViewModel
import com.astriex.catsvsdogs.data.repository.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CatsViewModel @Inject constructor(private val repository: PhotoRepository): ViewModel() {

    fun getAllCatImages() = repository.getAllCats()

}