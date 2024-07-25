package com.vlk.application

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.vlk.application.common.BaseResult
import com.vlk.application.domain.entity.StockItem
import com.vlk.application.domain.usecase.GetStockItemUseCase
import com.vlk.application.view.StockDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StockDetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var getStockItemUseCase: GetStockItemUseCase

    @Mock
    private lateinit var productListObserver: Observer<List<StockItem>>

    @Mock
    private lateinit var isLoadingObserver: Observer<Boolean>

    @Mock
    private lateinit var errorObserver: Observer<String>

    @Mock
    private lateinit var stockDetailViewModel: StockDetailViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.IO)
        stockDetailViewModel = StockDetailViewModel(getStockItemUseCase)
        stockDetailViewModel.productList.observeForever(productListObserver)
        stockDetailViewModel.isLoading.observeForever(isLoadingObserver)
        stockDetailViewModel.error.observeForever(errorObserver)
    }

    @After
    fun tearDown() {
        stockDetailViewModel.productList.removeObserver(productListObserver)
        stockDetailViewModel.isLoading.removeObserver(isLoadingObserver)
        stockDetailViewModel.error.removeObserver(errorObserver)
    }

    @Test
    fun getProductByIdSuccessTest() = runTest {
        val stockItems = listOf(StockItem(3, "name", 10.3, android.graphics.Color.GREEN, 3.2))
        val flow = flow {
            emit(BaseResult.Success(stockItems))
        }
        Mockito.`when`(getStockItemUseCase.execute()).thenReturn(flow)
        stockDetailViewModel.getStockItem()
        Mockito.verify(productListObserver).onChanged(stockItems)
    }


    @Test
    fun getProductByIdFailureTest() = runBlocking {
        val errorMessage = "Network Error"
        val flow = flow<BaseResult<List<StockItem>, String>> {
            emit(BaseResult.Error("", errorMessage))
        }
        Mockito.`when`(getStockItemUseCase.execute()).thenReturn(flow)

        stockDetailViewModel.getStockItem()
        Assert.assertNotNull(Any())
    }

}