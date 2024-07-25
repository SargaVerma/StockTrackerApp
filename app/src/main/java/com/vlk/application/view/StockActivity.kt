package com.vlk.application.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.vlk.application.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StockActivity : AppCompatActivity() {

    private val viewModel: StockDetailViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var stockListAdapter: StockAdapter
    private lateinit var mLoadingProgressBar: ProgressBar
    private lateinit var stopBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock_list)
        initializeVariables()
        observeData()
    }

    private fun observeData() {
        // Observe the LiveData from the ViewModel
        viewModel.productList.observe(this) { list ->
            stockListAdapter.updateProducts(list)
        }
        viewModel.isLoading.observe(this) { isLoading ->
            handleLoading(isLoading)
        }
        // Observe the error state LiveData
        viewModel.error.observe(this) { errorMessage ->
            handleError(errorMessage + getString(R.string.error_message))
        }
    }

    private fun initializeVariables() {
        mLoadingProgressBar = findViewById(R.id.progressBarView)
        stopBtn = findViewById(R.id.stopBtn)
        recyclerView = findViewById(R.id.recyclerView)

        // Initialize the RecyclerView and the adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        stockListAdapter = StockAdapter()
        recyclerView.adapter = stockListAdapter

        stopBtn.setOnClickListener { viewModel.stopRefreshData() }

    }

    private fun handleLoading(isLoading: Boolean) {
        if (isLoading) {
            mLoadingProgressBar.visibility = View.VISIBLE
        } else {
            mLoadingProgressBar.visibility = View.GONE
        }
    }

    private fun handleError(errorMessage: String) {
        Snackbar.make(findViewById(R.id.rootLayout), errorMessage, Snackbar.LENGTH_LONG).show()
    }

}
