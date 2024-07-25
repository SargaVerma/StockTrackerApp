package com.vlk.application.domain.entity

data class StockItem(
    var id: Int,
    var name: String,
    var price: Double,
    var color: Int,
    var priceChange: Double
)
