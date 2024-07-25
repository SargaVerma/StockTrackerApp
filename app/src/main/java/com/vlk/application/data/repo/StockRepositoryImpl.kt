package com.vlk.application.data.repo

import com.vlk.application.data.networking.ApiService
import com.vlk.application.common.BaseResult
import com.vlk.application.domain.entity.StockItem
import com.vlk.application.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class StockRepositoryImpl(private val apiService: ApiService) :
    StockRepository {

    private val MIN_ADJUSTMENT: Double = -5.0
    private val MAX_ADJUSTMENT: Double = 5.0
    private var cachedItems: MutableMap<Int, Double> = mutableMapOf()

    override suspend fun getStockItems(): Flow<BaseResult<List<StockItem>, String>> {
        return flow {
            val response = apiService.getListOfStock()
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()
                val stockItems = data?.map { productData ->
                    val previousPrice = cachedItems[productData.id] ?: productData.price
                    val productPrice = if (cachedItems.containsKey(productData.id)) {
                        adjustPrice(productData.price)
                    } else {
                        productData.price
                    }

                    // Calculate the price change and update the cache
                    val priceChange = productPrice - previousPrice
                    cachedItems[productData.id] = productPrice
                    StockItem(
                        id = productData.id,
                        name = productData.name,
                        price = productPrice,
                        color = determineColor(priceChange),
                        priceChange = priceChange
                    )
                } ?: emptyList()
                emit(BaseResult.Success(stockItems.sortedByDescending { it.price }.take(5)))
            } else {
                emit(
                    BaseResult.Error(
                        error = "Unknown error",
                        message = response.message()
                    )
                )
            }
        }
    }

    private fun adjustPrice(price: Double): Double {
        val change = Random.nextDouble(MIN_ADJUSTMENT, MAX_ADJUSTMENT)
        return price + change
    }

    // Determine the color based on price change
    private fun determineColor(priceChange: Double): Int {
        return when {
            priceChange > 0 -> android.graphics.Color.GREEN
            priceChange < 0 -> android.graphics.Color.RED
            else -> android.graphics.Color.BLACK
        }
    }


}