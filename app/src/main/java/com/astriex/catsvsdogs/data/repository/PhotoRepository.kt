package com.astriex.catsvsdogs.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.astriex.catsvsdogs.data.networking.cats.catsVersus.CatsApi
import com.astriex.catsvsdogs.data.networking.dogs.dogsVersus.DogsApi
import com.astriex.catsvsdogs.data.networking.unsplashPhotos.UnsplashApi
import com.astriex.catsvsdogs.data.networking.unsplashPhotos.UnsplashPagingSource
import com.astriex.catsvsdogs.db.Vote
import com.astriex.catsvsdogs.db.VoteDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PhotoRepository @Inject constructor(
    private val catsApi: CatsApi,
    private val dogsApi: DogsApi,
    private val unsplashApi: UnsplashApi,
    private val voteDao: VoteDao
) {

    suspend fun getCatPhoto() = catsApi.getRandomCatPhoto()

    suspend fun getDogPhoto() = dogsApi.getRandomDogPhoto()

    suspend fun saveVote(vote: Vote) = voteDao.insertVote(vote)

    fun getDogVotes() = voteDao.getDogVotes()

    fun getCatVotes() = voteDao.getCatVotes()

    suspend fun deleteVotes() = voteDao.deleteVotes()

    fun getVotes(): Flow<List<Vote>> = flow<List<Vote>> {
        val catVotes = getCatVotes()
        val dogVotes = getDogVotes()
        val votes = mutableListOf<Vote>()

        votes.add(catVotes)
        votes.add(dogVotes)
        emit(votes)
    }.flowOn(Dispatchers.IO)

    fun getSearchResults(query: String) = Pager(
        config = PagingConfig(
            pageSize = 5,
            maxSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { UnsplashPagingSource(unsplashApi, query) }
    ).liveData

}