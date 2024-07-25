package com.vlk.application.data.networking

import com.vlk.application.data.model.StockListResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("stock_list")
    suspend fun getListOfStock(): Response<List<StockListResponse>>
}