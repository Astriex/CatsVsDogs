package com.astriex.catsvsdogs.data.networking.dogs.dogsList

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val DOGS_LIST_STARTING_PAGE_INDEX = 0

class DogsPagingSource @Inject constructor(private val dogsListApi: DogsListApi) :
    PagingSource<Int, AllDogImagesResponseItem>() {
    override fun getRefreshKey(state: PagingState<Int, AllDogImagesResponseItem>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AllDogImagesResponseItem> {
        val position = params.key ?: DOGS_LIST_STARTING_PAGE_INDEX
        return try {
            val response = dogsListApi.getAllDogImages(page = position)
            val photos = response.toList()

            LoadResult.Page(
                data = photos,
                prevKey = if(position == 1) null else position - 1,
                nextKey = if(photos.isEmpty()) null else position + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }


}