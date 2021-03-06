package com.astriex.catsvsdogs.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVote(vote: Vote)

    @Query("DELETE FROM votes")
    suspend fun deleteVotes()

    @Query("SELECT * FROM votes WHERE cat_or_dog='dog'")
    fun getDogVotes(): Vote

    @Query("SELECT * FROM votes WHERE cat_or_dog='cat'")
    fun getCatVotes(): Vote

}