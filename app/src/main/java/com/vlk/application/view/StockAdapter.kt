package com.vlk.application.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vlk.application.R
import com.vlk.application.domain.entity.StockItem
import java.util.Locale

/**
 * Provide a reference to the type of views that you are using
 * (custom ViewHolder)
 */

class StockAdapter() : RecyclerView.Adapter<StockAdapter.StockViewHolder>() {

    private var stockItemList: List<StockItem> = listOf()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): StockViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.stock_list_item, viewGroup, false)
        return StockViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: StockViewHolder, position: Int) {
        viewHolder.bind(stockItemList[position])
    }

    override fun getItemCount() = stockItemList.size

    class StockViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvStockName: TextView = view.findViewById(R.id.tvStockName)
        private val tvStockPrice: TextView = view.findViewById(R.id.tvStockPrice)
        private val tvPriceDifference: TextView = view.findViewById(R.id.tvPriceDifference)
        private val locale = Locale.US

        fun bind(stockItem: StockItem) {
            tvStockName.text = stockItem.name
            tvStockPrice.text = String.format(locale, "Є%.2f", stockItem.price)
            tvStockPrice.setTextColor(stockItem.color)
            if (stockItem.priceChange != 0.0)
                tvPriceDifference.text = String.format(locale, "Є%.2f", stockItem.priceChange)
        }
    }

    /**
     * Update the stock items in the adapter and notify changes.
     */
    fun updateProducts(newStockItemList: List<StockItem>) {
        stockItemList = newStockItemList
        notifyDataSetChanged()
    }

}
