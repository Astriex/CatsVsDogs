package com.astriex.catsvsdogs.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.astriex.catsvsdogs.data.networking.cats.catsList.CatsListApi
import com.astriex.catsvsdogs.data.networking.cats.catsVersus.CatsApi
import com.astriex.catsvsdogs.data.networking.dogs.dogsList.DogsListApi
import com.astriex.catsvsdogs.data.networking.dogs.dogsVersus.DogsApi
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
    @Named("catsList")
    fun provideCatsListRetrofit(): Retrofit = Retrofit.Builder().baseUrl(CatsListApi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()

    @Provides
    @Singleton
    fun provideCatsListApi(@Named("catsList") retrofit: Retrofit): CatsListApi =
        retrofit.create(CatsListApi::class.java)

    @Provides
    @Singleton
    @Named("dogsList")
    fun provideDogsListRetrofit(): Retrofit = Retrofit.Builder().baseUrl(DogsListApi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()

    @Provides
    @Singleton
    fun provideDogsListApi(@Named("dogsList") retrofit: Retrofit): DogsListApi =
        retrofit.create(DogsListApi::class.java)

    @Provides
    @Singleton
    fun provideRepository(
        catsApi: CatsApi,
        dogsApi: DogsApi,
        catsListApi: CatsListApi,
        dogsListApi: DogsListApi,
        voteDao: VoteDao
    ): PhotoRepository =
        PhotoRepository(catsApi, dogsApi, catsListApi, dogsListApi, voteDao)

    @Provides
    @Singleton
    fun provideVotesDatabase(@ApplicationContext app: Context): VotesDatabase =
        Room.databaseBuilder(app, VotesDatabase::class.java, "votesDB").addCallback(rdc).build()

    private val rdc = object : RoomDatabase.Callback() {
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