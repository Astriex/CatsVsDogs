package com.astriex.catsvsdogs.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.astriex.catsvsdogs.data.networking.cats.catsList.CatsListApi
import com.astriex.catsvsdogs.data.networking.cats.catsList.CatsListPagingSource
import com.astriex.catsvsdogs.data.networking.cats.catsVersus.CatsApi
import com.astriex.catsvsdogs.data.networking.dogs.dogsList.DogsListApi
import com.astriex.catsvsdogs.data.networking.dogs.dogsList.DogsPagingSource
import com.astriex.catsvsdogs.data.networking.dogs.dogsVersus.DogsApi
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
    private val catsListApi: CatsListApi,
    private val dogsListApi: DogsListApi,
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

    fun getAllCats() = Pager(
        config = PagingConfig(
            pageSize = 9,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { CatsListPagingSource(catsListApi) }
    ).liveData

    suspend fun getAllDogImages() = dogsListApi.getAllDogImages()

    fun getAllDogs() = Pager(
        config = PagingConfig(
            pageSize = 9,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { DogsPagingSource(dogsListApi) }
    ).liveData

}