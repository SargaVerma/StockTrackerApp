package com.vlk.application.domain.repository

import com.vlk.application.common.BaseResult
import com.vlk.application.domain.entity.StockItem
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun getStockItems(): Flow<BaseResult<List<StockItem>, String>>
}