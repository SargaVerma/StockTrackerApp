package com.vlk.application.domain.usecase

import com.vlk.application.common.BaseResult
import com.vlk.application.domain.entity.StockItem
import com.vlk.application.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStockItemUseCase @Inject constructor(private val productRepository: StockRepository) {
    suspend fun execute(): Flow<BaseResult<List<StockItem>, String>> {
        return productRepository.getStockItems()
    }
}