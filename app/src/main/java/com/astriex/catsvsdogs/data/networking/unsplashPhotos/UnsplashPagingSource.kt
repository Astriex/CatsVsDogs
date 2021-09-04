package com.astriex.catsvsdogs.data.networking.unsplashPhotos

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.astriex.catsvsdogs.data.networking.unsplashPhotos.UnsplashApi
import com.astriex.catsvsdogs.data.networking.unsplashPhotos.UnsplashPhoto
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val STARTING_PAGE_INDEX = 0

class UnsplashPagingSource @Inject constructor(
    private val listApi: UnsplashApi,
    private val query: String
) :
    PagingSource<Int, UnsplashPhoto>() {
    override fun getRefreshKey(state: PagingState<Int, UnsplashPhoto>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhoto> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response =
                listApi.searchPhotos(query = query, page = position, perPage = params.loadSize)
            val photos = response.results

            LoadResult.Page(
                data = photos,
                prevKey = if (position == 0) null else position - 1,
                nextKey = if (photos.isEmpty()) null else position + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

}