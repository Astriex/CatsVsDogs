package com.astriex.catsvsdogs.data.networking.cats.catsList

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.astriex.catsvsdogs.data.networking.unsplashList.UnsplashApi
import com.astriex.catsvsdogs.data.networking.unsplashList.UnsplashPhoto
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val CATS_LIST_STARTING_PAGE_INDEX = 1

class CatsListPagingSource @Inject constructor(
    private val unsplashApi: UnsplashApi,
    private val query: String
) :
    PagingSource<Int, UnsplashPhoto>() {
    override fun getRefreshKey(state: PagingState<Int, UnsplashPhoto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhoto> {
        val position = params.key ?: CATS_LIST_STARTING_PAGE_INDEX
        return try {
            val response =
                unsplashApi.searchPhotos(page = position, perPage = params.loadSize, query = query)
            val photos = response.results

            LoadResult.Page(
                data = photos,
                prevKey = if (position == CATS_LIST_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (photos.isEmpty()) null else position + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }

    }

}