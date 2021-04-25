package com.astriex.catsvsdogs.data.repository

import com.astriex.catsvsdogs.data.cats.CatsApi
import com.astriex.catsvsdogs.data.dogs.DogsApi
import com.astriex.catsvsdogs.db.Vote
import com.astriex.catsvsdogs.db.VoteDao
import javax.inject.Inject

class PhotoRepository @Inject constructor(
    private val catsApi: CatsApi,
    private val dogsApi: DogsApi,
    private val voteDao: VoteDao
) {

    suspend fun getCatPhoto() = catsApi.getRandomCatPhoto()

    suspend fun getDogPhoto() = dogsApi.getRandomDogPhoto()

    suspend fun saveVote(vote: Vote) = voteDao.insertVote(vote)

    fun getDogVotes() = voteDao.getDogVotes()

    fun getCatVotes() = voteDao.getCatVotes()

    suspend fun deleteVotes() = voteDao.deleteVotes()

}