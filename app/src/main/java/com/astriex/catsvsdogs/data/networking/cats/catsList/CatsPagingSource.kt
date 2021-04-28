package com.astriex.catsvsdogs.data.networking.cats.catsList

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val CATS_LIST_STARTING_PAGE_INDEX = 0

class CatsListPagingSource @Inject constructor(private val catsListApi: CatsListApi) :
    PagingSource<Int, AllCatImagesResponseItem>() {
    override fun getRefreshKey(state: PagingState<Int, AllCatImagesResponseItem>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AllCatImagesResponseItem> {
        val position = params.key ?: CATS_LIST_STARTING_PAGE_INDEX
        return try {
            val response = catsListApi.getAllCatImages(limit = params.loadSize, page = position)
            val photos = response.toList()

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