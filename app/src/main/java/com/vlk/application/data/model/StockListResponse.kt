package com.vlk.application.data.model

import com.google.gson.annotations.SerializedName

data class StockListResponse(
    @SerializedName("id") var id: Int,
    @SerializedName("name") var name: String,
    @SerializedName("price") var price: Double,
)
