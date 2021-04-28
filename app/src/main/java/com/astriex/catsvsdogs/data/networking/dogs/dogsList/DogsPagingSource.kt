package com.astriex.catsvsdogs.data.networking.dogs.dogsList

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.astriex.catsvsdogs.data.networking.unsplashList.UnsplashApi
import com.astriex.catsvsdogs.data.networking.unsplashList.UnsplashPhoto
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val DOGS_LIST_STARTING_PAGE_INDEX = 0

class DogsPagingSource @Inject constructor(
    private val dogsListApi: UnsplashApi,
    private val query: String
) :
    PagingSource<Int, UnsplashPhoto>() {
    override fun getRefreshKey(state: PagingState<Int, UnsplashPhoto>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhoto> {
        val position = params.key ?: DOGS_LIST_STARTING_PAGE_INDEX
        return try {
            val response =
                dogsListApi.searchPhotos(query = query, page = position, perPage = params.loadSize)
            val photos = response.results

            LoadResult.Page(
                data = photos,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (photos.isEmpty()) null else position + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }


}