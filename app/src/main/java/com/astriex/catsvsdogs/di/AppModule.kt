package com.astriex.catsvsdogs.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.astriex.catsvsdogs.data.cats.CatsApi
import com.astriex.catsvsdogs.data.dogs.DogsApi
import com.astriex.catsvsdogs.data.repository.PhotoRepository
import com.astriex.catsvsdogs.db.VoteDao
import com.astriex.catsvsdogs.db.VotesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    @Named("cats")
    fun provideCatsRetrofit(): Retrofit = Retrofit.Builder().baseUrl(CatsApi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()

    @Provides
    @Singleton
    fun provideCatsApi(@Named("cats") retrofit: Retrofit): CatsApi =
        retrofit.create(CatsApi::class.java)

    @Provides
    @Singleton
    @Named("dogs")
    fun provideDogsRetrofit(): Retrofit = Retrofit.Builder().baseUrl(DogsApi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()

    @Provides
    @Singleton
    fun provideDogsApi(@Named("dogs") retrofit: Retrofit): DogsApi =
        retrofit.create(DogsApi::class.java)

    @Provides
    @Singleton
    fun provideRepository(catsApi: CatsApi, dogsApi: DogsApi, voteDao: VoteDao): PhotoRepository =
        PhotoRepository(catsApi, dogsApi, voteDao)

    @Provides
    @Singleton
    fun provideVotesDatabase(@ApplicationContext app: Context): VotesDatabase =
        Room.databaseBuilder(app, VotesDatabase::class.java, "votesDB").addCallback(rdc).build()

    private val rdc = object: RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                db.execSQL("INSERT INTO votes VALUES(0, 'cat')")
                db.execSQL("INSERT INTO votes VALUES(0, 'dog')")
            }
        }
    }

    @Provides
    @Singleton
    fun provideVoteDao(votesDatabase: VotesDatabase): VoteDao = votesDatabase.voteDao()

}