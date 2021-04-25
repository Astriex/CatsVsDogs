package com.astriex.catsvsdogs.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "votes")
data class Vote(
    @ColumnInfo(name = "vote_value")
    var count: Int,
    @PrimaryKey
    @ColumnInfo(name = "cat_or_dog")
    var catOrDog: String
)
