package com.astriex.catsvsdogs.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Vote::class], version = 1, exportSchema = false)
abstract class VotesDatabase : RoomDatabase() {
    abstract fun voteDao(): VoteDao
}