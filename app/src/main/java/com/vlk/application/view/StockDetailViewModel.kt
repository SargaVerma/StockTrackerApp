package com.vlk.application.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlk.application.common.BaseResult
import com.vlk.application.domain.entity.StockItem
import com.vlk.application.domain.usecase.GetStockItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockDetailViewModel @Inject constructor(
    private val getStockUseCase: GetStockItemUseCase
) : ViewModel() {

    private val _productList = MutableLiveData<List<StockItem>>()
    val productList: LiveData<List<StockItem>> get() = _productList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private var apiJob: Job? = null

    init {
        startApiCall()
    }

    private fun startApiCall() {
        apiJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                getStockItem()
                delay(2000)
            }
        }
    }

    suspend fun getStockItem() {
        getStockUseCase.execute().onStart {
            setLoading(true)
        }.catch {
            setLoading(false)
        }.collect { result ->
            setLoading(false)
            when (result) {
                is BaseResult.Success -> {
                    setResult(result.data)
                }

                is BaseResult.Error -> {
                    result.message?.let { setError(it) }
                }
            }
        }
    }

    fun stopRefreshData() {
        apiJob?.cancel()
    }

    private fun setResult(stockItems: List<StockItem>) {
        _productList.postValue(stockItems)
    }


    private fun setLoading(isLoading: Boolean) {
        _isLoading.postValue(isLoading)
    }

    private fun setError(errorMessage: String) {
        _error.postValue(errorMessage)
    }

}